package zad1.dict.server;

import zad1.dict.server.parser.ClientRequestParseResult;
import zad1.dict.server.parser.ClientRequestParser;
import zad1.dict.server.translator.TranslatorRouteTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
            handleParsedRequest(ClientRequestParser.parseRequest(line));
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

    private void handleParsedRequest(ClientRequestParseResult clientRequestParseResult) throws IOException {
        if (!clientRequestParseResult.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = TranslatorRouteTable.getAddressForLanguage(
                clientRequestParseResult.getTargetLanguage()
        );
        if (address == null) {
            writeOutput(401, "Bad Target Language");
            return;
        }

        sendTranslationRequest(address, clientRequestParseResult);
        writeOutput(200, "Success");
    }

    private void sendTranslationRequest(InetSocketAddress address, ClientRequestParseResult clientRequestParseResult)
            throws IOException {
        logThreadCustomText("Connection to TranslatorServer established.");

        Socket translatorConnection = new Socket(address.getHostName(), address.getPort());
        PrintWriter printerWriter = getWriterForConnection(translatorConnection);
        BufferedReader bufferedReader = getReaderForConnection(translatorConnection);

        String requestForTranslator = getRequestForTranslator(clientRequestParseResult);
        printerWriter.println(requestForTranslator);
        logThreadCustomText("Sent " + requestForTranslator);

        System.out.println(bufferedReader.readLine());

        try {
            printerWriter.close();
            translatorConnection.close();

            logThreadCustomText("Connection to TranslatorServer closed.");
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    private String getRequestForTranslator(ClientRequestParseResult clientRequestParseResult) {
        String requestForTranslator = String.join(",", Arrays.asList(
                "\"" + clientRequestParseResult.getWord() + "\"",
                "\"" + connection.getInetAddress().getHostAddress() + "\"",
                String.valueOf(clientRequestParseResult.getPort())
        ));

        return "{" + requestForTranslator + "}";
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
