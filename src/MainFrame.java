import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainFrame {

    private static volatile MainFrame instance;
    private static JFrame frame;
    static String root = System.getProperty("user.dir");
    static final String CONTENT_PATH = String.join(File.separator, root, "content", "cezar.png");

    public static MainFrame getInstance() {
        if (instance == null) {
            synchronized (MainFrame.class) {
                if (instance == null) {
                    instance = new MainFrame();
                    frame = new JFrame();
                }
            }
        }
        return instance;
    }

    void initialization() {
        int centerX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
        int centerY = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        frame.setLocation(centerX, centerY);
        frame.setLocation(300, 100);
//        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    void setContent(MainForm form, String title) {
        frame.setContentPane(form.getContent());
        frame.setSize(form.getContent().getPreferredSize());
        frame.setTitle(title);
        setIcon(CONTENT_PATH);
        frame.repaint();
    }

    void setIcon(String pathIcon) {
        if (Files.exists(Path.of(CONTENT_PATH))) {
//        Image image = Toolkit.getDefaultToolkit().getImage(pathIcon;
            ImageIcon image = new ImageIcon(pathIcon);
            frame.setIconImage(image.getImage());
            frame.repaint();
        }
    }
}
