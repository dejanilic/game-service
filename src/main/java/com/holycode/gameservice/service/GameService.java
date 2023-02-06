package com.holycode.gameservice.service;

import com.holycode.gameservice.domain.Game;
import com.holycode.gameservice.domain.dto.NewGameRequest;

import java.util.List;

public interface GameService {

    /**
     * Kreira i upisuje novu igru u bazu.
     * Nakon kreiranja, proverava da li trenutni igrač postoji u player servisu.
     * Ukoliko ne postoji, kreira ga na osnovu dto-a.
     *
     * @param gameRequest - zahtev za novu igru.
     */
    Game createGame(NewGameRequest gameRequest);

    /**
     * Vraća info o igri na osnovu id-a.
     *
     * @param gameId - ID igre
     */
    Game getInfo(Long gameId);

    /**
     * Ažurira status u 'FINISHED'.
     *
     * @param gameId - ID igre
     */
    void updateStatus(Long gameId);

    /**
     * Briše igru iz baze.
     *
     * @param gameId - ID igre
     */
    void delete(Long gameId);

    /**
     * Vraća listu igara na osnovu imena igre, statusa i imena igrača
     *
     * @param gameName - ime igre
     * @param status - status u kome je igra
     * @param playerName - ime igrača
     */
    List<Game> getGameByNameAndStatusAndPlayerName(String gameName, String status, String playerName);
}
