package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenReservedSymbols implements IToken {
    PONTOVIRGULA(";", "SRS01"),
    VIRGULA(",", "SRS02"),
    DOISPONTOS(":", "SRS03"),
    ATRIBUICAO(":=", "SRS04"),
    INTERROGACAO("?", "SRS05"),
    PARENTESESABRIR("(", "SRS06"),
    PARENTESESFECHAR(")", "SRS07"),
    COLCHETEABRIR("[", "SRS08"),
    COLCHETEFECHAR("]", "SRS09"),
    CHAVEABRIR("{", "SRS10"),
    CHAVEFECHAR("}", "SRS11"),
    MAIS("+", "SRS12"),
    MENOS("-", "SRS13"),
    MULTIPLICACAO("*", "SRS14"),
    DIVISAO("/", "SRS15"),
    RESTO("%", "SRS16"),
    IGUAL("==", "SRS17"),
    DIFERENTE1("!=", "SRS18"),
    DIFERENTE2("#", "SRS18"),
    MENOR("<", "SRS19"),
    MENORIGUAL("<=", "SRS20"),
    MAIOR(">", "SRS21"),
    MAIORIGUAL(">=", "SRS22");

    private final String name;
    private final String code;

    TokenReservedSymbols(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    private static final Map<String, TokenReservedSymbols> TokenList = new HashMap<>();

    static {
        for (TokenReservedSymbols token : TokenReservedSymbols.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenReservedSymbols getTokenByName(String word) {
        if (word == null) {
            return null;
        }
        return TokenList.get(word);
    }

}