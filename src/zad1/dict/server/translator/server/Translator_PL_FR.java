package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_FR;

import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_FR extends Translator {
    public static String targetLanguage = "FR";

    public Translator_PL_FR(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) {
        TranslatorWorker_PL_FR translatorWorkerPlFr = new TranslatorWorker_PL_FR(connection);
        translatorWorkerPlFr.startIfValid();
    }
}
