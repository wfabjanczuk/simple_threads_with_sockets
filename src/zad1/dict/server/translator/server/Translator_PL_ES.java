package zad1.dict.server.translator.server;

import java.net.ServerSocket;

public class Translator_PL_ES extends Translator {
    public static String targetLanguage = "ES";
    public static int port = 1602;

    public Translator_PL_ES(ServerSocket serverSocket) {
        super(serverSocket);
    }
}
