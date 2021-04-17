package zad1.dict.builder;

import zad1.dict.server.Server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ServerBuilder {
    public static void startLocally(Class<? extends Server> serverClass, int port) {
        try {
            Class[] constructorArguments = new Class[]{ServerSocket.class};

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", port));

            Server server = serverClass.getDeclaredConstructor(constructorArguments).newInstance(serverSocket);
            server.startIfValid();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
