package zad1.dict.server.proxy;

import zad1.dict.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy extends Server {
    public final static int port = 2628;
    private final String serverHostname;
    private final String osName;

    public Proxy(ServerSocket serverSocket) {
        super(serverSocket);
        serverHostname = serverSocket.getInetAddress().getHostName();
        osName = System.getProperty("os.name");
    }

    public String getConnectionLabel() {
        return "Connection with Client";
    }

    protected void handleConnection(Socket connection) {
        ProxyWorker proxyServerWorker = new ProxyWorker(connection, serverHostname, osName);
        proxyServerWorker.startIfValid();
    }

    public static void main(String[] args) {
        // TODO: tworzenie wątków dynamiczne na potrzeby nowych klientów

        ServerSocket serverSocket = null;
        String host = "localhost";

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        for (int i = 1; i <= 5; i++) {
            new Proxy(serverSocket);
        }
    }
}
