package com.holycode.gameservice.repository;

import com.holycode.gameservice.domain.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> getAllById(@Param("id") Long id);

}
