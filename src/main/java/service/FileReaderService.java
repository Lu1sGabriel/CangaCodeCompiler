package service;

import model.TokenModel;
import tokens.IToken;
import tokens.TokenUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReaderService {

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(//.*)|" +                         // Comentário
                    "(\"[^\"]*\")|" +                  // String válida
                    "('[^']')|" +                      // Char válido
                    "('[^']+')|" +                     // Char inválido
                    "([a-zA-Z_][a-zA-Z0-9_]*)|" +      // Identificador
                    "([0-9]+)|" +                      // Número
                    "(:=|<=|>=|!=)|" +                 // Operadores compostos
                    "([=+\\-*/<>!;:,(){}\\[\\]])|" +   // Operadores simples
                    "(\\S+)"                           // Inválidos
    );

    public static Map<Integer, List<TokenModel>> processFile(String filePath) {
        Map<Integer, List<TokenModel>> tokensByLine = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                List<TokenModel> tokenModels = new ArrayList<>();
                Matcher matcher = TOKEN_PATTERN.matcher(line);

                while (matcher.find()) {
                    String token = matcher.group();

                    // Ignora comentários
                    if (token.startsWith("//")) break;

                    // Verifica o token nos enums
                    IToken found = TokenUtils.findTokenByWord(token);
                    if (found != null) {
                        tokenModels.add(new TokenModel(found.getName(), found.getCode()));
                    }
                }

                if (!tokenModels.isEmpty()) {
                    tokensByLine.put(lineNumber, tokenModels);
                }
            }

        } catch (IOException exception) {
            System.err.println("Erro ao ler o arquivo: " + exception.getMessage());
        }

        return tokensByLine;
    }
}