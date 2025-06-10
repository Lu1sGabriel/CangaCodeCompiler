package service;

import model.TokenModel;
import tokens.IToken;
import tokens.TokenIdentifiers;
import tokens.TokenReservedSymbols;
import shared.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class LexerService {

    private final String source;
    private int pos = 0;
    private final int length;

    public LexerService(String source) {
        this.source = source;
        this.length = source.length();
    }

    private char peek() {
        if (pos >= length) return '\0';
        return source.charAt(pos);
    }

    private char advance() {
        if (pos >= length) return '\0';
        return source.charAt(pos++);
    }

    private boolean isAtEnd() {
        return pos >= length;
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    private void skipComment() {
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }

    public List<TokenModel> tokenize() {
        List<TokenModel> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            skipWhitespace();

            if (isAtEnd()) break;

            if (peek() == '/' && (pos + 1 < length) && source.charAt(pos + 1) == '/') {
                pos += 2;
                skipComment();
                continue;
            }

            char c = peek();
            TokenModel token;

            if (Character.isLetter(c) || c == '_') {
                token = readWordOrSubmachine();
            } else if (Character.isDigit(c)) {
                token = readNumber();
            } else if (c == '"') {
                token = readString();
            } else if (c == '\'') {
                token = readChar();
            } else {
                token = readSymbolOrInvalid();
            }

            // Apenas adiciona o token se ele for válido (não nulo)
            if (token != null && token.token() != null) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    private TokenModel readWordOrSubmachine() {
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        String word = sb.toString();

        IToken token = TokenUtils.findTokenByWord(word);
        if (token != null) {
            return new TokenModel(token, word);
        }

        if (isValidIdentifier(word)) {
            return new TokenModel(TokenIdentifiers.VARIABLE, word);
        }

        // Retorna null para palavras inválidas
        return null;
    }

    private boolean isValidIdentifier(String word) {
        return word.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private TokenModel readNumber() {
        StringBuilder sb = new StringBuilder();
        boolean isReal = false;

        while (!isAtEnd() && Character.isDigit(peek())) {
            sb.append(advance());
        }

        if (!isAtEnd() && peek() == '.') {
            isReal = true;
            sb.append(advance());

            if (!isAtEnd() && Character.isDigit(peek())) {
                while (!isAtEnd() && Character.isDigit(peek())) {
                    sb.append(advance());
                }
            } else {
                // Número inválido (ponto sem dígitos após)
                return null;
            }
        }

        String numberStr = sb.toString();

        if (isReal) {
            return new TokenModel(TokenIdentifiers.REALCONST, numberStr);
        } else {
            return new TokenModel(TokenIdentifiers.INTCONST, numberStr);
        }
    }

    private TokenModel readString() {
        advance();

        StringBuilder sb = new StringBuilder();

        while (!isAtEnd() && peek() != '"') {
            char c = advance();

            if (c == '\n') {
                // String inválida (quebra de linha)
                return null;
            }

            sb.append(c);
        }

        if (isAtEnd()) {
            // String inválida (não fechada)
            return null;
        }

        advance();

        return new TokenModel(TokenIdentifiers.STRINGCONST, sb.toString());
    }

    private TokenModel readChar() {
        advance();

        if (isAtEnd()) return null;

        char c = advance();

        if (c == '\'' || c == '\n') {
            // char vazio ou quebra linha inválidos
            return null;
        }

        if (isAtEnd()) return null;

        char end = advance();
        if (end != '\'') {
            return null;
        }

        return new TokenModel(TokenIdentifiers.CHARCONST, String.valueOf(c));
    }

    private TokenModel readSymbolOrInvalid() {

        String twoChars = "";
        if (pos + 1 < length) {
            twoChars = "" + peek() + source.charAt(pos + 1);
        }

        IToken token = TokenReservedSymbols.getTokenByName(twoChars);
        if (token != null) {
            pos += 2;
            return new TokenModel(token, twoChars);
        }

        String oneChar = String.valueOf(advance());
        token = TokenReservedSymbols.getTokenByName(oneChar);
        if (token != null) {
            return new TokenModel(token, oneChar);
        }

        // Símbolo inválido - retorna null
        return null;
    }
}