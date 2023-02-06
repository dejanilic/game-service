package com.holycode.gameservice.service;

import com.holycode.gameservice.exception.ApplicationException;
import com.holycode.gameservice.exception.ApplicationExceptionCode;

public class GameException extends ApplicationException {

    public GameException(PlayerExceptionCode code, String pattern, Object... args) {
        super(code, pattern, args);
    }

    public enum PlayerExceptionCode implements ApplicationExceptionCode {
        GAME_INFO_EXCEPTION
    }
}
