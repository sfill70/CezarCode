import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class ChoiceFiled extends JDialog implements MainForm {

    private JPanel contentPane;
    private JButton buttonCode;
    private JButton buttonDeCode;
    MainForm inputCodeFiled = new InputCodeFiled();
    MainForm outputFiled = new OutputFiled();

    public ChoiceFiled() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCode);

        buttonCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().setContent(inputCodeFiled, "Окно кодирования");
            }
        });

        buttonDeCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.getInstance().setContent(outputFiled, "Окно декодирования");
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
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

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    @Override
    public JPanel getContent() {
        return contentPane;
    }

    public static void main(String[] args) {
        ChoiceFiled dialog = new ChoiceFiled();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
