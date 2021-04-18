package zad1;

import zad1.dict.client.Client;
import zad1.dict.gui.Gui;
import zad1.dict.gui.JavaFxGui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class GuiLocalRunner {
    public static void main(String[] args) {
        int clientPort = Integer.parseInt(args[0]);
        int proxyPort = Integer.parseInt(args[1]);

        startLocally(clientPort, proxyPort);
    }

    private static void startLocally(int clientPort, int proxyPort) {
        String applicationHost = "localhost";
        String proxyHost = "localhost";
        int translatorConnectionTimeout = 1000;

        boolean isInitialized = false;
        Client client = null;

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(applicationHost, clientPort));

            client = new Client(proxyHost, proxyPort, serverSocket, translatorConnectionTimeout);
            isInitialized = client.initializeIfValid();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (isInitialized) {
            Gui gui = new JavaFxGui();
            gui.setClient(client);
            gui.initialize();
        } else {
            System.out.println("Gui cannot start.");
        }
    }
}
