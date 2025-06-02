package gui;

import service.FileReaderService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class FileSenderApp extends JFrame {

    private final JTextArea textArea;
    private final JLabel selectedFileLabel;
    private File selectedFile;

    public FileSenderApp() {
        super("Mini Lexical Analyzer - .251 Files");

        // UI Setup
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fileRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnChooseFile = new JButton("📁 Choose File");
        JButton btnOpenExisting = new JButton("📂 Open from '251' Folder");

        selectedFileLabel = new JLabel("No file selected");

        fileRow.add(btnChooseFile);
        fileRow.add(btnOpenExisting);
        fileRow.add(selectedFileLabel);

        JPanel analyzeRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAnalyzeFile = new JButton("🔍 Analyze");
        analyzeRow.add(btnAnalyzeFile);

        topPanel.add(fileRow);
        topPanel.add(analyzeRow);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER TEXT AREA =====
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // ===== ACTIONS =====
        btnChooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("*.251 Files", "251"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                selectedFileLabel.setText("Selected: " + selectedFile.getName());
                textArea.setText("");
            }
        });

        btnOpenExisting.addActionListener(e -> {
            File dir = new File("src/main/resources/251");
            if (!dir.exists() || !dir.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Folder not found: src/main/resources/251", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File[] files = dir.listFiles((d, name) -> name.endsWith(".251"));
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(this, "No .251 files found in the folder.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select a file:", "Open File",
                    JOptionPane.PLAIN_MESSAGE, null, fileNames, fileNames[0]);

            if (selected != null) {
                selectedFile = new File(dir, selected);
                selectedFileLabel.setText("Selected: " + selectedFile.getName());
                textArea.setText("");
            }
        });

        btnAnalyzeFile.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please choose a file first.", "No file selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<Integer, List<String>> validTokens = FileReaderService.processFile(selectedFile.getPath());
            textArea.setText("");

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
            applyLookAndFeel();
            new FileSenderApp().setVisible(true);
        });
    }

    // Set global font
    private static void setUIFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
    }

    // Optional: Use system look and feel (nativo)
    private static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }
}
