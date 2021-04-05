package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DictServerMain extends Thread {
    public final static int port = 2628;

    private int serverThreadId;
    private ServerSocket serverSocket;
    private volatile boolean isServerRunning;

    public DictServerMain(int serverThreadId, ServerSocket serverSocket) {
        this.serverThreadId = serverThreadId;
        this.serverSocket = serverSocket;

        if (isValid()) {
            start();
        } else {
            signalThreadCannotStart();
        }
    }

    private boolean isValid() {
        return serverSocket != null;
    }

    private void signalThreadStarted() {
        System.out.println(getInstanceLabel() + " started.");
    }

    private void signalThreadCannotStart() {
        System.out.println(getInstanceLabel());
        System.out.println("Thread cannot start. Constructor provided with invalid arguments.");
    }

    private void signalThreadException(Exception exception) {
        System.out.println(getInstanceLabel());
        exception.printStackTrace();
    }

    private String getInstanceLabel() {
        return "DictServerMain" + serverThreadId + ":";
    }

    public void run() {
        signalThreadStarted();
        isServerRunning = true;

        while (isServerRunning) {
            try {
                Socket connection = serverSocket.accept();
                System.out.println("Connection established by DictServerMain " + serverThreadId);
            } catch (IOException exception) {
                signalThreadException(exception);
            }
        }

        try {
            serverSocket.close();
        } catch (IOException exception) {
            signalThreadException(exception);
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
            new DictServerMain(i, serverSocket);
        }
    }
}
