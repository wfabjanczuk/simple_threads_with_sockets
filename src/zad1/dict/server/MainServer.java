package zad1.dict.server;

import zad1.dict.server.parser.ParseResult;
import zad1.dict.server.parser.RequestParser;
import zad1.dict.server.translator.TranslatorRouteTable;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class MainServer extends Server {
    public final static int port = 2628;
    private String initialResponsePrefix;

    public MainServer(ServerSocket serverSocket) {
        super(serverSocket);
        prepareInitialResponsePrefix();
    }

    public String getConnectionLabel() {
        return "Connection with Client";
    }

    protected void handleRequests() throws IOException {
        sendInitialResponse();

        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText("Received " + line);
            ParseResult parseResult = RequestParser.parseRequest(line);
            handleParsedRequest(parseResult);
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

        forwardRequest(address, parseResult.getOriginalRequest());
        writeOutput(200, "Success");
    }

    private void forwardRequest(InetSocketAddress address, String originalRequest) throws IOException {
        logThreadCustomText("Connection to TranslatorServer established.");

        Socket translatorConnection = new Socket(address.getHostName(), address.getPort());
        PrintWriter printerWriter = getWriterForConnection(translatorConnection);
        printerWriter.println(originalRequest);
        logThreadCustomText("Forwarded " + originalRequest);

        try {
            printerWriter.close();
            translatorConnection.close();

            logThreadCustomText("Connection to TranslatorServer closed.");
        } catch (IOException exception) {
            logThreadException(exception);
        }
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
