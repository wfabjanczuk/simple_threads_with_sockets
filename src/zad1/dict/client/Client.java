package zad1.dict.client;

import zad1.dict.LoggableSocketThread;
import zad1.dict.client.parser.TranslatorResponse;
import zad1.dict.client.parser.TranslatorResponseParser;
import zad1.dict.server.proxy.Proxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Thread implements LoggableSocketThread {
    private final ServerSocket serverSocket;

    public final static String defaultConnectionLabel = "Proxy connection";
    public final String proxyHost;
    private Socket proxyConnection;
    private PrintWriter proxySocketWriter;
    private BufferedReader proxySocketReader;

    public final static String translatorConnectionLabel = "Translator connection";
    private final int translatorConnectionTimeout;
    private Socket translatorConnection;
    private BufferedReader translatorBufferedReader;

    public Client(String proxyHost, ServerSocket serverSocket, int translatorConnectionTimeout) {
        this.proxyHost = proxyHost;
        this.serverSocket = serverSocket;
        this.translatorConnectionTimeout = translatorConnectionTimeout;
    }

    public String getDefaultConnectionLabel() {
        return defaultConnectionLabel;
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    public boolean initializeIfValid() {
        if (isValid()) {
            initialize();
            return true;
        }

        logThreadCannotInitialize();
        return false;
    }

    private boolean isValid() {
        return serverSocket != null;
    }

    private void initialize() {
        try {
            serverSocket.setSoTimeout(translatorConnectionTimeout);
            openProxyResources();
            readInitialProxyResponse();

            logThreadConnectionEstablished();
            logThreadInitialized();
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    private void openProxyResources() throws IOException {
        proxyConnection = new Socket(proxyHost, Proxy.port);

        logThreadConnectionEstablished();

        proxySocketReader = getReaderForConnection(proxyConnection);
        proxySocketWriter = getWriterForConnection(proxyConnection);

        logThreadConnectionResourcesOpened();
    }

    private BufferedReader getReaderForConnection(Socket connection) throws IOException {
        return new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
        );
    }

    private PrintWriter getWriterForConnection(Socket connection) throws IOException {
        return new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8),
                true
        );
    }

    private void readInitialProxyResponse() throws IOException {
        String resp = proxySocketReader.readLine(); // połączenie nawiązane - info o tym
        System.out.println(resp);
        if (!resp.startsWith("220")) {
            throw new IOException("");
        }
        logThreadConnectionEstablished();
    }

    public void closeResources() {
        closeProxyResources();
        closeTranslatorConnectionResources();
    }

    private void closeProxyResources() {
        try {
            proxyConnection.close();

            logThreadConnectionResourcesClosed();
            logThreadConnectionClosed();
        } catch (IOException exception) {
            logThreadException(exception);
        }
    }

    public String getTranslation(String word, String targetLanguage) {
        try {
            sendRequestForTranslation(prepareTranslationRequest(word, targetLanguage));
            translatorConnection = serverSocket.accept();
            return getTranslationFromConnection();
        } catch (IOException exception) {
            logThreadException(exception);
            return null;
        }
    }

    private String prepareTranslationRequest(String word, String targetLanguage) {
        String[] parts = {
                quote(word.toLowerCase()),
                quote(targetLanguage.toUpperCase()),
                String.valueOf(getLocalPort())
        };
        return "{" + String.join(",", parts) + "}";
    }

    private String quote(String text) {
        return "\"" + text + "\"";
    }

    private void sendRequestForTranslation(String requestForTranslation) {
        proxySocketWriter.println(requestForTranslation);

        logThreadSent(requestForTranslation);
    }

    private String getTranslationFromConnection() {
        try {
            openTranslatorConnectionResources();
            return getTranslationFromResponse();
        } catch (IOException exception) {
            logThreadException(exception);
            return null;
        } finally {
            closeTranslatorConnectionResources();
        }
    }

    private void openTranslatorConnectionResources() throws IOException {
        translatorBufferedReader = new BufferedReader(new InputStreamReader(translatorConnection.getInputStream(), StandardCharsets.UTF_8));

        logThreadConnectionResourcesOpened(translatorConnectionLabel);
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

    private String getTranslationFromResponse() throws IOException {
        String response = translatorBufferedReader.readLine();

        logThreadReceived(response);

        TranslatorResponse translatorResponse = TranslatorResponseParser.parse(response);

        if (translatorResponse.isValid()) {
            return translatorResponse.getTranslation();
        }

        return null;
    }
}
