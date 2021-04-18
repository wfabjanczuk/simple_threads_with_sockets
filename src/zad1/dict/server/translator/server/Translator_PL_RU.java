package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_RU;

import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_RU extends Translator {
    public static String targetLanguage = "RU";

    public Translator_PL_RU(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) {
        TranslatorWorker_PL_RU translatorWorkerPlRu = new TranslatorWorker_PL_RU(connection);
        translatorWorkerPlRu.startIfValid();
    }
}
