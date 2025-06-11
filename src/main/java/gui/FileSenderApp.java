package gui;

import model.TokenModel;
import service.LexerService;
import shared.utils.FileReaderUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class FileSenderApp extends JFrame {

    private final JTextArea textArea;
    private JLabel selectedFileLabel;
    private JLabel analysisStatusLabel;
    private File selectedFile;

    // Informações da equipe - ALTERE AQUI CONFORME SUA EQUIPE
    private static final String TEAM_CODE = "EQUIPE-001";
    private static final String[] TEAM_MEMBERS = {
            "João Silva - Matrícula: 12345678",
            "Maria Santos - Matrícula: 87654321",
            "Pedro Oliveira - Matrícula: 11223344"
    };

    public FileSenderApp() {
        super("Mini Lexical Analyzer - .251 Files");

        // UI Setup
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);
        setUIFont(new Font("Segoe UI", Font.PLAIN, 12));

        // ===== HEADER PANEL =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER TEXT AREA =====
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Resultado da Análise Léxica",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize with welcome message
        showWelcomeMessage();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createHeaderPanel() {
<<<<<<< Updated upstream
        JPanel headerPanel = new JPanel(new BorderLayout());
=======
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
>>>>>>> Stashed changes
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        headerPanel.setBackground(new Color(240, 248, 255));

<<<<<<< Updated upstream
        // Left side - Team Information and Report Header
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 248, 255));

        // Team Information Section
        JPanel teamInfoPanel = createTeamInfoPanel();
        leftPanel.add(teamInfoPanel);

        leftPanel.add(Box.createVerticalStrut(10));

        // Analysis Report Header
        JPanel reportHeaderPanel = createReportHeaderPanel();
        leftPanel.add(reportHeaderPanel);

        // Right side - File Selection Panel (Buttons)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(240, 248, 255));

        // Add some vertical space to center the buttons
        rightPanel.add(Box.createVerticalGlue());

        JPanel fileSelectionPanel = createFileSelectionPanel();
        rightPanel.add(fileSelectionPanel);

        rightPanel.add(Box.createVerticalGlue());

        // Add panels to header
        headerPanel.add(leftPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);
=======
        // Team Information Section
        JPanel teamInfoPanel = createTeamInfoPanel();
        headerPanel.add(teamInfoPanel);

        headerPanel.add(Box.createVerticalStrut(10));

        // Analysis Report Header
        JPanel reportHeaderPanel = createReportHeaderPanel();
        headerPanel.add(reportHeaderPanel);

        headerPanel.add(Box.createVerticalStrut(10));

        // File Selection Panel
        JPanel fileSelectionPanel = createFileSelectionPanel();
        headerPanel.add(fileSelectionPanel);
>>>>>>> Stashed changes

        return headerPanel;
    }

    private JPanel createTeamInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Informações da Equipe",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12)
        ));
        panel.setBackground(new Color(240, 248, 255));

        // Team Code
<<<<<<< Updated upstream
        JLabel teamCodeLabel = new JLabel("Código da Equipe: " + TEAM_CODE);
=======
        JLabel teamCodeLabel = new JLabel("🏷️ Código da Equipe: " + TEAM_CODE);
>>>>>>> Stashed changes
        teamCodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        teamCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(teamCodeLabel);

        panel.add(Box.createVerticalStrut(8));

        // Team Members
<<<<<<< Updated upstream
        JLabel membersLabel = new JLabel("Componentes:");
=======
        JLabel membersLabel = new JLabel("👥 Componentes:");
>>>>>>> Stashed changes
        membersLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        membersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(membersLabel);

        for (String member : TEAM_MEMBERS) {
<<<<<<< Updated upstream
            JLabel memberLabel = new JLabel("• " + member);
=======
            JLabel memberLabel = new JLabel("   • " + member);
>>>>>>> Stashed changes
            memberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            memberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(memberLabel);
        }

        return panel;
    }

    private JPanel createReportHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Relatório da Análise Léxica",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12)
        ));
        panel.setBackground(new Color(240, 248, 255));

<<<<<<< Updated upstream
        selectedFileLabel = new JLabel("Texto fonte analisado: Nenhum arquivo selecionado");
=======
        selectedFileLabel = new JLabel("📄 Texto fonte analisado: Nenhum arquivo selecionado");
