package com.holycode.gameservice.rest;

import com.holycode.gameservice.domain.Game;
import com.holycode.gameservice.domain.dto.GameResponseStatus;
import com.holycode.gameservice.domain.dto.NewGameRequest;
import com.holycode.gameservice.domain.dto.GameInfoResponse;
import com.holycode.gameservice.domain.dto.NewGameResponse;
import com.holycode.gameservice.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Ovde vraćam custom Response, u celoj ovoj klasi nisam hteo da koristim ResponseEntity kao u player servisu,
    // čisto da demonstriram još jedan način vraćanja odgovora.
    @PostMapping("/play")
    public NewGameResponse playGame(@RequestBody NewGameRequest gameRequest) {
        Game game = gameService.createGame(gameRequest);

        if (game == null) {
            return new NewGameResponse(GameResponseStatus.FAILED, "Game not created.", null);
        }

        return new NewGameResponse(GameResponseStatus.CREATED, "Game created.", game);
    }

    // Da li nam je dovoljno dobro dohvatanje igre preko @RequestParam?
    // Može da se uradi i preko @RequestHeader
    @GetMapping("/game")
    public GameInfoResponse getGameInfo(@RequestParam Long gameId) {

        // Ovde hendlujem error jer u servisu bacam custom exception, pa čisto da se u konzoli igraču prikaže
        // custom response, a ne generička http 500 greška
        Game game = null;
        try {
            game = gameService.getInfo(gameId);
        } catch (Exception e) {
            log.error("Greška prilikom dohvatanja igre.");
        }

        if (game == null) {
            return new GameInfoResponse(GameResponseStatus.FAILED, null);
        }

        return new GameInfoResponse(GameResponseStatus.OK, game);
    }

    // Jedan primer i sa @PathVariable
    @PutMapping("/play/{gameId}")
    public void updateGameStatus(@PathVariable Long gameId) {
        gameService.updateStatus(gameId);
    }

    @DeleteMapping("/game")
    public ResponseEntity<String> deletePlayer(@RequestParam Long gameId) {
        gameService.delete(gameId);
        return ResponseEntity.ok("Game deleted");
    }

    @GetMapping("/name/{gameName}/status/{status}/playername/{playerName}")
    public List<Game> getGameByNameAndStatusAndPlayerName(@PathVariable String gameName, @PathVariable String status, @PathVariable String playerName) {
        return gameService.getGameByNameAndStatusAndPlayerName(gameName, status, playerName);
    }

}