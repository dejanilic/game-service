package com.holycode.gameservice.domain.dto;

import com.holycode.gameservice.domain.Game;
import lombok.*;

/**
 * Ogovor za kreiranje nove igre.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewGameResponse {

    @NonNull
    private GameResponseStatus status;

    @NonNull
    private String message;

    private Game game;

}
