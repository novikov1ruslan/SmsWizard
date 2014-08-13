package com.external.smswizard.model;

public class Token {
    public Token(String token) {
        this.token = token;
    }

    public String token;

    @Override
    public String toString() {
        return "[token=" + token + "]";
    }
}
