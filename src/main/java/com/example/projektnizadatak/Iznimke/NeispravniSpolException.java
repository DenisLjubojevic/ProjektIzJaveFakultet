package com.example.projektnizadatak.Iznimke;

public class NeispravniSpolException extends Exception{
    public NeispravniSpolException() {
    }

    public NeispravniSpolException(String message) {
        super(message);
    }

    public NeispravniSpolException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeispravniSpolException(Throwable cause) {
        super(cause);
    }

    public NeispravniSpolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
