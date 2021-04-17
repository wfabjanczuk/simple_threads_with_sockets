package zad1.dict.application;

import zad1.dict.application.gui.SwingGui;
import zad1.dict.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Application extends SwingGui {
    private final Client client;

    public Application(Client client) {
        this.client = client;
        initialize();
    }

    public void setWindowTitle() {
        setTitle(client.getThreadLabel() + " on port " + client.getLocalPort());
        pack();
        if (client.getLocalPort() > 1500) {
            // TODO: replace it, now it is for testing only
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    public String getTranslation(String word, String targetLanguage) {
        try {
            return client.getTranslation(word, targetLanguage);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.closeResources();
    }

    public static void startLocally(int numberOfClients) {
        int firstLocalPort = 1500;

        for (int i = 0; i < numberOfClients; i++) {
            startInstanceLocally(firstLocalPort + i);
        }
    }

    public static void startInstanceLocally(int applicationPort) {
        String applicationHost = "localhost";
        String proxyHost = "localhost";
        int translatorConnectionTimeout = 1000;

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
            new Application(client);
        } else {
            System.out.println("Application cannot start.");
        }
    }

}