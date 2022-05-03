import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) {

        try {
            Util.createDirectory();
        } catch (IOException e) {
            Util.showMessageWrongInput(new JOptionPane(), "Директории, ввода, вывода не могут быть созданы", "Ошибка");
        }
        ViewForms viewForms = new ViewForms();
        viewForms.run();

    }

}
