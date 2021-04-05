package zad1.server;

import zad1.LoggableSocketThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class DictServerMain extends Thread implements LoggableSocketThread {
    public final static int port = 2628;

    private final ServerSocket serverSocket;
    private volatile boolean isServerRunning;
    private String initialResponsePrefix;

    private BufferedReader reader;
    private PrintWriter writer;

    public DictServerMain(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        if (isValid()) {
            prepareInitialResponsePrefix();
            start();
        } else {
            logThreadCannotStart();
        }
    }

    private void prepareInitialResponsePrefix() {
        initialResponsePrefix = "220 "
                + serverSocket.getInetAddress().getHostName()
                + " on "
                + System.getProperty("os.name")
                + " <auth.mime> ";
    }

    private boolean isValid() {
        return serverSocket != null;
    }

    public void stopServer() {
        isServerRunning = false;
    }

    public void run() {
        logThreadStarted();
        isServerRunning = true;

        while (isServerRunning) {
            try {
                Socket connection = serverSocket.accept();
                handleConnection(connection);
            } catch (IOException exception) {
                logThreadException(exception);
            }
        }

        try {
            serverSocket.close();
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    private void handleConnection(Socket connection) {
        try {
            openConnectionResources(connection);
            sendInitialResponse();
            handleInput();
        } catch (IOException exception) {
            logThreadException(exception);
        } finally {
            closeConnectionResources(connection);
        }
    }

    private void sendInitialResponse() {
        writer.println(initialResponsePrefix + generateMsgId());
    }

    private String generateMsgId() {
        return "<" + UUID.randomUUID().toString() + "@" + serverSocket.getInetAddress().getHostName() + ">";
    }

    private void openConnectionResources(Socket connection) throws IOException {
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        writer = new PrintWriter(connection.getOutputStream(), true);

        logThreadConnectionEstablished();
    }

    private void handleInput() throws IOException {
        for (String line; (line = reader.readLine()) != null; ) {
            System.out.println(line);
            writeOutput(200, "OK");
        }
    }

    private void writeOutput(int responseCode, String message) throws IOException {
        writer.println(responseCode);
        if (message != null) writer.println(message);
    }

    private void closeConnectionResources(Socket connection) {
        try {
            reader.close();
            writer.close();
            connection.close();

            logThreadConnectionClosed();
        } catch (IOException exception) {
            logThreadException(exception);
        }
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
            new DictServerMain(serverSocket);
        }
    }
}
