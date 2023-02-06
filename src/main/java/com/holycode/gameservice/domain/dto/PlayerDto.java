package com.holycode.gameservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa koja predstavlja Player klasu iz player-service, a koju dohvatamo preko RestTemplate-a.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private Long id;

    private String name;

    private Long gameId;

}
