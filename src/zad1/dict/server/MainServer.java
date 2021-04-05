package zad1.dict.server;

import zad1.dict.server.parser.ParseResult;
import zad1.dict.server.parser.RequestParser;
import zad1.dict.server.translator.TranslatorRouteTable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class MainServer extends Server {
    public final static int port = 2628;
    private String initialResponsePrefix;

    public MainServer(ServerSocket serverSocket) {
        super(serverSocket);
    }

    protected void handleRequests() throws IOException {
        sendInitialResponse();

        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText(line);
            ParseResult parseResult = RequestParser.parseRequest(line);
            handleParsedRequest(parseResult);
        }
    }

    public void run() {
        prepareInitialResponsePrefix();
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

    private void prepareInitialResponsePrefix() {
        initialResponsePrefix = "220 "
                + serverSocket.getInetAddress().getHostName()
                + " on "
                + System.getProperty("os.name")
                + " <auth.mime> ";
    }

    private void sendInitialResponse() {
        writer.println(initialResponsePrefix + generateMsgId());
    }

    private String generateMsgId() {
        return "<" + UUID.randomUUID().toString() + "@" + serverSocket.getInetAddress().getHostName() + ">";
    }

    private void handleParsedRequest(ParseResult parseResult) throws IOException {
        if (!parseResult.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = TranslatorRouteTable.getAddressForLanguage(parseResult.getTargetLanguage());
        if (address == null) {
            writeOutput(401, "Bad Target Language");
            return;
        }

        writeOutput(200, "Success");
    }

    private void writeOutput(int responseCode, String message) throws IOException {
        writer.println(responseCode + " " + message);
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