>>>>>>> Stashed changes
        selectedFileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        selectedFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(selectedFileLabel);

        panel.add(Box.createVerticalStrut(5));

<<<<<<< Updated upstream
        analysisStatusLabel = new JLabel("Status: Aguardando seleção de arquivo");
=======
        analysisStatusLabel = new JLabel("📊 Status: Aguardando seleção de arquivo");
>>>>>>> Stashed changes
        analysisStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        analysisStatusLabel.setForeground(new Color(102, 102, 102));
        analysisStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(analysisStatusLabel);

        return panel;
    }

    private JPanel createFileSelectionPanel() {
<<<<<<< Updated upstream
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Ações",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JButton btnChooseFile = new JButton("Escolher Arquivo");
        JButton btnOpenExisting = new JButton("Abrir da Pasta '251'");
        JButton btnAnalyzeFile = new JButton("Analisar Arquivo");
=======
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 248, 255));

        JButton btnChooseFile = new JButton("📁 Escolher Arquivo");
        JButton btnOpenExisting = new JButton("📂 Abrir da Pasta '251'");
        JButton btnAnalyzeFile = new JButton("🔍 Analisar Arquivo");
>>>>>>> Stashed changes

        // Style buttons
        styleButton(btnChooseFile, new Color(52, 152, 219));
        styleButton(btnOpenExisting, new Color(155, 89, 182));
        styleButton(btnAnalyzeFile, new Color(46, 204, 113));

<<<<<<< Updated upstream
        // Set button alignment and size
        btnChooseFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOpenExisting.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnalyzeFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make buttons same width
        Dimension buttonSize = new Dimension(180, 35);
        btnChooseFile.setPreferredSize(buttonSize);
        btnChooseFile.setMaximumSize(buttonSize);
        btnOpenExisting.setPreferredSize(buttonSize);
        btnOpenExisting.setMaximumSize(buttonSize);
        btnAnalyzeFile.setPreferredSize(buttonSize);
        btnAnalyzeFile.setMaximumSize(buttonSize);

        panel.add(Box.createVerticalStrut(5));
        panel.add(btnChooseFile);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnOpenExisting);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnAnalyzeFile);
        panel.add(Box.createVerticalStrut(5));
=======
        panel.add(btnChooseFile);
        panel.add(btnOpenExisting);
        panel.add(btnAnalyzeFile);
