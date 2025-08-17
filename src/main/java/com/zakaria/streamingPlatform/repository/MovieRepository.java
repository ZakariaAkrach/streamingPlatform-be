package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByTitle(String title);

    Optional<MovieEntity> findByIdTheMovieDb(int idTheMovieDb);



    @Query("SELECT DISTINCT m FROM MovieEntity m " +
            "LEFT JOIN m.genres g " +
            "WHERE (:typeMovie IS NULL OR m.typeMovie = :typeMovie) " +
            "AND (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:genres IS NULL OR g.name IN :genres) " +
            "AND (:languages IS NULL OR LOWER(m.language) IN :languages) ")
    Page<MovieEntity> findFiltered(
            @Param("typeMovie") TypeMovie typeMovie,
            @Param("title") String title,
            @Param("genres") List<String> genres,
            @Param("languages") List<String> languages,
            Pageable pageable);

    Page<MovieEntity> findAllByTypeMovieAndTitleContainingIgnoreCase(TypeMovie typeMovie, String title, Pageable pageable);




    List<MovieEntity> findAllByTypeMovie(TypeMovie typeMovie);

    @Query("SELECT m FROM MovieEntity m WHERE m.typeMovie = :typeMovie ORDER BY m.popularity DESC ")
    List<MovieEntity> trendingByTypeMovie(@Param("typeMovie") TypeMovie typeMovie, Pageable pageable);


    //Content-Manager
    Page<MovieEntity> findByTypeMovie(@Param("typeMovie") TypeMovie typeMovie, Pageable pageable);

    Page<MovieEntity> findByTypeMovieAndTitleContainingIgnoreCase(@Param("typeMovie") TypeMovie typeMovie, @Param("title") String title, Pageable pageable);

    @Query("SELECT m.id, m.title, COUNT(umf) as favoriteCount " +
            "FROM MovieEntity m " +
            "LEFT JOIN UserMovieFavoriteEntity umf ON umf.userMovieKey.movieId = m.id " +
            "WHERE m.typeMovie = :typeMovie AND umf.favorite = true " +
            "GROUP BY m " +
            "ORDER BY favoriteCount DESC")
    List<Object[]> topFiveFavoriteContent(@Param("typeMovie") TypeMovie typeMovie, Pageable pageable);
}
