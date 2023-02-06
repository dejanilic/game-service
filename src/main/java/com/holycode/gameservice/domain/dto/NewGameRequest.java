package com.holycode.gameservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa koja predstavlja zahtev za kreiranje nove igre.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {

    private Long playerId;

    private String playerName;

    private String gameName;

}
