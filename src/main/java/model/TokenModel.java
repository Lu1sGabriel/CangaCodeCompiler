package model;

import tokens.IToken;

public record TokenModel(IToken token, String lexeme) {

    @Override
    public String toString() {
        return String.format("Token: %s (%s), Lexeme: '%s'", token.getName(), token.getCode(), lexeme);
    }
}