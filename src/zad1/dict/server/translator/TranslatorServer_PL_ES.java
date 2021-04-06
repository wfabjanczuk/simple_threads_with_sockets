package zad1.dict.server.translator;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorServer_PL_ES extends TranslatorServer {
    public static String targetLanguage = "ES";
    public static int port = 1602;
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

    public TranslatorServer_PL_ES(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
