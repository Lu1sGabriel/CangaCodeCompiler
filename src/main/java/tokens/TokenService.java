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

    public static String getAtomName(String token) {
        switch (identify(token)) {
            case PALAVRAS_RESERVADAS:
                return TokenReserverdWords.getTokenByName(token).getName();
            case SIMBOLOS_RESERVADOS:
                return TokenReservedSymbols.getTokenByName(token).getName();
            case IDENTIFICADORES:
                return TokenIdentifiers.getTokenByName(token).getName();
            case SUB_MAQUINAS:
                return TokenSubMachines.getTokenByName(token).getName();
            default:
                return "NULO";
        }
    }


}
