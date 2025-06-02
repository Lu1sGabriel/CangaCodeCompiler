package tokens;

import java.util.HashMap;
import java.util.Map;

public enum Token {

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
    PRINT("print", "PRS26"),

    // SRS - Símbolos
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
    MAIORIGUAL(">=", "SRS22"),

    // IDN - Identificadores
    PROGRAMNAME("programName", "IDN01"),
    VARIABLE("variable", "IDN02"),
    FUNCTIONNAME("functionName", "IDN03"),
    INTCONST("intConst", "IDN04"),
    REALCONST("realConst", "IDN05"),
    STRINGCONST("stringConst", "IDN06"),
    CHARCONST("charConst", "IDN07"),

    // SUB - Submáquinas
    SUBMACHINE1("submachine1", "SUB01"),
    SUBMACHINE2("submachine2", "SUB02"),
    SUBMACHINE3("submachine3", "SUB03"),
    SUBMACHINEN("submachinen", "SUBNN");

    private final String name;
    private final String code;

    Token(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, Token> TokenList = new HashMap<>();
    static {
        for (Token token : Token.values()) {
            TokenList.put(token.name(), token);
        }
    }


    public static Token getTokenbyName (String word) {
        if (word == null){
            return null;
        }
        return TokenList.get(word);
        
    }


}
