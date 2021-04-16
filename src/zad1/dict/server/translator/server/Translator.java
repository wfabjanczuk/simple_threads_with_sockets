package zad1.dict.server.translator.server;

import zad1.dict.server.Server;

import java.net.ServerSocket;

public abstract class Translator extends Server {
    public final static String defaultConnectionLabel = "Proxy connection";

    public Translator(ServerSocket serverSocket) {
        super(serverSocket);
    }

    public String getDefaultConnectionLabel() {
        return defaultConnectionLabel;
    }
}
