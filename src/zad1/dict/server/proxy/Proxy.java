package zad1.dict.server.proxy;

import zad1.dict.server.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Proxy extends Server {
    public final static int port = 2628;
    public final static String defaultConnectionLabel = "Client connection";

    private final String serverHostname;
    private final String osName;

    public Proxy(ServerSocket serverSocket) {
        super(serverSocket);

        serverHostname = serverSocket.getInetAddress().getHostName();
        osName = System.getProperty("os.name");
    }

    public String getDefaultConnectionLabel() {
        return defaultConnectionLabel;
    }

    @Override
    protected void handleConnection(Socket connection) {
        ProxyWorker proxyServerWorker = new ProxyWorker(connection, serverHostname, osName);
        proxyServerWorker.startIfValid();
    }
}
