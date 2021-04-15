package zad1.dict.server.proxy;

import zad1.dict.LoggableSocketThread;
import zad1.dict.server.ServerWorker;
import zad1.dict.server.parser.ClientRequest;
import zad1.dict.server.parser.ClientRequestParser;
import zad1.dict.server.translator.TranslatorRouteTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

public class ProxyWorker extends ServerWorker implements LoggableSocketThread {
    private String initialResponsePrefix;
    private final String serverHostname;
    private final String osName;

    public ProxyWorker(Socket connection, String serverHostname, String osName) {
        super(connection);
        this.serverHostname = serverHostname;
        this.osName = osName;
        prepareInitialResponsePrefix();
    }

    private void prepareInitialResponsePrefix() {
        initialResponsePrefix = "220 "
                + serverHostname
                + " on "
                + osName
                + " <auth.mime> ";
    }

    protected void handleRequests() throws IOException {
        sendInitialResponse();

        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText("Received " + line);
            handleParsedRequest(ClientRequestParser.parse(line));
        }
    }

    private void sendInitialResponse() {
        writer.println(initialResponsePrefix + generateMsgId());
    }

    private String generateMsgId() {
        return "<" + UUID.randomUUID().toString() + "@" + serverHostname + ">";
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
        logThreadCustomText("Connection to Translator established.");

        Socket translatorConnection = new Socket(address.getHostName(), address.getPort());
        PrintWriter printerWriter = getWriterForConnection(translatorConnection);
        BufferedReader bufferedReader = getReaderForConnection(translatorConnection);

        String requestForTranslator = getRequestForTranslator(clientRequest);
        printerWriter.println(requestForTranslator);
        logThreadCustomText("Sent " + requestForTranslator);

        System.out.println(bufferedReader.readLine());

        try {
            translatorConnection.close();
            logThreadCustomText("Connection to Translator closed.");
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
}
