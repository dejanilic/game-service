package com.holycode.gameservice;

import com.holycode.gameservice.domain.Game;
import com.holycode.gameservice.domain.Status;
import com.holycode.gameservice.repository.GameRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@SpringBootApplication
public class GameServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameServiceApplication.class, args);
    }

}

@Component
class DataLoader implements ApplicationRunner {

    private GameRepository gameRepository;

    public DataLoader(GameRepository playerRepository) {
        this.gameRepository = playerRepository;
    }

    // Ovako sam punio bazu test podacima, moglo je i preko data.sql fajla
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        gameRepository.save(new Game(1L, "Football", Status.NEW, now, now));
        gameRepository.save(new Game(2L, "Football", Status.NEW, now, now));
        gameRepository.save(new Game(3L, "Basketball", Status.FINISHED, now, now));
        gameRepository.save(new Game(4L, "Basketball", Status.FINISHED, now, now));
        gameRepository.save(new Game(5L, "Basketball", Status.DROPPED, now, now));
    }
}
