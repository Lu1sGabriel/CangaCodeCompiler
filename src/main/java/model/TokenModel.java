package model;

import tokens.IToken;

public record TokenModel(
        IToken token,
        String lexeme,
        int line,
        int symbolTableIndex
) {

    // Construtor para tokens que não precisam estar na tabela de símbolos
    public TokenModel(IToken token, String lexeme, int line) {
        this(token, lexeme, line, -1);
    }

    /**
     * Verifica se este token está na tabela de símbolos
     */
    public boolean isInSymbolTable() {
        return symbolTableIndex > 0;
    }

    @Override
    public String toString() {
        if (isInSymbolTable()) {
            return String.format("Token{%s, '%s', linha: %d, índice: %d}",
                    token.getName(), lexeme, line, symbolTableIndex);
        } else {
            return String.format("Token{%s, '%s', linha: %d}",
                    token.getName(), lexeme, line);
        }
    }

}