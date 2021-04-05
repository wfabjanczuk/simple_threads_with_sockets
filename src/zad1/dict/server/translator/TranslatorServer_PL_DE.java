package zad1.dict.server.translator;

import java.net.ServerSocket;

public class TranslatorServer_PL_DE extends TranslatorServer {
    public static String targetLanguage = "DE";
    public static int port = 1600;

    public TranslatorServer_PL_DE(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return null;
    }
}
