package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenSubMachines implements IToken {
    SUBMACHINE1("submachine1", "SUB01"),
    SUBMACHINE2("submachine2", "SUB02"),
    SUBMACHINE3("submachine3", "SUB03"),
    SUBMACHINEN("submachinen", "SUBNN");

    private final String name;
    private final String code;

    TokenSubMachines(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, TokenSubMachines> TokenList = new HashMap<>();

    static {
        for (TokenSubMachines token : TokenSubMachines.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenSubMachines getTokenByName(String word) {
        if (word == null) {
            return null;
        }
        return TokenList.get(word);
    }

}