package zad1.dict.server.translator;

import java.net.ServerSocket;

public class TranslatorServer_PL_RU extends TranslatorServer {
    public static String targetLanguage = "RU";
    public static int port = 1604;

    public TranslatorServer_PL_RU(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return null;
    }
}
