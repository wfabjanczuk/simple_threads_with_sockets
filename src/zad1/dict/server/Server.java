package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server extends Thread implements LoggableSocketThread {
    protected abstract void handleConnection(Socket connection) throws IOException;

    protected final ServerSocket serverSocket;
    protected volatile boolean isServerRunning;
    protected Socket connection;

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
                Socket connection = serverSocket.accept();
                logThreadConnectionEstablished();
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
}