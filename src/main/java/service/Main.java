package service;

import tokens.TokenUtils;
import tokens.IToken;

public class Main {
    private final static StringBuilder STRING_BUILDER = new StringBuilder();
    public static void main(String[] args) {
        IToken IToken = TokenUtils.findTokenByWord(":=");
        STRING_BUILDER.append(IToken.getName());
        STRING_BUILDER.append(IToken.getCode());
        System.out.println(STRING_BUILDER);
    }
}
