package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenSubMaquinas {

    // SUB - Submáquinas
    SUBMACHINE1("submachine1", "SUB01"),
    SUBMACHINE2("submachine2", "SUB02"),
    SUBMACHINE3("submachine3", "SUB03"),
    SUBMACHINEN("submachinen", "SUBNN");

    private final String name;
    private final String code;

    TokenSubMaquinas(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, TokenSubMaquinas> TokenList = new HashMap<>();
    static {
        for (TokenSubMaquinas token : TokenSubMaquinas.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenSubMaquinas getTokenByName (String word) {
        if (word == null){
            return null;
        }
        return TokenList.get(word);

    }


}
