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

    /**
     * Retorna o próximo índice que será usado na tabela de símbolos
     */
    public int getNextIndex() {
        return index;
    }

    /**
     * Retorna o índice de um símbolo na tabela, ou -1 se não encontrado
     */
    public int getSymbolIndex(String lexeme) {
        SymbolEntryModel entry = table.get(lexeme);
        return entry != null ? entry.index() : -1;
    }

    /**
     * Verifica se um símbolo já existe na tabela
     */
    public boolean contains(String lexeme) {
        return table.containsKey(lexeme);
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

    /**
     * Retorna uma cópia da tabela de símbolos
     */
    public Map<String, SymbolEntryModel> getTable() {
        return new LinkedHashMap<>(table);
    }

    /**
     * Retorna o número total de símbolos na tabela
     */
    public int size() {
        return table.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tabela de Símbolos:\n");
        sb.append(String.format("%-6s | %-6s | %-35s | %-10s | %-12s | %s%n",
                "Índice", "Linha", "Lexema", "Truncado", "Não Truncado", "Token"));
        sb.append("-".repeat(90)).append("\n");

        for (SymbolEntryModel entry : table.values()) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }

}