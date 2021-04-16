package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_FR;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_FR extends Translator {
    public static String targetLanguage = "FR";
    public static int port = 1603;

    public Translator_PL_FR(ServerSocket serverSocket) {
        super(serverSocket);
    }

    protected void handleConnection(Socket connection) throws IOException {
        TranslatorWorker_PL_FR translatorWorkerPlFr = new TranslatorWorker_PL_FR(connection);
        translatorWorkerPlFr.startIfValid();
    }
}
