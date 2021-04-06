package zad1.dict.server.translator;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorServer_PL_RU extends TranslatorServer {
    public static String targetLanguage = "RU";
    public static int port = 1604;
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

    public TranslatorServer_PL_RU(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
