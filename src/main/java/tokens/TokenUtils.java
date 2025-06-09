package tokens;

public class TokenUtils {

    public static IToken findTokenByWord(String word) {
        if (word == null) return null;

        IToken token;

        token = TokenReservedSymbols.getTokenByName(word);
        if (token != null) return token;

        token = TokenReserverdWords.getTokenByName(word);
        if (token != null) return token;

        token = TokenSubMachines.getTokenByName(word);
        return token;
    }

}