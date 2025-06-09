package tokens;

public class TokenUtils {

    public static IToken findTokenByWord(String word) {
        if (word == null) return null;

        IToken token;

        token = TokenIdentifiers.getTokenByName(word);
        if (token != null) return token;

        token = TokenReservedWords.getTokenByName(word);
        if (token != null) return token;

        token = TokenReservedSymbols.getTokenByName(word);
        if (token != null) return token;

        token = TokenSubMachines.getTokenByName(word);
        return token;
    }

}