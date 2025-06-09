package service;

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
            "(//.*)|" +                       // Comment
                    "(\"[^\"]*\")|" +                // Valid string
                    "('[^']')|" +                    // Valid char
                    "('[^']+')|" +                   // Invalid char (ignored)
                    "([a-zA-Z_][a-zA-Z0-9_]*)|" +   // Identifier
                    "([0-9]+)|" +                   // Number
                    "([=+\\-*/<>!;:,(){}\\[\\]])|" +// Operators
                    "(\\S+)"                        // Anything else (invalid)
    );

    public static Map<Integer, List<String>> processFile(String filePath) {
        Map<Integer, List<String>> tokensByLine = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                List<String> validTokens = new ArrayList<>();
                List<String> codeByLine = new ArrayList<>();
                Matcher matcher = TOKEN_PATTERN.matcher(line);

                while (matcher.find()) {

                    String token = matcher.group();

                    // Ignore comments and everything after
                    if (token.startsWith("//")) {
                        break;
                    }

                    if (token.matches("\"[^\"]*\"")) {
                        validTokens.add(token);
                        // valid string
                    } else if (token.matches("'[^']'")) {
                        validTokens.add(token);
                        // valid char
                    } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                        validTokens.add(token);
                        // identifier
                    } else if (token.matches("[0-9]+")) {
                        validTokens.add(token);
                        // number
                    } else if (token.matches("([=+\\-*/<>!;:,(){}\\[\\]])|")) {
                        validTokens.add(token);

                        // operadores simples
                    }else if (token.matches("(:=|<=|>=|!=)|")){
                        validTokens.add(token);
                    }

                }

                if (!validTokens.isEmpty()) {
                    tokensByLine.put(lineNumber, validTokens);
                    tokensByLine.put(lineNumber, codeByLine);
                }
            }

        } catch (IOException exception) {
            System.err.println("Error reading the file: " + exception.getMessage());
        }

        return tokensByLine;
    }

}