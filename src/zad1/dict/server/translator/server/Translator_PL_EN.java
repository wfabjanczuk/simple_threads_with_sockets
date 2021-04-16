package zad1.dict.server.translator.server;

import zad1.dict.server.translator.worker.TranslatorWorker_PL_EN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Translator_PL_EN extends Translator {
    public static String targetLanguage = "EN";
    public static int port = 1601;

    public Translator_PL_EN(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected void handleConnection(Socket connection) throws IOException {
        TranslatorWorker_PL_EN translatorWorkerPlEn = new TranslatorWorker_PL_EN(connection);
        translatorWorkerPlEn.startIfValid();
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        String host = "localhost";

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        new Translator_PL_EN(serverSocket);
    }
}
