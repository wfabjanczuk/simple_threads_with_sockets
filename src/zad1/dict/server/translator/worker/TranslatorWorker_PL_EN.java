package zad1.dict.server.translator.worker;

import zad1.dict.server.translator.TranslatorWorker;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorWorker_PL_EN extends TranslatorWorker {
    private static final Map<String, String> dictionary = createDictionary();

    private static Map<String, String> createDictionary() {
        HashMap<String, String> dictionary = new HashMap<>();

        dictionary.put("dom", "house");
        dictionary.put("szkoła", "school");
        dictionary.put("nauczyciel", "teacher");
        dictionary.put("droga", "road");
        dictionary.put("krzesło", "chair");

        return dictionary;
    }

    @Override
    protected String getTranslation(String word) {
        return dictionary.get(word);
    }

    public TranslatorWorker_PL_EN(Socket connection) {
        super(connection);
    }
}
