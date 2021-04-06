package zad1.dict.server.translator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class TranslatorServer_PL_EN extends TranslatorServer {
    public static String targetLanguage = "EN";
    public static int port = 1601;

    public TranslatorServer_PL_EN(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override
    protected String getTranslation(String word) {
        return "house";
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

        for (int i = 1; i <= 5; i++) {
            new TranslatorServer_PL_EN(serverSocket);
        }
    }
}
