package com.zakaria.streamingPlatform.movie;

import com.zakaria.streamingPlatform.entities.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<SeasonEntity, Long> {
    Optional<SeasonEntity> findByIdTheMovieDb(int idTheMovieDb);
}
