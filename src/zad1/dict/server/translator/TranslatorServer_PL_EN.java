package zad1.dict.server.translator;

import java.net.ServerSocket;

public class TranslatorServer_PL_EN extends TranslatorServer {
    public static String targetLanguage = "EN";
    public static int port = 1601;

    public TranslatorServer_PL_EN(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return null;
    }
}
