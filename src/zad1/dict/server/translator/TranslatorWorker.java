package zad1.dict.server.translator;

import zad1.dict.server.ServerWorker;
import zad1.dict.server.parser.ProxyRequest;
import zad1.dict.server.parser.ProxyRequestParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class TranslatorWorker extends ServerWorker {
    abstract protected String getTranslation(String word);

    public TranslatorWorker(Socket connection) {
        super(connection);
    }

    protected void handleRequests() throws IOException {
        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText("Received " + line);
            handleParsedRequest(ProxyRequestParser.parse(line));
        }
    }

    private void handleParsedRequest(ProxyRequest proxyRequest) throws IOException {
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

        sendTranslationResponse(address, translation);
        writeOutput(200, getThreadLabel() + " Successfully sent translation to Client");
    }

    private void sendTranslationResponse(InetSocketAddress address, String translation)
            throws IOException {
        logThreadCustomText("Connection to Client established.");

        Socket clientConnection = new Socket(address.getHostName(), address.getPort());
        PrintWriter printerWriter = getWriterForConnection(clientConnection);

        String translationResponse = "{\"" + translation + "\"}";
        printerWriter.println(translationResponse);
        logThreadCustomText("Sent " + translationResponse);

        try {
            clientConnection.close();
            logThreadCustomText("Connection to Client closed.");
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }
}
