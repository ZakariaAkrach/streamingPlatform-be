package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.CastDTO;
import com.zakaria.streamingPlatform.dto.GenresDTO;
import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.dto.SeasonDTO;
import com.zakaria.streamingPlatform.entities.*;
import com.zakaria.streamingPlatform.external.TheMovieDbModel;
import com.zakaria.streamingPlatform.external.TheMovieDbPaginationModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbCastModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbDetailModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbGenresMovieModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbSeason;
import com.zakaria.streamingPlatform.mapper.CastMapper;
import com.zakaria.streamingPlatform.mapper.GenresMapper;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.mapper.SeasonMapper;
import com.zakaria.streamingPlatform.repository.*;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenresRepository genresRepository;
    private final CastRepository castRepository;
    private final SeasonRepository seasonRepository;
    private final MovieCastRepository movieCastRepository;
    private final GenresMapper genresMapper;
    private final CastMapper castMapper;
    private final SeasonMapper seasonMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, GenresRepository genresRepository,
                        CastRepository castRepository, SeasonRepository seasonRepository, MovieCastRepository movieCastRepository,
                        GenresMapper genresMapper, CastMapper castMapper, SeasonMapper seasonMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genresRepository = genresRepository;
        this.castRepository = castRepository;
        this.seasonRepository = seasonRepository;
        this.movieCastRepository = movieCastRepository;
        this.genresMapper = genresMapper;
        this.castMapper = castMapper;
        this.seasonMapper = seasonMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final List<String> LANGUAGES_ALLOWED = List.of(
            "en", // English (USA, Australia, Canada)
            "es", // Spanish (Argentina)
            "nl", // Dutch (Belgium)
            "fr", // French (Belgium, Canada, Morocco)
            "de", // German (Belgium)
            "pt", // Portuguese (Brazil)
            "zh", // Chinese (China)
            "da", // Danish (Denmark)
            "ru", // Russian (Russia)
            "ar", // Arabic (Morocco)
            "th"  // Thai (Thailand)
    );

    @Value("${movies-api-key}")
    private String movieApiKey;

    public void addMoviesFromDbApi(String typeMovieApi) {
        List<TheMovieDbPaginationModel> result = getPaginatedMoviesFromApi(typeMovieApi);
        List<MovieDTO> listMovieDTO = new ArrayList<>();

        for (TheMovieDbPaginationModel theMovieDbPaginationModel : result) {
            for (TheMovieDbModel theMovieDbModel : theMovieDbPaginationModel.getResults()) {
                setMovieModel(theMovieDbModel, listMovieDTO, typeMovieApi);
            }
        }
        int countMovieAdded = 0;
        for (MovieDTO singleMovieDTO : listMovieDTO) {
            if (isMovieModelValid(singleMovieDTO)) {
                Optional<MovieEntity> existingMovie = movieRepository.findByIdTheMovieDb(singleMovieDTO.getIdTheMovieDb());
                if (existingMovie.isEmpty()) {
                    MovieEntity movieEntity = movieMapper.convertToEntity(singleMovieDTO);
                    movieEntity.setGenres(new ArrayList<>());
                    movieRepository.save(movieEntity);
                    countMovieAdded++;
                    logger.info("Added movie {}", singleMovieDTO.getTitle());
                }
            }
        }
        if (countMovieAdded > 0) {
            addMovieDetail(typeMovieApi);
        }
        logger.info("Total {} added {}", typeMovieApi, countMovieAdded);
    }

    public void addMovieDetail(String typeMovieApi) {

        List<MovieEntity> allMovie;
        if (typeMovieApi.equals("movie")) {
            allMovie = movieRepository.findAllByTypeMovie(TypeMovie.MOVIE);
        } else {
            allMovie = movieRepository.findAllByTypeMovie(TypeMovie.TV_SHOW);
        }

        for (MovieEntity movieEntity : allMovie) {
            logger.info("Processing {} title {}", typeMovieApi, movieEntity.getTitle());
            List<TheMovieDbDetailModel> listTheMovieDbDetailModel = getDetailMovies(typeMovieApi, movieEntity.getIdTheMovieDb());
            List<GenresEntity> genresEntitiesList = new ArrayList<>();
            List<SeasonEntity> seasonEntityList = new ArrayList<>();
            for (TheMovieDbDetailModel theMovieDbDetailModel : listTheMovieDbDetailModel) {
                if (theMovieDbDetailModel.getGenres() != null && theMovieDbDetailModel.getCredits() != null) {
                    for (TheMovieDbGenresMovieModel theMovieDbGenresMovieModel : theMovieDbDetailModel.getGenres()) {
                        Optional<GenresEntity> genresEntity = genresRepository.findByName(theMovieDbGenresMovieModel.getName());
                        if (genresEntity.isEmpty()) {
                            GenresDTO genresDTO = setGenreDTOBeforeSaving(theMovieDbGenresMovieModel);
                            genresEntitiesList.add(genresRepository.save(genresMapper.convertToEntity(genresDTO)));
                        } else {
                            genresEntitiesList.add(genresEntity.get());
                        }
                    }
                    for (TheMovieDbCastModel theMovieDbCreditsModel : theMovieDbDetailModel.getCredits().getCast()) {
                        Optional<CastEntity> castEntityOptional = castRepository.findByName(theMovieDbCreditsModel.getName());
                        MovieCastEntity movieCastEntity = new MovieCastEntity();
                        CastEntity castEntity;

                        movieCastEntity.setMovie(movieEntity);
                        movieCastEntity.setCharacterName(theMovieDbCreditsModel.getCharacter());

                        if (castEntityOptional.isEmpty()) {
                            CastDTO castDTO = setCastDTOBeforeSaving(theMovieDbCreditsModel);
                            castEntity = castRepository.save(castMapper.convertToEntity(castDTO));
                        } else {
                            castEntity = castEntityOptional.get();
                        }
                        movieCastEntity.setCast(castEntity);
                        movieCastRepository.save(movieCastEntity);
                    }
                    if (typeMovieApi.equals("tv")) {
                        for (TheMovieDbSeason theMovieDbSeason : theMovieDbDetailModel.getSeasons()) {
                            Optional<SeasonEntity> existingSeason = seasonRepository.findByIdTheMovieDb(theMovieDbSeason.getId());
                            if (existingSeason.isEmpty()) {
                                SeasonDTO seasonDTO = setSeasonDTOBeforeSaving(theMovieDbSeason);
                                SeasonEntity seasonEntity = seasonMapper.convertToEntity(seasonDTO);
                                seasonEntity.setMovie(movieEntity);
                                seasonEntityList.add(seasonRepository.save(seasonEntity));
                            }
                        }
                    }
                }
            }
            movieEntity.setGenres(genresEntitiesList);
            movieEntity.setSeasons(seasonEntityList);
            movieEntity.setRuntime(listTheMovieDbDetailModel.get(0).getRuntime()); //it is always the index 0
            movieRepository.save(movieEntity);
        }
    }

    private boolean isMovieModelValid(MovieDTO singleMovieDTO) {
        return singleMovieDTO != null &&
                (singleMovieDTO.getBackdropPath() != null && !singleMovieDTO.getBackdropPath().isEmpty()) &&
                (singleMovieDTO.getPosterPath() != null && !singleMovieDTO.getPosterPath().isEmpty()) &&
                (singleMovieDTO.getDescription() != null && !singleMovieDTO.getDescription().isEmpty());
    }

    private static void setMovieModel(TheMovieDbModel theMovieDbModel, List<MovieDTO> listMovieDTO, String typeMovieApi) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setIdTheMovieDb(theMovieDbModel.getId());
        if (typeMovieApi.equals("movie")) {
            movieDTO.setTypeMovie(TypeMovie.MOVIE);
            movieDTO.setTitle(theMovieDbModel.getTitle());
            movieDTO.setReleaseDate(theMovieDbModel.getRelease_date());
        } else {
            movieDTO.setTypeMovie(TypeMovie.TV_SHOW);
            movieDTO.setTitle(theMovieDbModel.getName());
            movieDTO.setReleaseDate(theMovieDbModel.getFirst_air_date());
        }
        movieDTO.setLanguage(theMovieDbModel.getOriginal_language());
        movieDTO.setDescription(theMovieDbModel.getOverview());
        movieDTO.setPosterPath(theMovieDbModel.getPoster_path());
        movieDTO.setBackdropPath(theMovieDbModel.getBackdrop_path());
        movieDTO.setPopularity(theMovieDbModel.getPopularity());
        movieDTO.setActive(true);
        movieDTO.setDateCreated(LocalDate.now());

        listMovieDTO.add(movieDTO);
    }

    private String getBaseUrl(String typeMovieApi) {
        return "https://api.themoviedb.org/3/discover/" + typeMovieApi + "?api_key=" + movieApiKey;
    }

    private String getBaseMovieDetailUrl(String typeMovieApi, int idTheMovieDb) {
        return "https://api.themoviedb.org/3/" + typeMovieApi + "/" + idTheMovieDb + "?api_key=" + movieApiKey + "&append_to_response=credits";
    }

    public HttpHeaders setHeaderMoviesFromDbApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    public List<TheMovieDbPaginationModel> getPaginatedMoviesFromApi(String typeMovieApi) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(setHeaderMoviesFromDbApi());
        List<TheMovieDbPaginationModel> result = new ArrayList<>();

        int lengthLoop = 0;

        // Added this logic since some fields (e.g. overview) are often missing from TMDB,
        // so I extended the loop to ensure at least 1000 records are saved.
        if (typeMovieApi.equals("movie")) {
            lengthLoop = 6;
        } else {
            lengthLoop = 10;
        }

        for (int i = 1; i <= lengthLoop; i++) {
            for (String languageAllowed : LANGUAGES_ALLOWED) {
                String paginatedUrl = getBaseUrl(typeMovieApi) + "&page=" + i + "&with_original_language=" + languageAllowed;
                logger.info("Call The Movie Db Api with Page Number {} Language {}", i, languageAllowed);

                ResponseEntity<TheMovieDbPaginationModel> response = restTemplate.exchange(
                        paginatedUrl,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {
                        });
                if (response.getBody() != null) {
                    result.add(response.getBody());
                }
            }
        }
        return result;
    }

    public List<TheMovieDbDetailModel> getDetailMovies(String typeMovieApi, int idTheMovieDb) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(setHeaderMoviesFromDbApi());
        List<TheMovieDbDetailModel> result = new ArrayList<>();

        String detailMovieUrl = getBaseMovieDetailUrl(typeMovieApi, idTheMovieDb);

        int maxRetries = 5;
        int attempt = 0;
        long waitTimeMs = 2000;

        while (attempt < maxRetries) {
            try {
                ResponseEntity<TheMovieDbDetailModel> response = restTemplate.exchange(
                        detailMovieUrl,
                        HttpMethod.GET,
                        request,
                        TheMovieDbDetailModel.class);
                if (response.getBody() != null) {
                    result.add(response.getBody());
                    break;
                }
            } catch (Exception e) {
                logger.info("Try {} failed: {}", attempt + 1, e.getMessage());
            }

            attempt++;
            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        return result;
    }

    private SeasonDTO setSeasonDTOBeforeSaving(TheMovieDbSeason theMovieDbSeason) {
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setAirDate(theMovieDbSeason.getAir_date());
        seasonDTO.setEpisodeCount(theMovieDbSeason.getEpisode_count());
        seasonDTO.setIdTheMovieDb(theMovieDbSeason.getId());
        seasonDTO.setSeasonNumber(theMovieDbSeason.getSeason_number());

        return seasonDTO;
    }

    private CastDTO setCastDTOBeforeSaving(TheMovieDbCastModel theMovieDbCreditsModel) {
        CastDTO castDTO = new CastDTO();
        castDTO.setName(theMovieDbCreditsModel.getName());
        castDTO.setOriginal_name(theMovieDbCreditsModel.getOriginal_name());
        castDTO.setProfile_path(theMovieDbCreditsModel.getProfile_path());

        return castDTO;
    }

    private GenresDTO setGenreDTOBeforeSaving(TheMovieDbGenresMovieModel theMovieDbGenresMovieModel) {
        GenresDTO genresDTO = new GenresDTO();
        genresDTO.setName(theMovieDbGenresMovieModel.getName());

        return genresDTO;
    }

    @Cacheable("allMovie")
    public ResponsePagination<MovieDTO> getAllMovie(Pageable pageable) {
        ResponsePagination<MovieDTO> response = new ResponsePagination<>();
        List<MovieDTO> movieDTO = new ArrayList<>();

        Page<MovieEntity> movieEntityPage = movieRepository.findAllByTypeMovie(TypeMovie.MOVIE, pageable);

        if (!movieEntityPage.isEmpty()) {
            movieDTO = movieEntityPage.get()
                    .map(movieMapper::convertToModel)
                    .toList();
            logger.info("Get all movies successfully");
            return Utils.createResponsePagination(HttpStatus.OK.value(), "Get all movies successfully", movieDTO, pageable.getPageNumber(),
                    pageable.getPageSize(), movieEntityPage.getTotalPages(), movieEntityPage.getTotalElements(), movieEntityPage.isLast(), null);
        }
        logger.info("No movie available");
        return Utils.createResponsePagination(HttpStatus.NO_CONTENT.value(), "No movie available", "No movie available");
    }

    @Cacheable("getTrendingMovie")
    public Response<List<MovieDTO>> getTrendingMovie() {
        List<MovieDTO> movieDTO;

        Pageable top24 = PageRequest.of(0, 24);
        List<MovieEntity> movieEntity = movieRepository.trendingByTypeMovie(TypeMovie.MOVIE, top24);

        if (!movieEntity.isEmpty()) {
            movieDTO = movieEntity.stream().map(movieMapper::convertToModel).collect(Collectors.toList());
            logger.info("trending movie returned successfully");
            return Utils.createResponse(HttpStatus.OK.value(), "trending movie returned successfully", null, movieDTO);
        }
        logger.info("trending movies no data found");
        return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "trending movies no data found", List.of("trending movies no data found"), null);
    }


    @Cacheable("getTrendingTvShow")
    public Response<List<MovieDTO>> getTrendingTvShow() {
        List<MovieDTO> movieDTO;

        Pageable top24 = PageRequest.of(0, 24);
        List<MovieEntity> movieEntity = movieRepository.trendingByTypeMovie(TypeMovie.TV_SHOW, top24);

        if (!movieEntity.isEmpty()) {
            movieDTO = movieEntity.stream().map(movieMapper::convertToModel).collect(Collectors.toList());
            logger.info("trending tv shows returned successfully");
            return Utils.createResponse(HttpStatus.OK.value(), "trending tv shows returned successfully", null, movieDTO);
        }
        logger.info("trending tv shows no data found");
        return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "trending tv shows no data found", List.of("trending tv shows no data found"), null);
    }
}