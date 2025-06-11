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
 * Serviço de análise léxica para tokenizar (quebrar em unidades) o código fonte.
 * <p>
 * Este serviço percorre o código-fonte caractere por caractere e produz
 * uma lista de tokens válidos, ignorando os inválidos ou não reconhecidos.
 */
public final class LexerService {

    // Constantes para representar caracteres especiais
    private static final char NULL_CHAR = '\0';              // Caractere nulo
    private static final char NEWLINE = '\n';                // Quebra de linha
    private static final char QUOTE = '"';                   // Aspas duplas
    private static final char SINGLE_QUOTE = '\'';           // Aspas simples
    private static final char DOT = '.';                     // Ponto
    private static final char UNDERSCORE = '_';              // Underscore
    private static final char FORWARD_SLASH = '/';           // Barra (/)

    private static final String COMMENT_START = "//";        // Início de comentário
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"); // Expressão para identificadores válidos

    private final String source;         // Código-fonte a ser analisado
    private final int sourceLength;     // Tamanho do código-fonte
    private int currentPosition = 0;    // Posição atual de leitura

    /**
     * Construtor: cria um novo analisador léxico com o código-fonte fornecido.
     *
     * @param source o código-fonte a ser tokenizado
     * @throws IllegalArgumentException se o código fonte for nulo
     */
    public LexerService(String source) {
        this.source = Objects.requireNonNull(source, "Código fonte não pode ser nulo");
        this.sourceLength = source.length();
    }

    /**
     * Realiza a tokenização do código-fonte, retornando uma lista de tokens válidos.
     * Tokens inválidos ou não reconhecidos são ignorados.
     *
     * @return lista imutável de tokens válidos
     */
    public List<TokenModel> tokenize() {
        final ArrayList<TokenModel> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            skipWhitespace(); // Ignora espaços em branco

            if (isAtEnd()) break;

            if (isCommentStart()) {
                skipComment(); // Ignora linha de comentário
                continue;
            }

            var token = readNextToken();
            if (isValidToken(token)) {
                tokens.add(token); // Adiciona token válido
            }
        }

