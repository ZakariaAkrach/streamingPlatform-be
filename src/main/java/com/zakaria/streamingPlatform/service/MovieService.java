package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.*;
import com.zakaria.streamingPlatform.entities.*;
import com.zakaria.streamingPlatform.external.TheMovieDbModel;
import com.zakaria.streamingPlatform.external.TheMovieDbPaginationModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbCastModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbDetailModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbGenresMovieModel;
import com.zakaria.streamingPlatform.external.detailMovieModels.TheMovieDbSeason;
import com.zakaria.streamingPlatform.mapper.*;
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
    private final UserRepository userRepository;
    private final UserMovieLikeRepository userMovieLikeRepository;
    private final UserMovieFavoriteRepository userMovieFavoriteRepository;
    private final GenresMapper genresMapper;
    private final CastMapper castMapper;
    private final SeasonMapper seasonMapper;
    private final UserMovieLikeMapper userMovieLikeMapper;
    private final UserMovieFavoriteMapper userMovieFavoriteMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, GenresRepository genresRepository,
                        CastRepository castRepository, SeasonRepository seasonRepository, MovieCastRepository movieCastRepository,
                        UserRepository userRepository, UserMovieLikeRepository userMovieLikeRepository, UserMovieFavoriteRepository userMovieFavoriteRepository,
                        GenresMapper genresMapper, CastMapper castMapper, SeasonMapper seasonMapper, UserMovieLikeMapper userMovieLikeMapper, UserMovieFavoriteMapper userMovieFavoriteMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genresRepository = genresRepository;
        this.castRepository = castRepository;
        this.seasonRepository = seasonRepository;
        this.movieCastRepository = movieCastRepository;
        this.userRepository = userRepository;
        this.userMovieLikeRepository = userMovieLikeRepository;
        this.userMovieFavoriteRepository = userMovieFavoriteRepository;
        this.genresMapper = genresMapper;
        this.castMapper = castMapper;
        this.seasonMapper = seasonMapper;
        this.userMovieLikeMapper = userMovieLikeMapper;
        this.userMovieFavoriteMapper = userMovieFavoriteMapper;
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
            "ar", // Arabic
            "th"  // Thai (Thailand)
    );

    @Value("${movies-api-key}")
    private String movieApiKey;

    public void addMoviesFromDbApi(String typeMovieApi) {
        List<TheMovieDbPaginationModel> result = getPaginatedMoviesFromApi(typeMovieApi);
        List<MovieDTO> listMovieDTO = new ArrayList<>();

        int countMovieAdded = mapAndPersistMoviesFromApiResponse(typeMovieApi, result, listMovieDTO);

        if (countMovieAdded > 0) {
            addMovieDetails(typeMovieApi);
        }
        logger.info("Total {} added {}", typeMovieApi, countMovieAdded);
    }

    public int addSingleMovieFromDbApi(String typeMovie, int id) {
        TheMovieDbModel result = getSingleMovieFromApi(typeMovie, id);
        List<MovieDTO> listMovieDTO = new ArrayList<>();

        int countMovieAdded = mapAndPersistMoviesFromApiResponseSingleMovie(typeMovie, result, listMovieDTO);

        if (countMovieAdded > 0) {
            addMovieDetail(typeMovie, result.getId());
        }
        logger.info("Total {} added {}", typeMovie, countMovieAdded);
        return countMovieAdded;
    }

    private int mapAndPersistMoviesFromApiResponseSingleMovie(String typeMovieApi, TheMovieDbModel theMovieDbModel, List<MovieDTO> listMovieDTO) {
        setMovieModel(theMovieDbModel, listMovieDTO, typeMovieApi);

        int countMovieAdded = 0;
        countMovieAdded = getCountMovieAdded(listMovieDTO, countMovieAdded);
        return countMovieAdded;
    }

    private int mapAndPersistMoviesFromApiResponse(String typeMovieApi, List<TheMovieDbPaginationModel> result, List<MovieDTO> listMovieDTO) {
        for (TheMovieDbPaginationModel theMovieDbPaginationModel : result) {
            for (TheMovieDbModel theMovieDbModel : theMovieDbPaginationModel.getResults()) {
                setMovieModel(theMovieDbModel, listMovieDTO, typeMovieApi);
            }
        }
        int countMovieAdded = 0;
        countMovieAdded = getCountMovieAdded(listMovieDTO, countMovieAdded);
        return countMovieAdded;
    }

    private int getCountMovieAdded(List<MovieDTO> listMovieDTO, int countMovieAdded) {
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
        return countMovieAdded;
    }

    public void addMovieDetails(String typeMovieApi) {

        List<MovieEntity> allMovie;
        if (typeMovieApi.equals("movie")) {
            allMovie = movieRepository.findAllByTypeMovie(TypeMovie.MOVIE);
        } else {
            allMovie = movieRepository.findAllByTypeMovie(TypeMovie.TV_SHOW);
        }

        processMovieDetails(typeMovieApi, allMovie);
    }

    public void addMovieDetail(String typeMovieApi, int idTheMovieDb) {
        Optional<MovieEntity> movie = movieRepository.findByIdTheMovieDb(idTheMovieDb);
        if (movie.isPresent()) {
            processSingleMovieDetails(typeMovieApi, movie.get());
        } else {
            logger.info("Movie to add not found");
        }
    }

    private void processMovieDetails(String typeMovieApi, List<MovieEntity> allMovie) {
        for (MovieEntity movieEntity : allMovie) {
            logger.info("Processing {} title {}", typeMovieApi, movieEntity.getTitle());
            fetchAndStoreMovieDetails(typeMovieApi, movieEntity);
        }
    }

    private void processSingleMovieDetails(String typeMovieApi, MovieEntity movie) {
        logger.info("Processing {} title {}", typeMovieApi, movie.getTitle());
        fetchAndStoreMovieDetails(typeMovieApi, movie);
    }

    private void fetchAndStoreMovieDetails(String typeMovieApi, MovieEntity movieEntity) {
        List<TheMovieDbDetailModel> listTheMovieDbDetailModel = getDetailMovies(typeMovieApi, movieEntity.getIdTheMovieDb());
        addDetailMovieToDB(typeMovieApi, movieEntity, listTheMovieDbDetailModel);
    }

    private void addDetailMovieToDB(String typeMovieApi, MovieEntity movieEntity, List<TheMovieDbDetailModel> listTheMovieDbDetailModel) {
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

    private String getBaseUrlForSingleMovie(String typeMovieApi, int id) {
        return "https://api.themoviedb.org/3/" + typeMovieApi + "/" + id + "?api_key=" + movieApiKey;
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

    public TheMovieDbModel getSingleMovieFromApi(String typeMovie, int id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(setHeaderMoviesFromDbApi());
        TheMovieDbModel result = null;

        String url = getBaseUrlForSingleMovie(typeMovie, id);

        ResponseEntity<TheMovieDbModel> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                });
        if (response.getBody() != null &&
                (response.getBody().getBackdrop_path() != null && !response.getBody().getBackdrop_path().isEmpty()) &&
                (response.getBody().getPoster_path() != null && !response.getBody().getPoster_path().isEmpty()) &&
                (response.getBody().getOverview() != null && !response.getBody().getOverview().isEmpty())) {
            result = response.getBody();
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

    @Cacheable("allContent")
    public ResponsePagination<MovieDTO> getAllMovie(Pageable pageable, TypeMovie typeMovie, String title, List<String> genres, List<String> languages) {
        List<MovieDTO> movieDTO;

        Page<MovieEntity> movieEntityPage = movieRepository.findFiltered(typeMovie, title, genres, languages, pageable);

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

    public Response<UserMovieLikeDTO> likeMovie(UserMovieLikeDTO userMovieLikeDTO) {
        Optional<MovieEntity> existingMovie = this.movieRepository.findById(userMovieLikeDTO.getMovieId());
        Optional<UserEntity> existingUser = this.userRepository.findById(Utils.getCurrentUserEntity().getId());

        if (existingMovie.isPresent()) {
            try {
                Optional<UserMovieLikeEntity> existingUserMovieLike = this.userMovieLikeRepository.findById(Utils.buildUserMovieKey(existingMovie.get().getId()));
                UserMovieLikeEntity userMovieLikeEntity = null;
                UserMovieLikeEntity savedUserMovieLikeEntity = null;

                if (existingUserMovieLike.isPresent()) {
                    userMovieLikeEntity = existingUserMovieLike.get();
                    if (userMovieLikeEntity.getLiked() != null && userMovieLikeEntity.getLiked().equals(userMovieLikeDTO.getLiked())) {
                        userMovieLikeEntity.setLiked(null);
                    } else {
                        userMovieLikeEntity.setLiked(userMovieLikeDTO.getLiked());
                    }
                    savedUserMovieLikeEntity = this.userMovieLikeRepository.save(userMovieLikeEntity);
                } else {
                    userMovieLikeEntity = new UserMovieLikeEntity();
                    userMovieLikeEntity.setUserMovieKey(Utils.buildUserMovieKey(existingMovie.get().getId()));
                    userMovieLikeEntity.setUser(existingUser.get());
                    userMovieLikeEntity.setMovie(existingMovie.get());
                    userMovieLikeEntity.setLiked(userMovieLikeDTO.getLiked());
                    savedUserMovieLikeEntity = this.userMovieLikeRepository.save(userMovieLikeEntity);
                }

                UserMovieLikeDTO savedUserMovieLikeDTO = this.userMovieLikeMapper.convertToModel(savedUserMovieLikeEntity);
                logger.info("Add like to the movie ID {} and user ID {}", savedUserMovieLikeDTO.getMovieId(), Utils.getCurrentUserEntity().getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Successfully added like", null, savedUserMovieLikeDTO);

            } catch (Exception e) {
                logger.error("Error while adding like to the movie ID {}", userMovieLikeDTO.getMovieId(), e);
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add like", null, null);
            }
        }
        logger.error("Movie ID {} not found", userMovieLikeDTO.getMovieId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Movie to add like not found", null, null);
    }

    public Response<UserMovieFavoriteDTO> favoriteMovie(UserMovieFavoriteDTO userMovieFavoriteDTO) {
        Optional<MovieEntity> existMovie = this.movieRepository.findById(userMovieFavoriteDTO.getMovieId());
        Optional<UserEntity> existUser = this.userRepository.findById(Utils.getCurrentUserEntity().getId());

        if (existMovie.isPresent()) {
            try {
                Optional<UserMovieFavoriteEntity> existUserMovieFavoriteEntity = this.userMovieFavoriteRepository.findById(Utils.buildUserMovieKey(userMovieFavoriteDTO.getMovieId()));
                UserMovieFavoriteEntity userMovieFavoriteEntity = null;
                UserMovieFavoriteEntity savedUserMovieFavoriteEntity = null;

                if (existUserMovieFavoriteEntity.isPresent()) {
                    userMovieFavoriteEntity = existUserMovieFavoriteEntity.get();
                    userMovieFavoriteEntity.setFavorite(userMovieFavoriteDTO.isFavorite());
                    savedUserMovieFavoriteEntity = this.userMovieFavoriteRepository.save(userMovieFavoriteEntity);
                } else {
                    userMovieFavoriteEntity = new UserMovieFavoriteEntity();
                    userMovieFavoriteEntity.setUserMovieKey(Utils.buildUserMovieKey(userMovieFavoriteDTO.getMovieId()));
                    userMovieFavoriteEntity.setUser(existUser.get());
                    userMovieFavoriteEntity.setMovie(existMovie.get());
                    userMovieFavoriteEntity.setFavorite(userMovieFavoriteDTO.isFavorite());
                    savedUserMovieFavoriteEntity = this.userMovieFavoriteRepository.save(userMovieFavoriteEntity);
                }
                UserMovieFavoriteDTO savedUserMovieFavoriteDTO = this.userMovieFavoriteMapper.convertToModel(savedUserMovieFavoriteEntity);
                logger.info("Add to favorite the movie ID {} and user ID {}", savedUserMovieFavoriteDTO.getMovieId(), Utils.getCurrentUserEntity().getId());
                return Utils.createResponse(HttpStatus.OK.value(), "Successfully added favorite", null, savedUserMovieFavoriteDTO);
            } catch (Exception e) {
                logger.error("Error while adding to favorite the movie ID {}", userMovieFavoriteDTO.getMovieId());
                return Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add favorite", null, null);
            }
        }
        logger.error("Movie ID {} not found", userMovieFavoriteDTO.getMovieId());
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Movie to add favorite not found", null, null);
    }

    public Response<UserMovieFavoriteDTO> isMovieIdFavorite(Long id) {
        UserMovieFavoriteDTO returnNotFound = new UserMovieFavoriteDTO();
        returnNotFound.setFavorite(false);
        if (Utils.buildUserMovieKey(id) == null) {
            logger.info("Error Get info favorite for movie ID {} does not exist", id);
            return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Error info movie favorite", null, returnNotFound);
        }

        Optional<MovieEntity> existMovie = this.movieRepository.findById(id);

        if (existMovie.isPresent()) {
            Optional<UserMovieFavoriteEntity> existUserMovieFavoriteEntity = this.userMovieFavoriteRepository.findById(Utils.buildUserMovieKey(id));

            if (existUserMovieFavoriteEntity.isPresent()) {
                UserMovieFavoriteDTO userMovieLikeDTO = userMovieFavoriteMapper.convertToModel(existUserMovieFavoriteEntity.get());
                logger.info("Get info favorite for movie ID {}", id);
                return Utils.createResponse(HttpStatus.OK.value(), "Successfully returned info favorite", null, userMovieLikeDTO);
            }
        }
        logger.error("Movie ID {} not found", id);
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Movie id not found", null, null);
    }

    public Response<UserMovieLikeDTO> isMovieIdLiked(Long id) {
        UserMovieLikeDTO returnNotFound = new UserMovieLikeDTO();
        returnNotFound.setLiked(null);
        if (Utils.buildUserMovieKey(id) == null) {
            logger.info("Error Get info liked for movie ID {} does not exist", id);
            return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Error info movie favorite", null, returnNotFound);
        }
        Optional<MovieEntity> existingMovie = this.movieRepository.findById(id);

        if (existingMovie.isPresent()) {
            Optional<UserMovieLikeEntity> existUserMovieLikeEntity = this.userMovieLikeRepository.findById(Utils.buildUserMovieKey(id));

            if (existUserMovieLikeEntity.isPresent()) {
                UserMovieLikeDTO userMovieLikeDTO = userMovieLikeMapper.convertToModel(existUserMovieLikeEntity.get());
                logger.info("Get info like for movie ID {}", id);
                return Utils.createResponse(HttpStatus.OK.value(), "Successfully returned info like movie", null, userMovieLikeDTO);
            }
        }
        logger.error("Movie ID {} not found", id);
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "Movie id not found", null, null);
    }
}