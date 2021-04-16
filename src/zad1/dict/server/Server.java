package zad1.dict.server;

import zad1.dict.LoggableSocketThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server extends Thread implements LoggableSocketThread {
    protected abstract void handleConnection(Socket connection);

    protected final ServerSocket serverSocket;
    protected volatile boolean isServerRunning;
    protected Socket connection;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startIfValid() {
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

    public static void startLocally(Class<? extends Server> serverClass, int port) {
        try {
            Class[] constructorArguments = new Class[]{ServerSocket.class};

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", port));

            Server server = serverClass.getDeclaredConstructor(constructorArguments).newInstance(serverSocket);
            server.startIfValid();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}