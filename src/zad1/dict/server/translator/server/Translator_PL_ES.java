package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_ES;

import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_ES extends Translator {
    public static String targetLanguage = "ES";
    public static int port = 1602;

    public Translator_PL_ES(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) {
        TranslatorWorker_PL_ES translatorWorkerPlEs = new TranslatorWorker_PL_ES(connection);
        translatorWorkerPlEs.startIfValid();
    }
}
