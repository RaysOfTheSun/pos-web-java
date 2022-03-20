package com.raysofthesun.poswebjava.core.exceptions;

public class MarketNotSupportedException extends RuntimeException {
    public MarketNotSupportedException() {
        super("The requested operation is not supported by the provided market");
    }
}
