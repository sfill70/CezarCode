import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Util {

    static private final String LOWERCASE_SYMBOL = "АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    static String root = System.getProperty("user.dir");
    private static final String OUTPUT_PATH = String.join(File.separator, root, "output");
    private static final String INPUT_PATH = String.join(File.separator, root, "input");

    public static void createDirectory() throws IOException {
        Path out = Path.of(OUTPUT_PATH);
        if (!Files.exists(out)) {
            Files.createDirectory(out);
        }
        Path in = Path.of(INPUT_PATH);
        if (!Files.exists(in)) {
            Files.createDirectory(in);
        }
    }


    public static void onOpen(String path, JTextArea textArea, JLabel label) {
        JFileChooser fileOpen = new JFileChooser();
        fileOpen.setCurrentDirectory(new File(path));
        fileOpen.setDialogTitle("Укажите файл для чтения");
        fileOpen.setFileFilter(getFileFilter());
        int ret = fileOpen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileOpen.getSelectedFile();
            //Автоматически определяет кодировки "UTF-8", "windows-12**", "KOI-R", "UTF-16BE", "UTF-16LE"
            // проверялась только на русских текстах, CharsetDetector не определяет какой "windows-12**",
//            говнокод с вложенным try catch подумать
            String text = getString(textArea, file);
            textArea.setText(text);
            label.setText(file.getAbsolutePath());
        }
    }

    private static String getString(JTextArea textArea, File file) {
        String[] charsetsToBeTested = {"UTF-8", "windows-1251"};
        CharsetDetector cd = new CharsetDetector();
        Charset charset = cd.detectCharset(file, charsetsToBeTested);
//        System.out.println(charset);
        String text = "";
        try {
            text = Files.readString(Path.of(file.getAbsolutePath()), charset);
            if (!charset.name().equals("UTF-8")) {
                int count = 0;
                for (int i = 0; i < 400; i++) {
                    if (LOWERCASE_SYMBOL.contains(text.substring(i, i + 1))) {
                        count++;
                    }
                }
                if (count >= 200) {
                    text = Files.readString(Path.of(file.getAbsolutePath()), Charset.forName("KOI8-R"));
                }
            }

        } catch (IOException ex) {
            String charsetAnother = getCharset(file.getAbsolutePath());
//            System.out.println(charsetAnother);
            try {
                text = Files.readString(Path.of(file.getAbsolutePath()), Charset.forName(charsetAnother));
            } catch (IOException e) {
                showMessageWrongInput(textArea, "Выберите другой файл", "Ошибка чтения файла");
            }
        }
        return text;
    }

    private static FileFilter getFileFilter() {
        return new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text documents (*.txt)";
            }
        };
    }

    static public void onSave(String path, JTextArea textArea) {
        while (true) {
            JFrame parentFrame = new JFrame();
            JFileChooser fileSave = new JFileChooser();
            fileSave.setFileFilter(getFileFilter());
            fileSave.setCurrentDirectory(new File(path));
            fileSave.setDialogTitle("Укажите файл для сохранения");
            int userSelection = fileSave.showSaveDialog(parentFrame);
            if (userSelection == JFileChooser.CANCEL_OPTION) {
                return;
            }
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileSave.getSelectedFile();
                if (!fileSave.accept(fileToSave)) {
                    fileToSave = new File(fileSave.getSelectedFile() + ".txt");
                }
                if (fileToSave.exists()) {
                    if (showMessageConfirmation(fileSave)) {
                        continue;
                    }
                }
                try {
                    if (!fileToSave.exists()) {
                        Files.createFile(Path.of(fileToSave.getAbsolutePath()));
                    }
                    writer(fileSave, fileToSave.getAbsolutePath(), textArea.getText());
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    static private boolean showMessageConfirmation(JComponent jComponent) {
        int result = JOptionPane.showConfirmDialog(jComponent, "Файл с таким именем существует, перезаписать?");
        if (result == 0) {
            return false;
        } else if (result == 1) {
            return true;
        } else {
            return true;
        }
    }

    static public void showMessageWrongInput(JComponent jComponent, String message, String title) {
        JOptionPane.showMessageDialog(
                jComponent,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    static private void writer(JFileChooser fileChooser, String path, String text) {
        try (FileWriter writer = new FileWriter(path, Charset.forName(String.valueOf(StandardCharsets.UTF_8)), false)) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            showMessageWrongInput(fileChooser, "No saved to file", "Error");
        }
    }

    public static String getCharset(String path) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0); // Примечание для читателя: bis.mark (0); Изменено на bis.mark (100); я использовал этот код и мне нужно изменить место, отмеченное выше.
            // Wagsn Примечание: Однако пока это нормально и не изменится.
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // кодировка файла ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // кодировка файла Unicode
//                charset = "Cp1251"; // кодировка файла Win ru
//                charset = "windows-1251"; // кодировка файла Win ru
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // кодировка файла с прямым порядком байтов в Юникоде
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // Файл закодирован как UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // Если он отображается только под BF, его также можно рассматривать как GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // двухбайтовый (0xC0-0xDF)
                        // (0x80-0xBF), возможно также в коде ГБ.  Странный if() но трогать не буду
                        {
                            continue;
                        } else {
                            break;
                        }
                    } else if (0xE0 <= read) {// ошибка может возникнуть, но вероятность невелика
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("-File-> [" + path + "] использует набор символов: [" + charset + "]");
        return charset;
    }
}
