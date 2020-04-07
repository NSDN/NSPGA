package cn.ac.nya.nspga.flex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public class NSPGAEditor extends JFrame {

    private static final String CMD_OK = "OK";
    private static final String CMD_APPLY = "APPLY";

    private static final String DEFAULT_PREFIX = "dev.";

    private static final Font FONT = getFont("Monospaced.plain", 14.0F);

    @FunctionalInterface
    public interface ICallback {
        void invoke(String[] inputs, String[] outputs, String code);
    }

    private ICallback callback;

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }

    private JPanel basePanel;
    private JButton btnOK;

    private JTabbedPane tabbedPane;
    private JTextField portPrefix;
    private JButton btnApply;
    private JPanel portPanel, codingPanel;

    private JTextArea codeArea;
    private KeyListener codeListener;

    private final int inputCount, outputCount;
    private ArrayList<JTextField> inputs = new ArrayList<>();
    private ArrayList<JTextField> outputs = new ArrayList<>();

    public NSPGAEditor(int inputCount, int outputCount, String code) {
        super("NSPGA Editor");

        this.callback = null;
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        init();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(640,480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        codeArea.setText(code);
    }

    public NSPGAEditor(int inputCount, int outputCount) {
        this(inputCount, outputCount, "");
    }

    public NSPGAEditor(String[] inputs, String[] outputs, String code) {
        this(inputs.length, outputs.length, code);

        for (int i = 0; i < inputCount; i++)
            this.inputs.get(i).setText(inputs[i]);
        for (int i = 0; i < outputCount; i++)
            this.outputs.get(i).setText(outputs[i]);
    }

    private static Font getFont(String name, float size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (Font f : ge.getAllFonts())
            if (f.getName().equalsIgnoreCase(name))
                return f.deriveFont(size);
        for (Font f : ge.getAllFonts())
            if (f.getName().equalsIgnoreCase("Arial"))
                return f.deriveFont(size);
        return null;
    }

    private void init() {
        basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout(4, 4));
        basePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        tabbedPane = new JTabbedPane();
        portPanel = new JPanel();
        codingPanel = new JPanel();
        portPanel.setLayout(new BorderLayout());
        codingPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Port Config", portPanel);
        tabbedPane.addTab("Logic Code", codingPanel);

        JPanel ports = new JPanel();
        ports.setLayout(new GridLayout(1, 2, 4, 4));
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        for (int i = 0; i < inputCount; i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH; c.gridy = i;
            c.gridx = 0; c.weightx = 0;
            JLabel label = new JLabel("Input # " + i);
            panel.add(label, c);
            c.gridx = 1; c.weightx = 0;
            panel.add(new JLabel("        "), c);
            JTextField field = new JTextField(DEFAULT_PREFIX + "in." + i);
            field.setFont(FONT);
            c.gridx = 2; c.weightx = 1;
            panel.add(field, c);
            inputs.add(field);
        }
        JScrollPane pane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ports.add(pane);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        for (int i = 0; i < outputCount; i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH; c.gridy = i;
            c.gridx = 0; c.weightx = 0;
            JLabel label = new JLabel("Output # " + i);
            panel.add(label, c);
            c.gridx = 1; c.weightx = 0;
            panel.add(new JLabel("        "), c);
            JTextField field = new JTextField(DEFAULT_PREFIX + "out." + i);
            field.setFont(FONT);
            c.gridx = 2; c.weightx = 1;
            panel.add(field, c);
            outputs.add(field);
        }
        pane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ports.add(pane);
        portPanel.add(ports, BorderLayout.CENTER);

        JPanel globalCfg = new JPanel();
        globalCfg.setLayout(new GridLayout(1, 3, 4, 4));
        globalCfg.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        globalCfg.add(new JLabel("Global IO Prefix"));
        portPrefix = new JTextField("foo.bar." + DEFAULT_PREFIX);
        portPrefix.setFont(FONT);
        globalCfg.add(portPrefix);
        btnApply = new JButton("Apply");
        btnApply.setActionCommand(CMD_APPLY);
        btnApply.addActionListener((e) -> {
            if (e.getActionCommand().equals(CMD_APPLY)) {
                String prefix = portPrefix.getText();
                if (!prefix.endsWith(DEFAULT_PREFIX)) {
                    portPrefix.setText("[REPLACE]." + DEFAULT_PREFIX);
                    return;
                }
                for (JTextField i : inputs) {
                    String s = i.getText();
                    if (!s.contains(DEFAULT_PREFIX))
                        continue;
                    s = s.substring(s.indexOf(DEFAULT_PREFIX) + DEFAULT_PREFIX.length());
                    i.setText(prefix + s);
                }
                for (JTextField i : outputs) {
                    String s = i.getText();
                    if (!s.contains(DEFAULT_PREFIX))
                        continue;
                    s = s.substring(s.indexOf(DEFAULT_PREFIX) + DEFAULT_PREFIX.length());
                    i.setText(prefix + s);
                }
            }
        });
        globalCfg.add(btnApply);
        portPanel.add(globalCfg, BorderLayout.NORTH);

        codeArea = new JTextArea();
        codeArea.setFont(FONT);
        codeArea.setWrapStyleWord(true);
        codeArea.setLineWrap(true);
        codeArea.setTabSize(4);
        pane = new JScrollPane(codeArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setAutoscrolls(true);
        codingPanel.add(pane, BorderLayout.CENTER);

        basePanel.add(tabbedPane, BorderLayout.CENTER);
        btnOK = new JButton("OK");
        btnOK.setActionCommand(CMD_OK);
        basePanel.add(btnOK, BorderLayout.SOUTH);

        this.add(basePanel);

        codeListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int pos = codeArea.getCaretPosition();
                switch (e.getKeyChar()) {
                    case '\t':
                        codeArea.replaceRange("    ", pos - 1, pos);
                        codeArea.setCaretPosition(pos + 3);
                        break;
                    case '\n':
                        int start, end;
                        for (start = pos - 2; start >= 0; start--) {
                            if (codeArea.getText().charAt(start) == '\n')
                                break;
                        }
                        for (end = start + 1; end < pos; end++) {
                            if (codeArea.getText().charAt(end) != ' ')
                                break;
                        }
                        String indent = "";
                        for (int k = 0; k < end - start - 1; k++)
                            indent = indent.concat(" ");
                        codeArea.insert(indent, pos);
                        break;
                    case '(': codeArea.insert(")", pos); codeArea.setCaretPosition(pos); break;
                    case '<': codeArea.insert(">", pos); codeArea.setCaretPosition(pos); break;
                    case '{': codeArea.insert("}", pos); codeArea.setCaretPosition(pos); break;
                    case '[': codeArea.insert("]", pos); codeArea.setCaretPosition(pos); break;
                    case '\'': codeArea.insert("\'", pos); codeArea.setCaretPosition(pos); break;
                    case '\"': codeArea.insert("\"", pos); codeArea.setCaretPosition(pos); break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
        tabbedPane.addChangeListener(e -> {
            codeArea.removeKeyListener(codeListener);
            if (codingPanel.isShowing())
                codeArea.addKeyListener(codeListener);
        });

        btnOK.addActionListener(e -> {
            if (e.getActionCommand().equals(CMD_OK))
                close();
        });
    }

    private void close() {
        if (callback != null) {
            String[] i = new String[inputCount];
            String[] o = new String[outputCount];
            for (int c = 0; c < inputCount; c++)
                i[c] = inputs.get(c).getText();
            for (int c = 0; c < outputCount; c++)
                o[c] = outputs.get(c).getText();
            callback.invoke(i, o, codeArea.getText());
        }

        this.dispose();
    }

}
