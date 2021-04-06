package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class Server extends Thread implements LoggableSocketThread {
    protected abstract void handleRequests() throws IOException;

    protected final ServerSocket serverSocket;
    protected volatile boolean isServerRunning;

    protected Socket connection;
    protected BufferedReader reader;
    protected PrintWriter writer;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        if (isValid()) {
            start();
        } else {
            logThreadCannotStart();
        }
    }

    protected boolean isValid() {
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
                connection = serverSocket.accept();
                handleConnection();
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

    protected void handleConnection() {
        try {
            openConnectionResources();
            handleRequests();
        } catch (IOException exception) {
            logThreadException(exception);
        } finally {
            closeConnectionResources();
        }
    }

    private void openConnectionResources() throws IOException {
        reader = getReaderForConnection(connection);
        writer = getWriterForConnection(connection);

        logThreadConnectionEstablished();
    }

    protected BufferedReader getReaderForConnection(Socket connection) throws IOException {
        return new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
        );
    }

    protected PrintWriter getWriterForConnection(Socket connection) throws IOException {
        return new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8),
                true
        );
    }

    private void closeConnectionResources() {
        try {
            reader.close();
            writer.close();
            connection.close();

            logThreadConnectionClosed();
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    protected void writeOutput(int responseCode, String message) throws IOException {
        writer.println(responseCode + " " + message);
    }
}