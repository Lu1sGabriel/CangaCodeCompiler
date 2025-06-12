package model;

import tokens.IToken;

public record TokenModel(IToken token, String lexeme, int line) {

    @Override
    public String toString() {
        return String.format("Token: %s (%s), Lexeme: '%s', Linha: %s", token.getName(), token.getCode(), lexeme, line);
    }

}