package zad1.dict.server.translator;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorServer_PL_FR extends TranslatorServer {
    public static String targetLanguage = "FR";
    public static int port = 1603;
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

    public TranslatorServer_PL_FR(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
