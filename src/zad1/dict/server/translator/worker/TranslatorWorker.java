package zad1.dict.server.translator.worker;

import zad1.dict.server.ServerWorker;
import zad1.dict.server.parser.ProxyRequest;
import zad1.dict.server.parser.ProxyRequestParser;
import zad1.dict.server.translator.server.Translator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class TranslatorWorker extends ServerWorker {
    abstract protected String getTranslation(String word);

    private final String clientConnectionLabel = "Client connection";
    private Socket clientConnection;
    private PrintWriter clientPrintWriter;

    public TranslatorWorker(Socket connection) {
        super(connection);
    }

    public String getDefaultConnectionLabel() {
        return Translator.defaultConnectionLabel;
    }

    protected void handleRequests() throws IOException {
        for (String line; (line = defaultBufferedReader.readLine()) != null; ) {
            logThreadReceived(line);

            handleProxyRequest(ProxyRequestParser.parse(line));
        }
    }

    private void handleProxyRequest(ProxyRequest proxyRequest) throws IOException {
        if (!proxyRequest.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = new InetSocketAddress(
                proxyRequest.getHostAddress(),
                proxyRequest.getPort()
        );
        if (address.isUnresolved()) {
            writeOutput(401, "Bad Client address");
            return;
        }

        String wordToTranslate = proxyRequest.getWord();
        String translation = getTranslation(wordToTranslate);
        if (translation == null) {
            writeOutput(402, "No translation for word " + wordToTranslate);
            return;
        }

        makeClientConnection(address, translation);
        writeOutput(200, getThreadLabel() + " Successfully sent translation to Client");
    }

    private void makeClientConnection(InetSocketAddress address, String translation)
            throws IOException {
        openClientConnectionResources(address);
        sendTranslationToClient(translation);
        closeClientConnectionResources();
    }

    private void openClientConnectionResources(InetSocketAddress address) throws IOException {
        clientConnection = new Socket(address.getHostName(), address.getPort());

        logThreadConnectionEstablished(clientConnectionLabel);

        clientPrintWriter = getWriterForConnection(clientConnection);

        logThreadConnectionResourcesOpened(clientConnectionLabel);
    }

    private void sendTranslationToClient(String translation) {
        String translationResponse = "{\"" + translation + "\"}";
        clientPrintWriter.println(translationResponse);

        logThreadSent(translationResponse);
    }

    private void closeClientConnectionResources() {
        try {
            clientConnection.close();

            logThreadConnectionResourcesClosed(clientConnectionLabel);
            logThreadConnectionClosed(clientConnectionLabel);
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }
}