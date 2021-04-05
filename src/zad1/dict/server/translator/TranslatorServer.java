package zad1.dict.server.translator;

import zad1.dict.server.Server;
import zad1.dict.server.parser.ParseResult;
import zad1.dict.server.parser.RequestParser;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class TranslatorServer extends Server {
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
            ParseResult parseResult = RequestParser.parseRequest(line);
            handleParsedRequest(parseResult);
        }
    }

    private void handleParsedRequest(ParseResult parseResult) throws IOException {

    }
}
