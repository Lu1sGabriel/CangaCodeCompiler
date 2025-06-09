package tokens;

import java.util.HashMap;
import java.util.Map;

public enum TokenReserverdWords implements Token {

    // PRS - Palavras reservadas
    INTEGER("integer", "PRS01"),
    REAL("real", "PRS02"),
    CHARACTER("character", "PRS03"),
    STRING("string", "PRS04"),
    BOOLEAN("boolean", "PRS05"),
    VOID("void", "PRS06"),
    TRUE("true", "PRS07"),
    FALSE("false", "PRS08"),
    VARTYPE("varType", "PRS09"),
    FUNCTYPE("funcType", "PRS10"),
    PARAMTYPE("paramType", "PRS11"),
    DECLARATIONS("declarations", "PRS12"),
    ENDDECLARATIONS("endDeclarations", "PRS13"),
    PROGRAM("program", "PRS14"),
    ENDPROGRAM("endProgram", "PRS15"),
    FUNCTIONS("functions", "PRS16"),
    ENDFUNCTIONS("endFunctions", "PRS17"),
    ENDFUNCTION("endFunction", "PRS18"),
    RETURN("return", "PRS19"),
    IF("if", "PRS20"),
    ELSE("else", "PRS21"),
    ENDIF("endIf", "PRS22"),
    WHILE("while", "PRS23"),
    ENDWHILE("endWhile", "PRS24"),
    BREAK("break", "PRS25"),
    PRINT("print", "PRS26");


    private final String name;
    private final String code;

    TokenReserverdWords(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, TokenReserverdWords> TokenList = new HashMap<>();
    static {
        for (TokenReserverdWords token : TokenReserverdWords.values()) {
            TokenList.put(token.getName(), token);
        }
    }

    public static TokenReserverdWords getTokenByName (String word) {
        if (word == null){
            return null;
        }
        return TokenList.get(word);

    }


}
