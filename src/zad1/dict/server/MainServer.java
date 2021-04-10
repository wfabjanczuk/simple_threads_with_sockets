package zad1.dict.server;

import zad1.dict.server.parser.ClientRequest;
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
            handleParsedRequest(ClientRequestParser.parse(line));
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

    private void handleParsedRequest(ClientRequest clientRequest) throws IOException {
        if (!clientRequest.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = TranslatorRouteTable.getAddressForLanguage(
                clientRequest.getTargetLanguage()
        );
        if (address == null) {
            writeOutput(401, "Bad Target Language");
            return;
        }

        sendTranslationRequest(address, clientRequest);
        writeOutput(200, "Success");
    }

    private void sendTranslationRequest(InetSocketAddress address, ClientRequest clientRequest)
            throws IOException {
        logThreadCustomText("Connection to TranslatorServer established.");

        Socket translatorConnection = new Socket(address.getHostName(), address.getPort());
        PrintWriter printerWriter = getWriterForConnection(translatorConnection);
        BufferedReader bufferedReader = getReaderForConnection(translatorConnection);

        String requestForTranslator = getRequestForTranslator(clientRequest);
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

    private String getRequestForTranslator(ClientRequest clientRequest) {
        String requestForTranslator = String.join(",", Arrays.asList(
                "\"" + clientRequest.getWord() + "\"",
                "\"" + connection.getInetAddress().getHostAddress() + "\"",
                String.valueOf(clientRequest.getPort())
        ));

        return "{" + requestForTranslator + "}";
    }

    public static void main(String[] args) {
        // TODO: tworzenie wątków dynamiczne na potrzeby nowych klientów

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
