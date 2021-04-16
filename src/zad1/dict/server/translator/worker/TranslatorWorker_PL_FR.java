package zad1.dict.server.translator.worker;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorWorker_PL_FR extends TranslatorWorker {
    private static final Map<String, String> dictionary = createDictionary();

    private static Map<String, String> createDictionary() {
        HashMap<String, String> dictionary = new HashMap<>();

        dictionary.put("dom", "maison");
        dictionary.put("szkoła", "l'école");
        dictionary.put("nauczyciel", "professeur");
        dictionary.put("droga", "route");
        dictionary.put("krzesło", "chaise");

        return dictionary;
    }

    @Override
    protected String getTranslation(String word) {
        return dictionary.get(word);
    }

    public TranslatorWorker_PL_FR(Socket connection) {
        super(connection);
    }
}
