package zad1;

import zad1.dict.client.Client;
import zad1.dict.gui.Gui;
import zad1.dict.gui.SwingGui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class GuiLocalRunner {
    public static void main(String[] args) {
        startLocally(Integer.parseInt(args[0]));
    }

    private static void startLocally(int applicationPort) {
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
            Gui gui = new SwingGui();
            gui.setClient(client);
            gui.initialize();
        } else {
            System.out.println("Gui cannot start.");
        }
    }
}
