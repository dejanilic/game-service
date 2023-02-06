package com.holycode.gameservice.exception;

import static java.lang.String.format;

public class InternalException extends RuntimeException {

    public InternalException(String pattern, Object... args) {
        super(format(pattern, args), extractCause(args));
    }

    static Throwable extractCause(Object... args) {
        if (args == null || args.length == 0) {
            return null;
        }
        if (args[args.length - 1] instanceof Throwable) {
            return (Throwable) args[args.length - 1];
        }
        return null;
    }
}
