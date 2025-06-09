package tokens;

public enum TokenService {

    PALAVRAS_RESERVADAS,
    SIMBOLOS_RESERVADOS,
    IDENTIFICADORES,
    SUB_MAQUINAS,
    INDEFINIDO;

    public static TokenService identify(String token) {
        if (TokenReserverdWords.getTokenByName(token) != null) {
            return PALAVRAS_RESERVADAS;
        } else if (TokenReservedSymbols.getTokenByName(token) != null) {
            return SIMBOLOS_RESERVADOS;
        } else if (TokenIdentifiers.getTokenByName(token) != null) {
            return IDENTIFICADORES;
        } else if (TokenSubMachines.getTokenByName(token) != null) {
            return SUB_MAQUINAS;
        } else {
            return INDEFINIDO;
        }
    }

    public static String getAtomByName(String token) {

        switch (identify(token)) {
            case PALAVRAS_RESERVADAS:
                return TokenReserverdWords.getTokenByName(token).getCode();
            case SIMBOLOS_RESERVADOS:
                return TokenReservedSymbols.getTokenByName(token).getCode();
            case IDENTIFICADORES:
                return TokenIdentifiers.getTokenByName(token).getCode();
            case SUB_MAQUINAS:
                return TokenSubMachines.getTokenByName(token).getCode();
            default:
                return null;
        }
    }


}
