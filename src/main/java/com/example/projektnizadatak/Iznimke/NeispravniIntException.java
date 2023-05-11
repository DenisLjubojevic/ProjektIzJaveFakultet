package com.example.projektnizadatak.Iznimke;

public class NeispravniIntException extends Exception{
    public NeispravniIntException() {
    }

    public NeispravniIntException(String message) {
        super(message);
    }

    public NeispravniIntException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeispravniIntException(Throwable cause) {
        super(cause);
    }

    public NeispravniIntException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
