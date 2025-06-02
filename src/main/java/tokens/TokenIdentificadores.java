package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenIdentificadores {
    PROGRAMNAME("programName", "IDN01"),
    VARIABLE("variable", "IDN02"),
    FUNCTIONNAME("functionName", "IDN03"),
    INTCONST("intConst", "IDN04"),
    REALCONST("realConst", "IDN05"),
    STRINGCONST("stringConst", "IDN06"),
    CHARCONST("charConst", "IDN07");

    private final String name;
    private final String code;

    TokenIdentificadores(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, TokenIdentificadores> TokenList = new HashMap<>();
    static {
        for (TokenIdentificadores token : TokenIdentificadores.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenIdentificadores getTokenByName (String word) {
        if (word == null){
            return null;
        }
        return TokenList.get(word);

    }

}
