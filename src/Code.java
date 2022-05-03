
public class Code {

    //length = 71
    static private final String ALL_SYMBOL = " !?\"',-.АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
            "абвгдеёжзиклмнопрстуфхцчшщъыьэя";


    public static String code(int shift, String text) {
        char[] textArray = text.toCharArray();
        for (int i = 0; i < textArray.length; i++) {
            char symbol = textArray[i];
            int indexIn = ALL_SYMBOL.indexOf(symbol);
            if (indexIn < 0) {
                continue;
            }
            int indexOut = (indexIn + shift) % ALL_SYMBOL.length();
            textArray[i] = ALL_SYMBOL.charAt(indexOut);
        }
        return new String(textArray);
    }

//    Ненужный метод, аналог предыдущего, но сдвигает символы в обратном направлении
        // Используется не стирать !!!!
        public static String deCode(int shift, String text) {
        char[] textArray = text.toCharArray();
        for (int i = 0; i < textArray.length; i++) {
            char symbol = textArray[i];
            int indexIn = ALL_SYMBOL.indexOf(symbol);
            if (indexIn < 0) {
                continue;
            }
            int indexOut = Math.abs(ALL_SYMBOL.length() + indexIn - shift) % ALL_SYMBOL.length();
            textArray[i] = ALL_SYMBOL.charAt(indexOut);
        }
        return new String(textArray);
    }

    //Определяет на сколько символов сдвигался текст, нужен для дешифровки
    public static int getShift(String codeText) {
        int shift = 0;
        int max = 0;
        for (int i = 1; i < ALL_SYMBOL.length() + 1; i++) {
            String text = deCode(i, codeText);
//            Подсчет по "запятая пробел" + "точка прбел" можно использовать совместносо следующим способом.
//            int countWorld = text.length() - text.replaceAll(",\\s", "").replaceAll("\\.\\s","").length();
            //Считаем пробелы то же работает.
            int countWorld = text.length() - text.replaceAll("\\s", "").length();
            if (countWorld > max) {
                max = countWorld;
                shift = i;
            }
        }
        return shift;
    }
}
