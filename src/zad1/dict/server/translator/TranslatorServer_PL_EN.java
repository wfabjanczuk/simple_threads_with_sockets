package zad1.dict.server.translator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TranslatorServer_PL_EN extends TranslatorServer {
    public static String targetLanguage = "EN";
    public static int port = 1601;
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

    public TranslatorServer_PL_EN(ServerSocket serverSocket) {
        super(serverSocket);
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        String host = "localhost";

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        for (int i = 1; i <= 5; i++) {
            new TranslatorServer_PL_EN(serverSocket);
        }
    }
}
