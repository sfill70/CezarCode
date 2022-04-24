import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class OutputFiled extends JDialog implements MainForm {
    static String root = System.getProperty("user.dir");
    static String OUTPUT_PATH = String.join(File.separator, root, "output");
    static String INPUT_PATH = String.join(File.separator, root, "input");
    private JPanel contentPane;
    private JButton buttonCode;
    private JButton buttonOpen;
    private JTextArea textArea;
    private JButton buttonClear;
    private JButton buttonSave;
    private JButton buttonTransition;
    private JLabel label = new JLabel("file");


    public OutputFiled() {
        if (!Files.exists(Path.of(OUTPUT_PATH))) {
            OUTPUT_PATH = root;
        }
        if (!Files.exists(Path.of(INPUT_PATH))) {
            INPUT_PATH = root;
        }
        setContentPane(contentPane);
        setModal(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        buttonTransition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transitionForm();
            }
        });

        buttonCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                code();
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.onOpen(OUTPUT_PATH, textArea, label);
            }
        });

        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.onSave(OUTPUT_PATH, textArea);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void clear() {
        textArea.setText("");
    }

    private void code() {
        int shift = 0;
        String text = textArea.getText();
        shift = Code.getShift(text);
        String deCodeText = Code.deCode(shift, text);
        textArea.setText(deCodeText);
    }

    private void transitionForm() {
        MainFrame.getInstance().setContent(new InputCodeFiled(), "Окно кодирования");
    }

    @Override
    public JPanel getContent() {
        return contentPane;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
