package com.zakaria.streamingPlatform.movie;

import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.models.MovieModel;
import com.zakaria.streamingPlatform.models.TheMovieDbModel;
import com.zakaria.streamingPlatform.models.TheMovieDbPaginationModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
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

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

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
        List<MovieModel> listMovieModel = new ArrayList<>();

        for (TheMovieDbPaginationModel theMovieDbPaginationModel : result) {
            for (TheMovieDbModel theMovieDbModel : theMovieDbPaginationModel.getResults()) {
                if (checkLanguage(theMovieDbModel.getOriginal_language())) {
                    setMovieModel(theMovieDbModel, listMovieModel, typeMovieApi);
                }
            }
        }
        for (MovieModel singleMovieModel : listMovieModel) {
            if (checkMovieModelBeforeSave(singleMovieModel)) {
                Optional<MovieEntity> movieEntity = movieRepository.findByTitle(singleMovieModel.getTitle());
                if (movieEntity.isEmpty()) {
                    movieRepository.save(movieMapper.convertToEntity(singleMovieModel));
                }
            }
        }
    }

    private boolean checkMovieModelBeforeSave(MovieModel singleMovieModel) {
        return singleMovieModel != null &&
                (singleMovieModel.getBackdropPath() != null && !singleMovieModel.getBackdropPath().isEmpty()) &&
                (singleMovieModel.getPosterPath() != null && !singleMovieModel.getPosterPath().isEmpty()) &&
                (singleMovieModel.getDescription() != null && !singleMovieModel.getDescription().isEmpty());
    }

    private static void setMovieModel(TheMovieDbModel theMovieDbModel, List<MovieModel> listMovieModel, String typeMovieApi) {
        MovieModel movieModel = new MovieModel();

        if (typeMovieApi.equals("movie")) {
            movieModel.setTypeMovie(TypeMovie.MOVIE);
            movieModel.setTitle(theMovieDbModel.getTitle());
            movieModel.setReleaseDate(theMovieDbModel.getRelease_date());
        } else {
            movieModel.setTypeMovie(TypeMovie.TV_SHOW);
            movieModel.setTitle(theMovieDbModel.getName());
            movieModel.setReleaseDate(theMovieDbModel.getFirst_air_date());
        }
        movieModel.setLanguage(theMovieDbModel.getOriginal_language());
        movieModel.setDescription(theMovieDbModel.getOverview());
        movieModel.setPosterPath(theMovieDbModel.getPoster_path());
        movieModel.setBackdropPath(theMovieDbModel.getBackdrop_path());
        movieModel.setPopularity(theMovieDbModel.getPopularity());
        movieModel.setActive(true);
        movieModel.setDateCreated(LocalDate.now());

        listMovieModel.add(movieModel);
    }

    private String getBaseUrl(String typeMovieApi) {
        return "https://api.themoviedb.org/3/discover/" + typeMovieApi + "?api_key=" + movieApiKey;
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
            for (String s : LANGUAGES_ALLOWED) {
                String paginatedUrl = getBaseUrl(typeMovieApi) + "&page=" + i + "&with_original_language=" + s;

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

    private boolean checkLanguage(String originalLanguage) {
        return LANGUAGES_ALLOWED.contains(originalLanguage);
    }

    @Cacheable("allMovie")
    public ResponsePagination<MovieModel> getAllMovie(Pageable pageable) {
        ResponsePagination<MovieModel> response = new ResponsePagination<>();
        List<MovieModel> movieModel = new ArrayList<>();

        Page<MovieEntity> movieEntityPage = movieRepository.findAllByTypeMovie(TypeMovie.MOVIE, pageable);

        if (!movieEntityPage.isEmpty()) {
            movieModel = movieEntityPage.get()
                    .map(movieMapper::convertToModel)
                    .toList();
            return Utils.createResponsePagination(HttpStatus.OK.value(), "Get all movies successfully", movieModel, pageable.getPageNumber(),
                    pageable.getPageSize(), movieEntityPage.getTotalPages(), movieEntityPage.getTotalElements(), movieEntityPage.isLast(), null);
        }
        return Utils.createResponsePagination(HttpStatus.NO_CONTENT.value(), "No movie available", "No movie available");
    }

    @Cacheable("getTrendingMovie")
    public Response<List<MovieModel>> getTrendingMovie() {
        Response<MovieModel> response = new Response<>();
        List<MovieModel> movieModel = new ArrayList<>();

        Pageable top24 = PageRequest.of(0, 10);
        List<MovieEntity> movieEntity = movieRepository.trendingByTypeMovie(TypeMovie.MOVIE, top24);

        if (!movieEntity.isEmpty()) {
            movieModel = movieEntity.stream().map(movieMapper::convertToModel).collect(Collectors.toList());
            return Utils.createResponse(HttpStatus.OK.value(), "trending movie returned successfully", null, movieModel);
        }
        return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "trending movies no data found", List.of("trending movies no data found"), null);
    }


    @Cacheable("getTrendingTvShow")
    public Response<List<MovieModel>> getTrendingTvShow() {
        Response<MovieModel> response = new Response<>();
        List<MovieModel> movieModel = new ArrayList<>();

        Pageable top24 = PageRequest.of(0, 10);
        List<MovieEntity> movieEntity = movieRepository.trendingByTypeMovie(TypeMovie.TV_SHOW, top24);

        if (!movieEntity.isEmpty()) {
            movieModel = movieEntity.stream().map(movieMapper::convertToModel).collect(Collectors.toList());
            return Utils.createResponse(HttpStatus.OK.value(), "trending tv shows returned successfully", null, movieModel);
        }
        return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "trending tv shows no data found", List.of("trending tv shows no data found"), null);
    }
}