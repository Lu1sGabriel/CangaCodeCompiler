package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenIdentifiers implements IToken {
    PROGRAMNAME("programName", "IDN01"),
    VARIABLE("variable", "IDN02"),
    FUNCTIONNAME("functionName", "IDN03"),
    INTCONST("intConst", "IDN04"),
    REALCONST("realConst", "IDN05"),
    STRINGCONST("stringConst", "IDN06"),
    CHARCONST("charConst", "IDN07");

    private final String name;
    private final String code;

    TokenIdentifiers(String name, String code) {
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

    @Override
    public TokenType getType() {
        return TokenType.IDENTIFIER;
    }

    private static final Map<String, TokenIdentifiers> TokenList = new HashMap<>();

    static {
        for (TokenIdentifiers token : TokenIdentifiers.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenIdentifiers getTokenByName(String word) {
        if (word == null) {
            return null;
        }
        return TokenList.get(word);
    }

}