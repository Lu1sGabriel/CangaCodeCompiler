package shared.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileReaderUtils {

    public static Map<Integer, String> readFileLines(String filePath) {
        Map<Integer, String> linesByNumber = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    linesByNumber.put(lineNumber, line);
                }
            }

        } catch (IOException exception) {
            System.err.println("Erro ao ler o arquivo: " + exception.getMessage());
        }

        return linesByNumber;
    }
}