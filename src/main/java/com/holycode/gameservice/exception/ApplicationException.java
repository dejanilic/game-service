package com.holycode.gameservice.exception;


import static com.holycode.gameservice.exception.InternalException.*;
import static java.lang.String.format;

public class ApplicationException extends RuntimeException {

    final ApplicationExceptionCode code;

    public ApplicationException(ApplicationExceptionCode code, String pattern, Object... args) {
        super(format("[%s] ", code) + format(pattern, args), extractCause(args));
        this.code = code;
    }

}
