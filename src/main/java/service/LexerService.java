package service;

import model.TokenModel;
import shared.utils.TokenUtils;
import tokens.TokenIdentifiers;
import tokens.TokenReservedSymbols;
import tokens.TokenReservedWords;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class LexerService {

    private static final char NULL_CHAR = '\0';
    private static final char NEWLINE = '\n';
    private static final char QUOTE = '"';
    private static final char SINGLE_QUOTE = '\'';
    private static final char DOT = '.';
    private static final char UNDERSCORE = '_';
    private static final char FORWARD_SLASH = '/';

    private static final String COMMENT_START = "//";
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");

    private final SymbolTableService symbolTableService = new SymbolTableService();

    private final String source;
    private final int sourceLength;
    private int currentPosition = 0;
    private int currentLine = 1;

    public LexerService(String source) {
        this.source = Objects.requireNonNull(source, "Código fonte não pode ser nulo");
        this.sourceLength = source.length();
    }

    public SymbolTableService getSymbolTable() {
        return symbolTableService;
    }

    public List<TokenModel> tokenize() {
        var tokens = new ArrayList<TokenModel>();

        while (!isAtEnd()) {
            skipWhitespace();
            if (isAtEnd()) break;

            if (isCommentStart()) {
                skipComment();
                continue;
            }

            int startLine = currentLine;

            TokenModel firstToken = readNextToken();
            if (firstToken == null) continue;

            // Se for um tipo primitivo (string, integer, character, real)
            if (firstToken.token() instanceof TokenReservedWords reservedWord &&
                    isPrimitiveType(reservedWord)) {

                skipWhitespace();
                TokenModel nextToken = readNextToken();

                if (!isValidDeclarationPair(reservedWord, nextToken)) {
                    // Ignora linha inválida
                    skipLine(startLine);
                    continue;
                }

                // Adiciona os tokens com índice da tabela de símbolos
                TokenModel firstTokenWithIndex = addToSymbolTableAndCreateToken(firstToken);
                TokenModel nextTokenWithIndex = addToSymbolTableAndCreateToken(nextToken);

                tokens.add(firstTokenWithIndex);
                tokens.add(nextTokenWithIndex);

            } else {
                if (isValidToken(firstToken)) {
                    TokenModel tokenWithIndex = addToSymbolTableAndCreateToken(firstToken);
                    tokens.add(tokenWithIndex);
                }
            }
        }

        return List.copyOf(tokens);
    }

    /**
     * Adiciona o token à tabela de símbolos e retorna um novo TokenModel com o índice
     */
    private TokenModel addToSymbolTableAndCreateToken(TokenModel token) {
        if (token == null) return null;

        // Obtém o índice antes de adicionar à tabela de símbolos
        int symbolTableIndex = symbolTableService.getNextIndex();

        // Adiciona à tabela de símbolos
        symbolTableService.add(token.lexeme(), token.token());

        // Retorna um novo token com o índice da tabela de símbolos
        return new TokenModel(token.token(), token.lexeme(), token.line(), symbolTableIndex);
    }

    private boolean isPrimitiveType(TokenReservedWords token) {
        return token == TokenReservedWords.INTEGER ||
                token == TokenReservedWords.REAL ||
                token == TokenReservedWords.STRING ||
                token == TokenReservedWords.CHARACTER;
    }

    private boolean isValidDeclarationPair(TokenReservedWords typeToken, TokenModel nextToken) {
        if (nextToken == null) return false;

        return switch (typeToken) {
            case INTEGER -> nextToken.token() == TokenIdentifiers.INTCONST || nextToken.token() == TokenIdentifiers.VARIABLE;
            case REAL -> nextToken.token() == TokenIdentifiers.REALCONST || nextToken.token() == TokenIdentifiers.VARIABLE;
            case STRING -> nextToken.token() == TokenIdentifiers.STRINGCONST || nextToken.token() == TokenIdentifiers.VARIABLE;
            case CHARACTER -> nextToken.token() == TokenIdentifiers.CHARCONST || nextToken.token() == TokenIdentifiers.VARIABLE;
            default -> false;
        };
    }

    private void skipLine(int lineToSkip) {
        while (!isAtEnd() && currentLine == lineToSkip) {
            advance();
        }
    }

    private TokenModel readNextToken() {
        char currentChar = peekChar();
        return switch (getCharacterType(currentChar)) {
            case LETTER_OR_UNDERSCORE -> readIdentifierOrKeyword();
            case DIGIT -> readNumericLiteral();
            case QUOTE_CHAR -> readStringLiteral();
            case SINGLE_QUOTE_CHAR -> readCharacterLiteral();
            case SYMBOL -> readSymbol();
            case UNKNOWN -> {
                advance(); // Ignora caractere desconhecido
                yield null;
            }
        };
    }

    private CharacterType getCharacterType(char c) {
        if (Character.isLetter(c) || c == UNDERSCORE) return CharacterType.LETTER_OR_UNDERSCORE;
        if (Character.isDigit(c)) return CharacterType.DIGIT;
        if (c == QUOTE) return CharacterType.QUOTE_CHAR;
        if (c == SINGLE_QUOTE) return CharacterType.SINGLE_QUOTE_CHAR;
        if (isPotentialSymbol(c)) return CharacterType.SYMBOL;
        return CharacterType.UNKNOWN;
    }

    private boolean isPotentialSymbol(char c) {
        return !Character.isWhitespace(c)
                && !Character.isLetterOrDigit(c)
                && c != UNDERSCORE && c != QUOTE && c != SINGLE_QUOTE;
    }

    private TokenModel readIdentifierOrKeyword() {
        String lexeme = readWhile(ch -> Character.isLetterOrDigit(ch) || ch == UNDERSCORE);
        var keywordToken = TokenUtils.findTokenByWord(lexeme);

        if (keywordToken != null) {
            return new TokenModel(keywordToken, lexeme, currentLine);
        }

        if (isValidIdentifier(lexeme)) {
            return new TokenModel(TokenIdentifiers.VARIABLE, lexeme, currentLine);
        }

        return null;
    }

    private boolean isValidIdentifier(String identifier) {
        return identifier != null && !identifier.isEmpty() && IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    private TokenModel readNumericLiteral() {
        String integerPart = readWhile(Character::isDigit);

        if (peekChar() == DOT && isDigitAtPosition(currentPosition + 1)) {
            advance();
            String fractionalPart = readWhile(Character::isDigit);

            if (fractionalPart.isEmpty()) return null;

            String realLiteral = integerPart + DOT + fractionalPart;
            return new TokenModel(TokenIdentifiers.REALCONST, realLiteral, currentLine);
        }

        return new TokenModel(TokenIdentifiers.INTCONST, integerPart, currentLine);
    }

    private TokenModel readStringLiteral() {
        advance();

        StringBuilder content = new StringBuilder();

        while (!isAtEnd() && peekChar() != QUOTE) {
            char c = advance();
            if (c == NEWLINE) return null;
            content.append(c);
        }

        if (isAtEnd()) return null;

        advance();
        return new TokenModel(TokenIdentifiers.STRINGCONST, content.toString(), currentLine);
    }

    private TokenModel readCharacterLiteral() {
        advance();

        if (isAtEnd()) return null;

        char c = advance();

        if (c == SINGLE_QUOTE || c == NEWLINE) return null;

        if (isAtEnd() || advance() != SINGLE_QUOTE) return null;

        return new TokenModel(TokenIdentifiers.CHARCONST, String.valueOf(c), currentLine);
    }

    private TokenModel readSymbol() {
        if (currentPosition + 1 < sourceLength) {
            String twoCharSymbol = source.substring(currentPosition, currentPosition + 2);
            var token = TokenReservedSymbols.getTokenByName(twoCharSymbol);
            if (token != null) {
                currentPosition += 2;
                return new TokenModel(token, twoCharSymbol, currentLine);
            }
        }

        String oneCharSymbol = String.valueOf(advance());
        var token = TokenReservedSymbols.getTokenByName(oneCharSymbol);

        return token != null ? new TokenModel(token, oneCharSymbol, currentLine) : null;
    }

    private String readWhile(java.util.function.Predicate<Character> condition) {
        StringBuilder result = new StringBuilder();

        while (!isAtEnd() && condition.test(peekChar())) {
            result.append(advance());
        }

        return result.toString();
    }

    private boolean isCommentStart() {
        return peekChar() == FORWARD_SLASH &&
                currentPosition + 1 < sourceLength &&
                source.charAt(currentPosition + 1) == FORWARD_SLASH;
    }

    private void skipComment() {
        currentPosition += COMMENT_START.length();
        readWhile(c -> c != NEWLINE);
    }

    private void skipWhitespace() {
        readWhile(Character::isWhitespace);
    }

    private boolean isValidToken(TokenModel token) {
        return token != null && token.token() != null;
    }

    private boolean isDigitAtPosition(int pos) {
        return pos < sourceLength && Character.isDigit(source.charAt(pos));
    }

    private char peekChar() {
        return isAtEnd() ? NULL_CHAR : source.charAt(currentPosition);
    }

    private char advance() {
        if (isAtEnd()) return NULL_CHAR;

        char c = source.charAt(currentPosition++);
        if (c == NEWLINE) currentLine++;
        return c;
    }

    private boolean isAtEnd() {
        return currentPosition >= sourceLength;
    }

    private enum CharacterType {
        LETTER_OR_UNDERSCORE,
        DIGIT,
        QUOTE_CHAR,
        SINGLE_QUOTE_CHAR,
        SYMBOL,
        UNKNOWN
    }

}