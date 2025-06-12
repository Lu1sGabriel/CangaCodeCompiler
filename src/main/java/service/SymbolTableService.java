package service;

import model.SymbolEntryModel;
import shared.utils.TokenUtils;
import tokens.IToken;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTableService {

    private final Map<String, SymbolEntryModel> table = new LinkedHashMap<>();
    private final int truncationLimit;
    private int index = 1;
    private int line = 1;

    public SymbolTableService() {
        this(35);
    }

    public SymbolTableService(int truncationLimit) {
        this.truncationLimit = truncationLimit;
    }

    public void add(String lexeme, IToken token) {
        if (!isStorable(token) || table.containsKey(lexeme)) return;

        int notTruncated = lexeme.length();
        int truncated = Math.min(notTruncated, truncationLimit);

        SymbolEntryModel entry = new SymbolEntryModel(index++, line++, lexeme, truncated, notTruncated, token);
        table.put(lexeme, entry);
    }

    private boolean isStorable(IToken token) {
        if (token == null) return false;
        IToken iToken = TokenUtils.findTokenByWord(token.getName());
        return iToken != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tabela de Símbolos:\n");
        for (SymbolEntryModel entry : table.values()) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }

}