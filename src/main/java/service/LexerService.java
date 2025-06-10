package service;

import model.TokenModel;
import shared.utils.TokenUtils;
import tokens.TokenIdentifiers;
import tokens.TokenReservedSymbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Lexical analyzer service for tokenizing source code.
 * <p>
 * This service processes source code character by character and produces
 * a list of valid tokens, ignoring invalid or unrecognized tokens.
 */
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

    private final String source;
    private final int sourceLength;
    private int currentPosition = 0;

    /**
     * Creates a new lexer service for the given source code.
     *
     * @param source the source code to tokenize
     * @throws IllegalArgumentException if source is null
     */
    public LexerService(String source) {
        this.source = Objects.requireNonNull(source, "Source code cannot be null");
        this.sourceLength = source.length();
    }

    /**
     * Tokenizes the source code and returns a list of valid tokens.
     * Invalid or unrecognized tokens are ignored.
     *
     * @return list of valid tokens
     */
    public List<TokenModel> tokenize() {
        final ArrayList<TokenModel> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            skipWhitespace();

            if (isAtEnd()) break;

            if (isCommentStart()) {
                skipComment();
                continue;
            }

            var token = readNextToken();
            if (isValidToken(token)) {
                tokens.add(token);
            }
        }

        return List.copyOf(tokens);
    }

    /**
     * Reads the next token from the current position.
     *
     * @return the next token or null if invalid
     */
    private TokenModel readNextToken() {
        char currentChar = peekChar();

        return switch (getCharacterType(currentChar)) {
            case LETTER_OR_UNDERSCORE -> readIdentifierOrKeyword();
            case DIGIT -> readNumericLiteral();
            case QUOTE_CHAR -> readStringLiteral();
            case SINGLE_QUOTE_CHAR -> readCharacterLiteral();
            case SYMBOL -> readSymbol();
            case UNKNOWN -> {
                advance(); // Skip unknown character
                yield null;
            }
        };
    }

    /**
     * Determines the type of character for tokenization.
     */
    private CharacterType getCharacterType(char c) {
        if (Character.isLetter(c) || c == UNDERSCORE) {
            return CharacterType.LETTER_OR_UNDERSCORE;
        }
        if (Character.isDigit(c)) {
            return CharacterType.DIGIT;
        }
        if (c == QUOTE) {
            return CharacterType.QUOTE_CHAR;
        }
        if (c == SINGLE_QUOTE) {
            return CharacterType.SINGLE_QUOTE_CHAR;
        }
        if (isPotentialSymbol(c)) {
            return CharacterType.SYMBOL;
        }
        return CharacterType.UNKNOWN;
    }

    /**
     * Checks if a character could be part of a symbol token.
     */
    private boolean isPotentialSymbol(char c) {
        return !Character.isWhitespace(c) &&
                !Character.isLetterOrDigit(c) &&
                c != UNDERSCORE &&
                c != QUOTE &&
                c != SINGLE_QUOTE;
    }

    /**
     * Reads an identifier or keyword token.
     */
    private TokenModel readIdentifierOrKeyword() {
        var lexeme = readWhile(c -> Character.isLetterOrDigit(c) || c == UNDERSCORE);

        // Try to find as keyword first
        var keywordToken = TokenUtils.findTokenByWord(lexeme);
        if (keywordToken != null) {
            return new TokenModel(keywordToken, lexeme);
        }

        // Validate as identifier
        if (isValidIdentifier(lexeme)) {
            return new TokenModel(TokenIdentifiers.VARIABLE, lexeme);
        }

        return null; // Invalid identifier
    }

    /**
     * Validates if a string is a valid identifier.
     */
    private boolean isValidIdentifier(String identifier) {
        return identifier != null &&
                !identifier.isEmpty() &&
                IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    /**
     * Reads a numeric literal (integer or real).
     */
    private TokenModel readNumericLiteral() {
        var integerPart = readWhile(Character::isDigit);

        // Check for decimal point
        if (peekChar() == DOT && isDigitAtPosition(currentPosition + 1)) {
            advance(); // consume dot
            var fractionalPart = readWhile(Character::isDigit);

            if (fractionalPart.isEmpty()) {
                return null; // Invalid: dot without following digits
            }

            var realLiteral = integerPart + DOT + fractionalPart;
            return new TokenModel(TokenIdentifiers.REALCONST, realLiteral);
        }

        return new TokenModel(TokenIdentifiers.INTCONST, integerPart);
    }

    /**
     * Reads a string literal.
     */
    private TokenModel readStringLiteral() {
        advance(); // consume opening quote

        var content = new StringBuilder();

        while (!isAtEnd() && peekChar() != QUOTE) {
            char c = advance();

            if (c == NEWLINE) {
                return null; // Invalid: newline in string
            }

            content.append(c);
        }

        if (isAtEnd()) {
            return null; // Invalid: unterminated string
        }

        advance(); // consume closing quote
        return new TokenModel(TokenIdentifiers.STRINGCONST, content.toString());
    }

    /**
     * Reads a character literal.
     */
    private TokenModel readCharacterLiteral() {
        advance(); // consume opening quote

        if (isAtEnd()) {
            return null; // Invalid: EOF after opening quote
        }

        char character = advance();

        if (character == SINGLE_QUOTE || character == NEWLINE) {
            return null; // Invalid: empty char or newline
        }

        if (isAtEnd() || advance() != SINGLE_QUOTE) {
            return null; // Invalid: not properly closed
        }

        return new TokenModel(TokenIdentifiers.CHARCONST, String.valueOf(character));
    }

    /**
     * Reads a symbol token (single or double character).
     */
    private TokenModel readSymbol() {
        // Try two-character symbols first
        if (currentPosition + 1 < sourceLength) {
            var twoCharSymbol = source.substring(currentPosition, currentPosition + 2);
            var token = TokenReservedSymbols.getTokenByName(twoCharSymbol);

            if (token != null) {
                currentPosition += 2;
                return new TokenModel(token, twoCharSymbol);
            }
        }

        // Try single-character symbol
        var oneCharSymbol = String.valueOf(advance());
        var token = TokenReservedSymbols.getTokenByName(oneCharSymbol);

        return token != null ? new TokenModel(token, oneCharSymbol) : null;
    }

    /**
     * Reads characters while the predicate is true.
     */
    private String readWhile(java.util.function.Predicate<Character> predicate) {
        var result = new StringBuilder();

        while (!isAtEnd() && predicate.test(peekChar())) {
            result.append(advance());
        }

        return result.toString();
    }

    /**
     * Checks if we're at the start of a comment.
     */
    private boolean isCommentStart() {
        return peekChar() == FORWARD_SLASH &&
                currentPosition + 1 < sourceLength &&
                source.charAt(currentPosition + 1) == FORWARD_SLASH;
    }

    /**
     * Skips the current comment line.
     */
    private void skipComment() {
        currentPosition += COMMENT_START.length();
        readWhile(c -> c != NEWLINE);
    }

    /**
     * Skips whitespace characters.
     */
    private void skipWhitespace() {
        readWhile(Character::isWhitespace);
    }

    /**
     * Checks if a token is valid (not null and has a valid token type).
     */
    private boolean isValidToken(TokenModel token) {
        return token != null && token.token() != null;
    }

    /**
     * Checks if there's a digit at the specified position.
     */
    private boolean isDigitAtPosition(int position) {
        return position < sourceLength && Character.isDigit(source.charAt(position));
    }

    /**
     * Peeks at the current character without advancing.
     */
    private char peekChar() {
        return isAtEnd() ? NULL_CHAR : source.charAt(currentPosition);
    }

    /**
     * Advances to the next character and returns the current one.
     */
    private char advance() {
        return isAtEnd() ? NULL_CHAR : source.charAt(currentPosition++);
    }

    /**
     * Checks if we've reached the end of the source.
     */
    private boolean isAtEnd() {
        return currentPosition >= sourceLength;
    }

    /**
     * Enumeration of character types for tokenization.
     */
    private enum CharacterType {
        LETTER_OR_UNDERSCORE,
        DIGIT,
        QUOTE_CHAR,
        SINGLE_QUOTE_CHAR,
        SYMBOL,
        UNKNOWN
    }

}