        return List.copyOf(tokens); // Retorna lista imutável
    }

    /**
     * Lê e retorna o próximo token a partir da posição atual.
     *
     * @return o próximo token ou null se inválido
     */
    private TokenModel readNextToken() {
        char currentChar = peekChar();

        return switch (getCharacterType(currentChar)) {
            case LETTER_OR_UNDERSCORE -> readIdentifierOrKeyword();   // Identificador ou palavra-chave
            case DIGIT -> readNumericLiteral();                       // Número (inteiro ou real)
            case QUOTE_CHAR -> readStringLiteral();                   // String entre aspas
            case SINGLE_QUOTE_CHAR -> readCharacterLiteral();         // Caractere entre aspas simples
            case SYMBOL -> readSymbol();                              // Símbolo reservado
            case UNKNOWN -> {
                advance(); // Ignora caractere desconhecido
                yield null;
            }
        };
    }

    /**
     * Retorna o tipo de caractere para ajudar na decisão de qual token ler.
     */
    private CharacterType getCharacterType(char c) {
        if (Character.isLetter(c) || c == UNDERSCORE) return CharacterType.LETTER_OR_UNDERSCORE;
        if (Character.isDigit(c)) return CharacterType.DIGIT;
        if (c == QUOTE) return CharacterType.QUOTE_CHAR;
        if (c == SINGLE_QUOTE) return CharacterType.SINGLE_QUOTE_CHAR;
        if (isPotentialSymbol(c)) return CharacterType.SYMBOL;
        return CharacterType.UNKNOWN;
    }

    /**
     * Verifica se o caractere pode ser parte de um símbolo reservado.
     */
    private boolean isPotentialSymbol(char c) {
        return !Character.isWhitespace(c) &&
                !Character.isLetterOrDigit(c) &&
                c != UNDERSCORE && c != QUOTE && c != SINGLE_QUOTE;
    }

    /**
     * Lê um identificador (variável) ou palavra-chave do código.
     */
    private TokenModel readIdentifierOrKeyword() {
        var lexeme = readWhile(c -> Character.isLetterOrDigit(c) || c == UNDERSCORE);

        // Verifica se é uma palavra-chave reservada
        var keywordToken = TokenUtils.findTokenByWord(lexeme);
        if (keywordToken != null) {
            return new TokenModel(keywordToken, lexeme);
        }

        // Verifica se é um identificador válido
        if (isValidIdentifier(lexeme)) {
            return new TokenModel(TokenIdentifiers.VARIABLE, lexeme);
        }

        return null; // Identificador inválido
    }

    /**
     * Verifica se uma string é um identificador válido.
     */
    private boolean isValidIdentifier(String identifier) {
        return identifier != null &&
                !identifier.isEmpty() &&
                IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    /**
     * Lê um número inteiro ou real do código.
     */
    private TokenModel readNumericLiteral() {
        var integerPart = readWhile(Character::isDigit);

        // Verifica se há um ponto para literal real
        if (peekChar() == DOT && isDigitAtPosition(currentPosition + 1)) {
            advance(); // Consome o ponto
            var fractionalPart = readWhile(Character::isDigit);

            if (fractionalPart.isEmpty()) {
                return null; // Ponto sem dígitos após ele
            }

            var realLiteral = integerPart + DOT + fractionalPart;
            return new TokenModel(TokenIdentifiers.REALCONST, realLiteral);
        }

        return new TokenModel(TokenIdentifiers.INTCONST, integerPart);
    }

    /**
     * Lê uma string entre aspas.
     */
    private TokenModel readStringLiteral() {
        advance(); // Consome aspas de abertura

        var content = new StringBuilder();

        while (!isAtEnd() && peekChar() != QUOTE) {
            char c = advance();
            if (c == NEWLINE) return null; // String não pode conter nova linha
            content.append(c);
        }

        if (isAtEnd()) return null; // String sem aspas de fechamento

        advance(); // Consome aspas de fechamento
        return new TokenModel(TokenIdentifiers.STRINGCONST, content.toString());
    }

    /**
     * Lê um caractere entre aspas simples.
     */
    private TokenModel readCharacterLiteral() {
        advance(); // Consome aspas de abertura

        if (isAtEnd()) return null;

        char character = advance();

        if (character == SINGLE_QUOTE || character == NEWLINE) {
            return null; // Caractere vazio ou quebra de linha
        }

        if (isAtEnd() || advance() != SINGLE_QUOTE) {
            return null; // Não fechou corretamente
        }

        return new TokenModel(TokenIdentifiers.CHARCONST, String.valueOf(character));
    }

    /**
     * Lê um símbolo reservado (ex: operadores como ==, <=, etc).
     */
    private TokenModel readSymbol() {
        // Verifica símbolos de dois caracteres
        if (currentPosition + 1 < sourceLength) {
            var twoCharSymbol = source.substring(currentPosition, currentPosition + 2);
            var token = TokenReservedSymbols.getTokenByName(twoCharSymbol);

            if (token != null) {
                currentPosition += 2;
                return new TokenModel(token, twoCharSymbol);
            }
        }

        // Tenta com um caractere só
        var oneCharSymbol = String.valueOf(advance());
        var token = TokenReservedSymbols.getTokenByName(oneCharSymbol);

        return token != null ? new TokenModel(token, oneCharSymbol) : null;
    }

    /**
     * Lê caracteres enquanto a condição fornecida for verdadeira.
     */
    private String readWhile(java.util.function.Predicate<Character> predicate) {
        var result = new StringBuilder();

        while (!isAtEnd() && predicate.test(peekChar())) {
            result.append(advance());
        }

        return result.toString();
    }

    /**
     * Verifica se o ponto atual inicia um comentário (//).
     */
    private boolean isCommentStart() {
        return peekChar() == FORWARD_SLASH &&
                currentPosition + 1 < sourceLength &&
                source.charAt(currentPosition + 1) == FORWARD_SLASH;
    }

    /**
     * Pula (ignora) a linha atual de comentário.
     */
    private void skipComment() {
        currentPosition += COMMENT_START.length();
        readWhile(c -> c != NEWLINE); // Lê até a próxima quebra de linha
    }

    /**
     * Pula espaços em branco.
     */
    private void skipWhitespace() {
        readWhile(Character::isWhitespace);
    }

    /**
     * Verifica se o token é válido (não nulo e com tipo válido).
     */
    private boolean isValidToken(TokenModel token) {
        return token != null && token.token() != null;
    }

    /**
     * Verifica se há um dígito na posição especificada.
     */
    private boolean isDigitAtPosition(int position) {
        return position < sourceLength && Character.isDigit(source.charAt(position));
    }

    /**
     * Retorna o caractere atual sem avançar a posição.
     */
    private char peekChar() {
        return isAtEnd() ? NULL_CHAR : source.charAt(currentPosition);
    }

    /**
     * Avança para o próximo caractere e retorna o atual.
     */
    private char advance() {
        return isAtEnd() ? NULL_CHAR : source.charAt(currentPosition++);
    }

    /**
     * Verifica se chegou ao final do código fonte.
     */
    private boolean isAtEnd() {
        return currentPosition >= sourceLength;
    }

    /**
     * Enumeração com os tipos de caracteres reconhecidos para a análise.
     */
    private enum CharacterType {
        LETTER_OR_UNDERSCORE,   // Letra ou underscore (ex: nomes de variáveis)
        DIGIT,                  // Dígito numérico
        QUOTE_CHAR,             // Aspas duplas (strings)
        SINGLE_QUOTE_CHAR,      // Aspas simples (char)
        SYMBOL,                 // Símbolo especial (operadores, pontuação, etc.)
        UNKNOWN                 // Caractere desconhecido
    }

}