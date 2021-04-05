package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server extends Thread implements LoggableSocketThread {
    protected abstract void handleRequests() throws IOException;

    protected final ServerSocket serverSocket;
    protected volatile boolean isServerRunning;

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

    protected void handleConnection(Socket connection) {
        try {
            openConnectionResources(connection);
            handleRequests();
        } catch (IOException exception) {
            logThreadException(exception);
        } finally {
            closeConnectionResources(connection);
        }
    }

    private void openConnectionResources(Socket connection) throws IOException {
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        writer = new PrintWriter(connection.getOutputStream(), true);

        logThreadConnectionEstablished();
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
}