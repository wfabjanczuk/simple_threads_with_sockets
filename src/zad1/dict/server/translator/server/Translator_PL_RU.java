package zad1.dict.server.translator.server;

import java.net.ServerSocket;

public class Translator_PL_RU extends Translator {
    public static String targetLanguage = "RU";
    public static int port = 1604;

    public Translator_PL_RU(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
