package zad1.dict.server.translator.server;

import zad1.dict.server.Server;

import java.net.ServerSocket;

public abstract class Translator extends Server {
    public Translator(ServerSocket serverSocket) {
        super(serverSocket);
    }

    public String getConnectionLabel() {
        return "Connection with Proxy";
    }
}
