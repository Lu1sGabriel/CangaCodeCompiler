package model;

import tokens.IToken;

public record SymbolEntryModel(int index, int line, String lexemeOriginal, int lexemeNaoTruncado, int lexemeTruncado, IToken token) {

    @Override
    public String toString() {
        return String.format("Índice: %s, Token: %s, Original: '%s', Truncado: '%s', Não Truncado: '%s'",
                index, token.getName().toLowerCase(), lexemeOriginal, lexemeNaoTruncado, lexemeTruncado);
    }

}