package com.rugbyInfo.rugbyInfoApi.Service;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
