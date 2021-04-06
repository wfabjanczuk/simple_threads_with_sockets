package zad1.dict.server.translator;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorServer_PL_DE extends TranslatorServer {
    public static String targetLanguage = "DE";
    public static int port = 1600;
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

    public TranslatorServer_PL_DE(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
