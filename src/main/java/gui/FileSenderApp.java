package gui;

import service.FileReaderService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class FileSenderApp extends JFrame {

    private final JTextArea textArea;
    private final JLabel selectedFileLabel;
    private File selectedFile;

    public FileSenderApp() {
        super("Mini Lexical Analyzer - .251 Files");

        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with buttons and label
        JPanel topPanel = new JPanel(new FlowLayout());

        JButton btnChooseFile = new JButton("Choose File");
        selectedFileLabel = new JLabel("No file selected");

        JButton btnAnalyzeFile = new JButton("Analyze");

        topPanel.add(btnChooseFile);
        topPanel.add(selectedFileLabel);
        topPanel.add(btnAnalyzeFile);

        add(topPanel, BorderLayout.NORTH);

        // Text area for output
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Choose file action
        btnChooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("251 files", "251"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File chosenFile = chooser.getSelectedFile();

                // pasta destino dentro do projeto
                File destFolder = new File("src/main/resources/251");
                if (!destFolder.exists()) {
                    destFolder.mkdirs();
                }

                File destFile = new File(destFolder, chosenFile.getName());

                try {
                    // copia o arquivo selecionado para a pasta destino (substitui se já existir)
                    Files.copy(chosenFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    selectedFile = destFile;  // agora vamos analisar o arquivo dentro da pasta resources/251
                    selectedFileLabel.setText(selectedFile.getName());
                    textArea.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error copying file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Analyze action
        btnAnalyzeFile.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please choose a file first.", "No file selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            textArea.setText("");
            Map<Integer, List<String>> validTokens = FileReaderService.processFile(selectedFile.getPath());

            if (validTokens.isEmpty()) {
                textArea.append("No valid tokens found.\n");
            } else {
                for (Map.Entry<Integer, List<String>> line : validTokens.entrySet()) {
                    textArea.append("Line " + line.getKey() + ": " + line.getValue() + "\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileSenderApp gui = new FileSenderApp();
            gui.setVisible(true);
        });
    }
}
