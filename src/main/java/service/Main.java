package service;

import model.TokenModel;
import tokens.IToken;
import tokens.TokenUtils;

public class Main {
    public static void main(String[] args) {
        testToken(":=");
        testToken(";");
        testToken(">=");
        testToken("integer");
        testToken("submachine1");
        testToken("nonexistent");
    }

    public static void testToken(String word) {
        IToken token = TokenUtils.findTokenByWord(word);
        if (token != null) {
            TokenModel tokenModel = new TokenModel(token.getName(), token.getCode());
            System.out.println("Entrada: \"" + word + "\" → " + tokenModel);
        } else {
            System.out.println("Entrada: \"" + word + "\" → Token não encontrado.");
        }
    }

}
