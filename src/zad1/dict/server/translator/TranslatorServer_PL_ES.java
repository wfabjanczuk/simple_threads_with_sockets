package zad1.dict.server.translator;

import java.net.ServerSocket;

public class TranslatorServer_PL_ES extends TranslatorServer {
    public static String targetLanguage = "ES";
    public static int port = 1602;

    public TranslatorServer_PL_ES(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return null;
    }
}
