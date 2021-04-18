package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ServerWorker extends Thread implements LoggableSocketThread {
    protected abstract void handleRequests() throws IOException;

    protected Socket defaultConnection;
    protected BufferedReader defaultBufferedReader;
    protected PrintWriter defaultPrintWriter;

    public ServerWorker(Socket defaultConnection) {
        this.defaultConnection = defaultConnection;
    }

    public void startIfValid() {
        if (isValid()) {
            start();
        } else {
            logThreadCannotStart();
        }
    }

    protected boolean isValid() {
        return defaultConnection != null;
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
        defaultBufferedReader = getReaderForConnection(defaultConnection);
        defaultPrintWriter = getWriterForConnection(defaultConnection);

        logThreadConnectionResourcesOpened();
    }

    private void closeConnectionResources() {
        try {
            if (defaultConnection != null) {
                defaultConnection.close();
            }

            logThreadConnectionResourcesClosed();
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
        defaultPrintWriter.println(responseCode + " " + message);
    }
}
