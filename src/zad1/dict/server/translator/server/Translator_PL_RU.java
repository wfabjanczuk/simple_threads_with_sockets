package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_RU;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_RU extends Translator {
    public static String targetLanguage = "RU";
    public static int port = 1604;

    public Translator_PL_RU(ServerSocket serverSocket) {
        super(serverSocket);
    }

    protected void handleConnection(Socket connection) throws IOException {
        TranslatorWorker_PL_RU translatorWorkerPlFr = new TranslatorWorker_PL_RU(connection);
        translatorWorkerPlFr.startIfValid();
    }
}
