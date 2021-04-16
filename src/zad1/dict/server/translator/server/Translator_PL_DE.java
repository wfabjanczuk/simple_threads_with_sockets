package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_DE;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_DE extends Translator {
    public static String targetLanguage = "DE";
    public static int port = 1600;

    public Translator_PL_DE(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) throws IOException {
        TranslatorWorker_PL_DE translatorWorkerPlDe = new TranslatorWorker_PL_DE(connection);
        translatorWorkerPlDe.startIfValid();
    }
}