package ui;

import javax.swing.*;
import java.awt.*;
import utils.InvalidInputException;

public abstract class MissionPanel extends JPanel {

    protected static final Color BG = new Color(10, 10, 26);
    protected static final Color BG_CARD = new Color(15, 15, 40);
    protected static final Color NEON_CYAN = new Color(0, 255, 245);
    protected static final Color TEXT_DIM = new Color(120, 120, 160);
    protected static final Color TEXT_LIGHT = new Color(200, 200, 220);
    protected static final Color ERROR_COLOR = new Color(255, 42, 109);

    protected final JTextArea inputArea;
    protected final JTextArea outputArea;
    protected final JComboBox<String> caseSelector;
    private final App app;

    public MissionPanel(App app, Color accent) {
        this.app = app;
        setLayout(new BorderLayout(8, 8));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputArea = createTextArea("Pega el input aqui...");
        outputArea = createTextArea(null);
        outputArea.setEditable(false);

        caseSelector = new JComboBox<>();
        caseSelector.setFont(new Font("Consolas", Font.BOLD, 11));
        caseSelector.setBackground(BG_CARD);
        caseSelector.setForeground(TEXT_LIGHT);
        caseSelector.setPreferredSize(new Dimension(140, 28));
        caseSelector.setVisible(false);

        JSplitPane textSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                createSection("INPUT", new JScrollPane(inputArea)),
                createSection("OUTPUT", new JScrollPane(outputArea)));
        textSplit.setDividerLocation(180);
        textSplit.setResizeWeight(0.5);
        textSplit.setOpaque(false);
        textSplit.setBorder(null);

        JPanel buttons = createButtons(accent);
        add(buttons, BorderLayout.NORTH);
        add(textSplit, BorderLayout.CENTER);
    }

    protected abstract String getSampleInput();
    protected abstract void onRun();

    private JPanel createButtons(Color accent) {
        JButton backBtn = createButton("← Volver", TEXT_DIM);
        JButton loadBtn = createButton("Cargar muestra", new Color(52, 152, 219));
        JButton runBtn = createButton("Ejecutar", accent);
        JButton clearBtn = createButton("Limpiar", TEXT_DIM);

        backBtn.addActionListener(e -> app.showMission("landing"));
        loadBtn.addActionListener(e -> inputArea.setText(getSampleInput()));
        runBtn.addActionListener(e -> onRun());
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            outputArea.setForeground(TEXT_LIGHT);
            caseSelector.removeAllItems();
            caseSelector.setVisible(false);
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.add(backBtn);
        panel.add(loadBtn);
        panel.add(runBtn);
        panel.add(clearBtn);
        panel.add(caseSelector);
        return panel;
    }

    protected void execute(java.util.function.Supplier<String> logic) {
        try {
            outputArea.setText(logic.get());
        } catch (InvalidInputException e) {
            outputArea.setForeground(ERROR_COLOR);
            outputArea.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            outputArea.setForeground(ERROR_COLOR);
            outputArea.setText("Error inesperado: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private JTextArea createTextArea(String prompt) {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Consolas", Font.PLAIN, 11));
        area.setBackground(BG_CARD);
        area.setForeground(TEXT_LIGHT);
        area.setCaretColor(NEON_CYAN);
        area.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        if (prompt != null) {
            area.putClientProperty("JTextArea.placeholderText", prompt);
        }
        return area;
    }

    private JPanel createSection(String title, JComponent content) {
        JLabel label = new JLabel(title);
        label.setForeground(TEXT_DIM);
        label.setFont(new Font("Consolas", Font.BOLD, 11));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.add(label, BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Consolas", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
