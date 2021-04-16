package zad1.dict.server.translator.worker;

import zad1.dict.LoggableSocketThread;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorWorker_PL_DE extends TranslatorWorker implements LoggableSocketThread {
    private static final Map<String, String> dictionary = createDictionary();

    private static Map<String, String> createDictionary() {
        HashMap<String, String> dictionary = new HashMap<>();

        dictionary.put("dom", "Haus");
        dictionary.put("szkoła", "Schule");
        dictionary.put("nauczyciel", "Lehrer");
        dictionary.put("droga", "Weg");
        dictionary.put("krzesło", "Stuhl");

        return dictionary;
    }

    @Override
    protected String getTranslation(String word) {
        return dictionary.get(word);
    }

    public TranslatorWorker_PL_DE(Socket connection) {
        super(connection);
    }
}
