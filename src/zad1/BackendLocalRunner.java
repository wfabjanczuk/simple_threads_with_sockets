package zad1;

import zad1.dict.server.Server;
import zad1.dict.server.proxy.Proxy;
import zad1.dict.server.translator.server.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class BackendLocalRunner {
    public static void main(String[] args) {
        startLocally(Proxy.class, Proxy.port);

        startLocally(Translator_PL_DE.class, Translator_PL_DE.port);
        startLocally(Translator_PL_EN.class, Translator_PL_EN.port);
        startLocally(Translator_PL_ES.class, Translator_PL_ES.port);
        startLocally(Translator_PL_FR.class, Translator_PL_FR.port);
        startLocally(Translator_PL_RU.class, Translator_PL_RU.port);
    }

    private static void startLocally(Class<? extends Server> serverClass, int port) {
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
