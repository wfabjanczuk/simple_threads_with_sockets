package zad1.dict.server.proxy;

import zad1.dict.LoggableSocketThread;
import zad1.dict.server.ServerWorker;
import zad1.dict.server.parser.ClientRequest;
import zad1.dict.server.parser.ClientRequestParser;
import zad1.dict.server.translator.router.TranslatorRouter;

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

    private final String translatorConnectionLabel = "Translator connection";
    private Socket translatorConnection;
    private PrintWriter translatorPrintWriter;
    private BufferedReader translatorBufferedReader;

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

    public String getDefaultConnectionLabel() {
        return Proxy.defaultConnectionLabel;
    }

    protected void handleRequests() throws IOException {
        sendInitialResponse();

        for (String line; (line = defaultBufferedReader.readLine()) != null; ) {
            logThreadReceived(line);

            handleClientRequest(ClientRequestParser.parse(line));
        }
    }

    private void sendInitialResponse() {
        defaultPrintWriter.println(initialResponsePrefix + generateMsgId());
    }

    private String generateMsgId() {
        return "<" + UUID.randomUUID().toString() + "@" + serverHostname + ">";
    }

    private void handleClientRequest(ClientRequest clientRequest) throws IOException {
        if (!clientRequest.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = TranslatorRouter.getAddressForLanguage(
                clientRequest.getTargetLanguage()
        );
        if (address == null) {
            writeOutput(400, "Bad Target Language");
            return;
        }

        makeTranslatorConnection(address, clientRequest);
        writeOutput(200, "Success");
    }

    private void makeTranslatorConnection(InetSocketAddress address, ClientRequest clientRequest)
            throws IOException {
        openTranslatorConnectionResources(address);
        sendTranslatorRequest(clientRequest);
        closeTranslatorConnectionResources();
    }

    private void openTranslatorConnectionResources(InetSocketAddress address) throws IOException {
        translatorConnection = new Socket(address.getHostName(), address.getPort());

        logThreadConnectionEstablished(translatorConnectionLabel);

        translatorPrintWriter = getWriterForConnection(translatorConnection);
        translatorBufferedReader = getReaderForConnection(translatorConnection);

        logThreadConnectionResourcesOpened(translatorConnectionLabel);
    }

    private void sendTranslatorRequest(ClientRequest clientRequest) throws IOException {
        String requestForTranslator = getRequestForTranslator(clientRequest);
        translatorPrintWriter.println(requestForTranslator);

        logThreadSent(requestForTranslator);

        System.out.println(translatorBufferedReader.readLine());
    }

    private void closeTranslatorConnectionResources() {
        try {
            translatorConnection.close();

            logThreadConnectionResourcesClosed(translatorConnectionLabel);
            logThreadConnectionClosed(translatorConnectionLabel);
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    private String getRequestForTranslator(ClientRequest clientRequest) {
        String requestForTranslator = String.join(",", Arrays.asList(
                "\"" + clientRequest.getWord() + "\"",
                "\"" + defaultConnection.getInetAddress().getHostAddress() + "\"",
                String.valueOf(clientRequest.getPort())
        ));

        return "{" + requestForTranslator + "}";
    }
}
