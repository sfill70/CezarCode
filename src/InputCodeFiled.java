import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class InputCodeFiled extends JDialog implements MainForm {
    static String root = System.getProperty("user.dir");
    static String OUTPUT_PATH = String.join(File.separator, root, "output");
    static String INPUT_PATH = String.join(File.separator, root, "input");
    private JPanel contentPane;
    private JButton buttonClear;
    private JButton buttonCode;
    private JTextArea textArea;
    private JButton buttonOpen;
    private JButton buttonSave;
    private JButton buttonTransition;
    private JTextField textFieldNumber;
    private JLabel label = new JLabel("file");


    public InputCodeFiled() {
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
            //можно на лямбдами написать.
            public void actionPerformed(ActionEvent e) {
                transitionForm();
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.onOpen(INPUT_PATH, textArea, label);
            }
        });

        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.onSave(OUTPUT_PATH, textArea);
            }
        });


        buttonCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                code();
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

    private void transitionForm() {
        MainFrame.getInstance().setContent(new OutputFiled(), "Окно декодирования");
    }

    private void code() {
        String text = textArea.getText();
        int shift = 0;
        boolean isCode = true;
        try {
            shift = Integer.parseInt(textFieldNumber.getText());
            if (shift <= 0 || shift % 71 == 0) {
                Util.showMessageWrongInput(contentPane, "Число должно быть больше 0 и не кратно 71", "Ошибка ввода");
                isCode = false;
            }
        } catch (NumberFormatException ex) {
            Util.showMessageWrongInput(contentPane, "Введите целое число", "Ошибка ввода");
        }
        if (isCode) {
            String textCode = Code.code(shift, text);
            textArea.setText(textCode);
        }
    }

    private void clear() {
        textArea.setText("");
        textFieldNumber.setText("");
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public JPanel getContent() {
        return contentPane;
    }
}