>>>>>>> Stashed changes

        // Add action listeners
        setupButtonActions(btnChooseFile, btnOpenExisting, btnAnalyzeFile);

        return panel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        bottomPanel.setBackground(new Color(248, 249, 250));

        JLabel statusLabel = new JLabel("Mini Analisador Léxico v1.0 - Pronto para uso");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        statusLabel.setForeground(new Color(102, 102, 102));
        bottomPanel.add(statusLabel);

        return bottomPanel;
    }

    private void setupButtonActions(JButton btnChooseFile, JButton btnOpenExisting, JButton btnAnalyzeFile) {
        btnChooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Arquivos .251", "251"));
            chooser.setDialogTitle("Selecionar Arquivo .251");

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                updateFileSelection();
                textArea.setText("");
                showFileSelectedMessage();
            }
        });

        btnOpenExisting.addActionListener(e -> {
            File dir = new File("src/main/resources/251");
            if (!dir.exists() || !dir.isDirectory()) {
                JOptionPane.showMessageDialog(this,
                        "Pasta não encontrada: src/main/resources/251",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            File[] files = dir.listFiles((d, name) -> name.endsWith(".251"));
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum arquivo .251 encontrado na pasta.",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }

            String selected = (String) JOptionPane.showInputDialog(this,
                    "Selecione um arquivo:",
                    "Abrir Arquivo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    fileNames,
                    fileNames[0]);

            if (selected != null) {
                selectedFile = new File(dir, selected);
                updateFileSelection();
                textArea.setText("");
                showFileSelectedMessage();
            }
        });

        btnAnalyzeFile.addActionListener(e -> performLexicalAnalysis());
    }

    private void updateFileSelection() {
<<<<<<< Updated upstream
        selectedFileLabel.setText("Texto fonte analisado: " + selectedFile.getName());
        analysisStatusLabel.setText("Status: Arquivo selecionado - Pronto para análise");
=======
        selectedFileLabel.setText("📄 Texto fonte analisado: " + selectedFile.getName());
        analysisStatusLabel.setText("📊 Status: Arquivo selecionado - Pronto para análise");
>>>>>>> Stashed changes
        analysisStatusLabel.setForeground(new Color(39, 174, 96));
    }

    private void showWelcomeMessage() {
        textArea.setText("=".repeat(60) + "\n");
<<<<<<< Updated upstream
        textArea.append("MINI ANALISADOR LÉXICO - ARQUIVOS .251\n");
        textArea.append("=".repeat(60) + "\n\n");
        textArea.append("Bem-vindo ao Mini Analisador Léxico!\n\n");
        textArea.append("INSTRUÇÕES:\n");
        textArea.append("1. Selecione um arquivo .251 usando os botões acima\n");
        textArea.append("2. Clique em 'Analisar Arquivo' para executar a análise léxica\n");
        textArea.append("3. Os tokens encontrados serão exibidos nesta área\n\n");
        textArea.append("O analisador reconhece:\n");
        textArea.append("• Identificadores e palavras reservadas\n");
        textArea.append("• Números inteiros e reais\n");
        textArea.append("• Strings e caracteres\n");
        textArea.append("• Símbolos e operadores\n");
        textArea.append("• Comentários (ignorados)\n\n");
        textArea.append("Pronto para começar!\n");
=======
        textArea.append("       MINI ANALISADOR LÉXICO - ARQUIVOS .251\n");
        textArea.append("=".repeat(60) + "\n\n");
        textArea.append("Bem-vindo ao Mini Analisador Léxico!\n\n");
        textArea.append("📋 INSTRUÇÕES:\n");
        textArea.append("   1. Selecione um arquivo .251 usando os botões acima\n");
        textArea.append("   2. Clique em 'Analisar Arquivo' para executar a análise léxica\n");
        textArea.append("   3. Os tokens encontrados serão exibidos nesta área\n\n");
        textArea.append("⚡ O analisador reconhece:\n");
        textArea.append("   • Identificadores e palavras reservadas\n");
        textArea.append("   • Números inteiros e reais\n");
        textArea.append("   • Strings e caracteres\n");
        textArea.append("   • Símbolos e operadores\n");
        textArea.append("   • Comentários (ignorados)\n\n");
        textArea.append("🚀 Pronto para começar!\n");
>>>>>>> Stashed changes
    }

    private void showFileSelectedMessage() {
        textArea.setText("=".repeat(60) + "\n");
        textArea.append("       ARQUIVO SELECIONADO\n");
        textArea.append("=".repeat(60) + "\n\n");
<<<<<<< Updated upstream
        textArea.append("Arquivo: " + selectedFile.getName() + "\n");
        textArea.append("Caminho: " + selectedFile.getAbsolutePath() + "\n\n");
        textArea.append("Arquivo carregado com sucesso!\n");
        textArea.append("Clique em 'Analisar Arquivo' para executar a análise léxica.\n");
=======
        textArea.append("📄 Arquivo: " + selectedFile.getName() + "\n");
        textArea.append("📂 Caminho: " + selectedFile.getAbsolutePath() + "\n\n");
        textArea.append("✅ Arquivo carregado com sucesso!\n");
        textArea.append("🔍 Clique em 'Analisar Arquivo' para executar a análise léxica.\n");
>>>>>>> Stashed changes
    }

    private void performLexicalAnalysis() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um arquivo primeiro.",
                    "Nenhum arquivo selecionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

<<<<<<< Updated upstream
        analysisStatusLabel.setText("Status: Analisando arquivo...");
=======
        analysisStatusLabel.setText("📊 Status: Analisando arquivo...");
>>>>>>> Stashed changes
        analysisStatusLabel.setForeground(new Color(243, 156, 18));

        try {
            // Read file lines
            Map<Integer, String> lines = FileReaderUtils.readFileLines(selectedFile.getPath());

            textArea.setText("");

            if (lines.isEmpty()) {
<<<<<<< Updated upstream
                textArea.append("Nenhuma linha válida encontrada no arquivo.\n");
                analysisStatusLabel.setText("Status: Erro - Arquivo vazio ou inválido");
=======
                textArea.append("❌ Nenhuma linha válida encontrada no arquivo.\n");
                analysisStatusLabel.setText("📊 Status: Erro - Arquivo vazio ou inválido");
>>>>>>> Stashed changes
                analysisStatusLabel.setForeground(new Color(231, 76, 60));
                return;
            }

            // Join lines for lexer
            StringBuilder sourceBuilder = new StringBuilder();
            for (String line : lines.values()) {
                sourceBuilder.append(line).append("\n");
            }
            String source = sourceBuilder.toString();

            // Perform lexical analysis
            LexerService lexerService = new LexerService(source);
            List<TokenModel> tokens = lexerService.tokenize();

            // Display results
            displayAnalysisResults(tokens, lines.size());

        } catch (Exception ex) {
<<<<<<< Updated upstream
            textArea.setText("ERRO durante a análise:\n\n" + ex.getMessage());
            analysisStatusLabel.setText("Status: Erro durante análise");
=======
            textArea.setText("❌ ERRO durante a análise:\n\n" + ex.getMessage());
            analysisStatusLabel.setText("📊 Status: Erro durante análise");
>>>>>>> Stashed changes
            analysisStatusLabel.setForeground(new Color(231, 76, 60));
        }
    }

    private void displayAnalysisResults(List<TokenModel> tokens, int totalLines) {
        textArea.append("=".repeat(80) + "\n");
<<<<<<< Updated upstream
        textArea.append("RESULTADO DA ANÁLISE LÉXICA\n");
        textArea.append("=".repeat(80) + "\n\n");

        textArea.append("Arquivo analisado: " + selectedFile.getName() + "\n");
        textArea.append("Linhas processadas: " + totalLines + "\n");
        textArea.append("Tokens encontrados: " + tokens.size() + "\n");
        textArea.append("Análise concluída em: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");

        if (tokens.isEmpty()) {
            textArea.append("Nenhum token válido foi encontrado no arquivo.\n");
            textArea.append("Verifique se o arquivo contém código válido.\n");
        } else {
            textArea.append("LISTA DE TOKENS RECONHECIDOS:\n");
=======
        textArea.append("                    RESULTADO DA ANÁLISE LÉXICA\n");
        textArea.append("=".repeat(80) + "\n\n");

        textArea.append("📄 Arquivo analisado: " + selectedFile.getName() + "\n");
        textArea.append("📊 Linhas processadas: " + totalLines + "\n");
        textArea.append("🎯 Tokens encontrados: " + tokens.size() + "\n");
        textArea.append("⏰ Análise concluída em: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");

        if (tokens.isEmpty()) {
            textArea.append("⚠️  Nenhum token válido foi encontrado no arquivo.\n");
            textArea.append("    Verifique se o arquivo contém código válido.\n");
        } else {
            textArea.append("📋 LISTA DE TOKENS RECONHECIDOS:\n");
>>>>>>> Stashed changes
            textArea.append("-".repeat(50) + "\n");

            int tokenCount = 1;
            for (TokenModel token : tokens) {
<<<<<<< Updated upstream
                textArea.append(String.format(
                        "%3d. %-20s | %-40s | %-10s\n",
                        tokenCount++,
                        token.token().getName(),
                        "Lexema: \"" + token.lexeme() + "\"",
                        "Code: \"" + token.token().getCode() + "\""
                ));
            }

            textArea.append("-".repeat(50) + "\n");
            textArea.append("Análise léxica concluída com sucesso!\n");
        }

        // Update status
        analysisStatusLabel.setText("Status: Análise concluída - " + tokens.size() + " tokens encontrados");
=======
                textArea.append(String.format("%3d. %-20s | %s\n",
                        tokenCount++,
                        token.token().getName(),
                        "\"" + token.lexeme() + "\""));
            }

            textArea.append("-".repeat(50) + "\n");
            textArea.append("✅ Análise léxica concluída com sucesso!\n");
        }

        // Update status
        analysisStatusLabel.setText("📊 Status: Análise concluída - " + tokens.size() + " tokens encontrados");
>>>>>>> Stashed changes
        analysisStatusLabel.setForeground(new Color(39, 174, 96));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();
            new FileSenderApp().setVisible(true);
        });
    }

    private static void setUIFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TitledBorder.font", font);
    }

    private static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default look and feel
        }
    }
}