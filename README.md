# CezarCode
Код Цезаря, шифрует текстовый файл пудем сдвига символов на заданное число.
Расшифровка, брудфорс по наибольшему количеству пробелов в тексте, можно сочетание "побел запятая" + пробел точка".
Писал на Java 15.0.2 на Java 17.0.* запустилась не везде.
Для графического интерфейса использовал Swing для создания форм использовал встроеный в IDEA конструктор GUI.
Данные из кодировки можно вводить в окно программы или загружать из файла.
По умолчанию директория файлов для кодирования input (в папке проекта) фильтр *.txt или все файлы, кодированые файлы сохраняются 
в output (в папке проекта) с расширением *.txt (можно убрать). Для декодирования фалов по умолчанию директория output (в папке проекта) 
с расширением *.txt (можно убрать).
Автоматически распознает кодировки файлов: "UTF-8", "windows-1251", "KOI8-R" "UTF-16LE", "UTF-16BE".
Работает только с русским алфавитом.
