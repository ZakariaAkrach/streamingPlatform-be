package com.zakaria.streamingPlatform.movie;

import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.models.MovieModel;
import com.zakaria.streamingPlatform.models.TheMovieDbModel;
import com.zakaria.streamingPlatform.models.TheMovieDbPaginationModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public void addMoviesFromDbApi() {
        List<TheMovieDbPaginationModel> result = getPaginatedMoviesFromApi();
        List<MovieModel> listMovieModel = new ArrayList<>();

        for (TheMovieDbPaginationModel theMovieDbPaginationModel : result) {
            for (TheMovieDbModel theMovieDbModel : theMovieDbPaginationModel.getResults()) {
                if (checkLanguage(theMovieDbModel.getOriginal_language())) {
                    setMovieModel(theMovieDbModel, listMovieModel);
                }
            }
        }
        for (MovieModel singleMovieModel : listMovieModel) {
            if (singleMovieModel != null && singleMovieModel.getBackdropPath() != null && singleMovieModel.getPosterPath() != null) {
                Optional<MovieEntity> movieEntity = movieRepository.findByTitle(singleMovieModel.getTitle());
                if (movieEntity.isEmpty()) {
                    movieRepository.save(movieMapper.convertToEntity(singleMovieModel));
                }
            }
        }
    }

    private static void setMovieModel(TheMovieDbModel theMovieDbModel, List<MovieModel> listMovieModel) {
        MovieModel movieModel = new MovieModel();

        movieModel.setTypeMovie(TypeMovie.MOVIE);
        movieModel.setLanguage(theMovieDbModel.getOriginal_language());
        movieModel.setTitle(theMovieDbModel.getTitle());
        movieModel.setDescription(theMovieDbModel.getOverview());
        movieModel.setPosterPath(theMovieDbModel.getPoster_path());
        movieModel.setBackdropPath(theMovieDbModel.getBackdrop_path());
        movieModel.setReleaseDate(theMovieDbModel.getRelease_date());
        movieModel.setActive(true);
        movieModel.setDateCreated(LocalDate.now());

        listMovieModel.add(movieModel);
    }

    private String getBaseUrl() {
        return "https://api.themoviedb.org/3/discover/movie?api_key=" + movieApiKey;
    }

    public HttpHeaders setHeaderMoviesFromDbApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    public List<TheMovieDbPaginationModel> getPaginatedMoviesFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(setHeaderMoviesFromDbApi());
        List<TheMovieDbPaginationModel> result = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < LANGUAGES_ALLOWED.size(); j++) {
                String paginatedUrl = getBaseUrl() + "&page=" + i + "&with_original_language=" + LANGUAGES_ALLOWED.get(j);

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

}
