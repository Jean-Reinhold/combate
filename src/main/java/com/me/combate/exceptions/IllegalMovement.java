package com.me.combate.exceptions;

public class IllegalMovement extends RuntimeException {
    public IllegalMovement(String message) {
        super(message);
    }
}
