package zad1.dict.server.translator.worker;

import zad1.dict.server.translator.server.Translator;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorWorker_PL_ES extends Translator {
    private static final Map<String, String> dictionary = createDictionary();

    private static Map<String, String> createDictionary() {
        HashMap<String, String> dictionary = new HashMap<>();

        dictionary.put("dom", "casa");
        dictionary.put("szkoła", "escuela");
        dictionary.put("nauczyciel", "profesor");
        dictionary.put("droga", "camino");
        dictionary.put("krzesło", "silla");

        return dictionary;
    }

    @Override
    protected String getTranslation(String word) {
        return dictionary.get(word);
    }

    public TranslatorWorker_PL_ES(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
