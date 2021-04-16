package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_EN;

import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_EN extends Translator {
    public static String targetLanguage = "EN";
    public static int port = 1601;

    public Translator_PL_EN(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) {
        TranslatorWorker_PL_EN translatorWorkerPlEn = new TranslatorWorker_PL_EN(connection);
        translatorWorkerPlEn.startIfValid();
    }
}
