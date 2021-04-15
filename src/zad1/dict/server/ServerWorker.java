package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ServerWorker extends Thread implements LoggableSocketThread {
    protected abstract void handleRequests() throws IOException;

    protected Socket connection;
    protected BufferedReader reader;
    protected PrintWriter writer;

    public ServerWorker(Socket connection) {
        this.connection = connection;
    }

    public void startIfValid() {
        if (isValid()) {
            start();
        } else {
            logThreadCannotStart();
        }
    }

    protected boolean isValid() {
        return connection != null;
    }

    public void run() {
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

    private void closeConnectionResources() {
        try {
            connection.close();
            logThreadConnectionClosed();
        } catch (IOException exception) {
            logThreadException(exception);
        }
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

    protected void writeOutput(int responseCode, String message) throws IOException {
        writer.println(responseCode + " " + message);
    }
}
