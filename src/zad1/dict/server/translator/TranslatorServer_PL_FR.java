package zad1.dict.server.translator;

import java.net.ServerSocket;

public class TranslatorServer_PL_FR extends TranslatorServer {
    public static String targetLanguage = "FR";
    public static int port = 1603;

    public TranslatorServer_PL_FR(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return null;
    }
}
