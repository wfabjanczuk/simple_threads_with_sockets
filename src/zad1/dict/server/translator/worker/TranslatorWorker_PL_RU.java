package zad1.dict.server.translator.worker;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorWorker_PL_RU extends TranslatorWorker {
    private static final Map<String, String> dictionary = createDictionary();

    private static Map<String, String> createDictionary() {
        HashMap<String, String> dictionary = new HashMap<>();

        dictionary.put("dom", "дом");
        dictionary.put("szkoła", "школа");
        dictionary.put("nauczyciel", "учитель");
        dictionary.put("droga", "дорога");
        dictionary.put("krzesło", "стул");

        return dictionary;
    }

    @Override
    protected String getTranslation(String word) {
        return dictionary.get(word);
    }

    public TranslatorWorker_PL_RU(Socket connection) {
        super(connection);
    }
}
