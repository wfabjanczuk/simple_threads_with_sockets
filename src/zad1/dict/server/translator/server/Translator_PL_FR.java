package zad1.dict.server.translator.server;

import java.net.ServerSocket;

public class Translator_PL_FR extends Translator {
    public static String targetLanguage = "FR";
    public static int port = 1603;

    public Translator_PL_FR(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
