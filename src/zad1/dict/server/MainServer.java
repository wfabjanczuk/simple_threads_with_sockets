package zad1.dict.server;

import zad1.dict.LoggableSocketThread;
import zad1.dict.server.parser.ParseResult;
import zad1.dict.server.parser.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class MainServer extends Thread implements LoggableSocketThread {
    public final static int port = 2628;

    private final ServerSocket serverSocket;
    private volatile boolean isServerRunning;
    private String initialResponsePrefix;

    private BufferedReader reader;
    private PrintWriter writer;

    public MainServer(ServerSocket serverSocket) {
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
            handleRequests();
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

    private void handleRequests() throws IOException {
        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText(line);
            ParseResult parseResult = RequestParser.parseRequest(line);
            handleParsedRequest(parseResult);
        }
    }

    private void handleParsedRequest(ParseResult parseResult) throws IOException {
        if (!parseResult.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        writeOutput(200, "Success");
    }

    private void writeOutput(int responseCode, String message) throws IOException {
        writer.println(responseCode + " " + message);
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
            new MainServer(serverSocket);
        }
    }
}
