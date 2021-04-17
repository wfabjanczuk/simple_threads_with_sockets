package zad1.dict.builder;

import zad1.dict.application.Application;
import zad1.dict.application.gui.SwingGui;
import zad1.dict.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ApplicationBuilder {
    public static void startMultipleLocally(int numberOfClients) {
        int firstLocalPort = 1500;

        for (int i = 0; i < numberOfClients; i++) {
            startInstanceLocally(firstLocalPort + i);
        }
    }

    public static void startInstanceLocally(int applicationPort) {
        String applicationHost = "localhost";
        String proxyHost = "localhost";
        int translatorConnectionTimeout = 5000;

        boolean isInitialized = false;
        Client client = null;

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(applicationHost, applicationPort));

            client = new Client(proxyHost, serverSocket, translatorConnectionTimeout);
            isInitialized = client.initializeIfValid();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (isInitialized) {
            Application application = new Application(new SwingGui(client));
            application.start();
        } else {
            System.out.println("Application cannot start.");
        }
    }
}
