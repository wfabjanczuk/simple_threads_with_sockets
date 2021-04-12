package zad1.dict.server.translator;

import zad1.dict.server.Server;
import zad1.dict.server.parser.MainServerRequest;
import zad1.dict.server.parser.MainServerRequestParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TranslatorServer extends Server {
    // TODO: Tworzenie wątków tylko na potrzebę obsługi tłumaczenia
    abstract protected String getTranslation(String word);

    public TranslatorServer(ServerSocket serverSocket) {
        super(serverSocket);
    }

    public String getConnectionLabel() {
        return "Connection with MainServer";
    }

    protected void handleRequests() throws IOException {
        for (String line; (line = reader.readLine()) != null; ) {
            logThreadCustomText("Received " + line);
            handleParsedRequest(MainServerRequestParser.parse(line));
        }
    }

    private void handleParsedRequest(MainServerRequest mainServerRequest) throws IOException {
        if (!mainServerRequest.isValid()) {
            writeOutput(400, "Bad Request");
            return;
        }

        InetSocketAddress address = new InetSocketAddress(
                mainServerRequest.getHostAddress(),
                mainServerRequest.getPort()
        );
        if (address.isUnresolved()) {
            writeOutput(401, "Bad Client address");
            return;
        }

        String wordToTranslate = mainServerRequest.getWord();
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
