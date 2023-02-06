package com.holycode.gameservice.service.impl;

import com.holycode.gameservice.domain.Game;
import com.holycode.gameservice.domain.Status;
import com.holycode.gameservice.domain.dto.NewGameRequest;
import com.holycode.gameservice.domain.dto.PlayerDto;
import com.holycode.gameservice.repository.GameRepository;
import com.holycode.gameservice.service.GameException;
import com.holycode.gameservice.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.holycode.gameservice.service.GameException.PlayerExceptionCode.GAME_INFO_EXCEPTION;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;
    private RestTemplate restTemplate;

    public GameServiceImpl(GameRepository gameRepository, RestTemplate restTemplate) {
        this.gameRepository = gameRepository;
        this.restTemplate = restTemplate;
    }

    // Improvement: ako igrač postoji, setovati mu game id. (za sada setujem samo novo-kreiranom igraču game id)
    @Override
    public Game createGame(NewGameRequest gameRequest) {
        log.info("Pozvana createGame(NewGameRequest gameRequest) metoda.");

        Game game = null;
        try {
            game = Game.builder()
                    .name(gameRequest.getGameName())
                    .status(Status.NEW)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            game = gameRepository.save(game);
            checkForPlayer(gameRequest, game);
        } catch (Exception e) {
            log.error("Greška prilikom snimanja igre.");
        }

        log.info("Metoda createGame(NewGameRequest gameRequest) uspešno izvršena.");
        return game;
    }

    @Override
    public Game getInfo(Long gameId) {
        log.info("Pozvana getInfo(Long gameId) metoda.");

        Game game = null;
        try {
            game = gameRepository.findById(gameId).get();
        } catch (Exception e) {
            log.error("Greška prilikom dohvatanja igre.");

            // Primer bacanja custom error-a.
            throw new GameException(GAME_INFO_EXCEPTION, "Can't find game with id: %s", gameId);
        }

        log.info("Metoda getInfo(Long gameId) uspešno izvršena.");
        return game;
    }

    @Override
    public void updateStatus(Long gameId) {
        log.info("Pozvana updateStatus(Long gameId) metoda.");

        gameRepository.findById(gameId)
                .map(game -> {
                    game.setStatus(Status.FINISHED);
                    game.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    return gameRepository.save(game);
                });
    }

    // Brisem igru skroz iz baze, iako vidim da postoji DROPPED state..
    // Improvement: ref. integritet, tj. prvo brisanje igraču polje game id, pa tek onda brisanje samog game objekta.
    @Override
    public void delete(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    @Override
    public List<Game> getGameByNameAndStatusAndPlayerName(String gameName, String status, String playerName) {

        // Improvement: nije bas najbolje resenje dohvatiti sve igrace ali za ovaj mali broj igraca je okej
        List<PlayerDto> players = new ArrayList<>();
        try {
            // Dohvati sve igrace
            ResponseEntity<List<PlayerDto>> rateResponse =
                    restTemplate.exchange("http://localhost:8080/all",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<PlayerDto>>() {
                            });
            players = rateResponse.getBody();
        } catch (Exception e) {
            log.error("Greška prilikom komunikacije sa player servisom. " + e);
        }

        List<Game> gamesFilteredByPlayerIds = new ArrayList<>();

        players.stream().filter(player -> player.getName().equals(playerName)).forEach(player -> {
            gameRepository.getAllById(player.getGameId()).forEach(game -> {
                gamesFilteredByPlayerIds.add(game);
            });
        });

        return gamesFilteredByPlayerIds.stream()
                .filter(game -> game.getName().equals(gameName) && game.getStatus().name().equals(status))
                .collect(Collectors.toList());
    }

    private void checkForPlayer(NewGameRequest gameRequest, Game game) {
        try {
            ResponseEntity<PlayerDto> responseEntity = restTemplate
                    .getForEntity(getPlayerUrlTemplate(gameRequest.getPlayerId()),PlayerDto.class);

            if (responseEntity.getBody().getId() == null) {
                PlayerDto request = new PlayerDto(gameRequest.getPlayerId(), gameRequest.getPlayerName(), game.getId());
                // Ovde mi puca org.springframework.web.client.RestClientException, vrv. zbog konverzije u application/json format,
                // ali se i dalje izvršava ova metoda i kreira se novi igrač u bazi.
                // Improvement: izdvojiti ove hardkodovane url-ove u neki property fajl
                responseEntity = restTemplate.postForEntity("http://localhost:8080/register", request, PlayerDto.class);
            }
        } catch (Exception e) {
            log.error("Greška prilikom komunikacije sa player servisom. " + e);
        }
    }

    private String getPlayerUrlTemplate(Long playerId) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:8080/player")
                .queryParam("playerId", playerId)
                .encode()
                .toUriString();
    }
}
