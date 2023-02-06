package com.holycode.gameservice.domain.dto;

import com.holycode.gameservice.domain.Game;
import lombok.*;

/**
 * Klasa koja predstavlja odgovor za upit o informacijama igre.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoResponse {

    @NonNull
    private GameResponseStatus status;

    private Game game;

}
