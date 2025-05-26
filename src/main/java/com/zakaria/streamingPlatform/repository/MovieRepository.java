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

    Page<MovieEntity> findAllByTypeMovie(TypeMovie typeMovie, Pageable pageable);
    List<MovieEntity> findAllByTypeMovie(TypeMovie typeMovie);

    @Query("SELECT m FROM MovieEntity m WHERE m.typeMovie = :typeMovie ORDER BY m.popularity DESC ")
    List<MovieEntity> trendingByTypeMovie(@Param("typeMovie") TypeMovie typeMovie, Pageable pageable);
}
