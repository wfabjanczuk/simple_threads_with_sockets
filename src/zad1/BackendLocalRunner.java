package zad1;

import zad1.dict.server.Server;
import zad1.dict.server.proxy.Proxy;
import zad1.dict.server.translator.router.TranslatorRouter;
import zad1.dict.server.translator.server.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class BackendLocalRunner {
    public static void main(String[] args) {
        int proxyPort = Integer.parseInt(args[0]);
        int firstTranslatorPort = Integer.parseInt(args[1]);

        startLocally(Proxy.class, proxyPort);

        Class<? extends Translator>[] translatorClasses = new Class[]{
                Translator_PL_DE.class,
                Translator_PL_EN.class,
                Translator_PL_ES.class,
                Translator_PL_FR.class,
                Translator_PL_RU.class
        };

        for (int i = 0; i < translatorClasses.length; i++) {
            startTranslatorLocally(translatorClasses[i], firstTranslatorPort + i);
        }
    }

    private static void startTranslatorLocally(Class<? extends Translator> translatorClass, int port) {
        try {
            Object targetLanguageValue = translatorClass.getDeclaredField("targetLanguage").get(null);
            TranslatorRouter.putAddressForLanguage(
                    String.valueOf(targetLanguageValue),
                    startLocally(translatorClass, port)
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    private static InetSocketAddress startLocally(Class<? extends Server> serverClass, int port) {
        InetSocketAddress inetSocketAddress = null;

        try {
            Class[] constructorArguments = new Class[]{ServerSocket.class};

            ServerSocket serverSocket = new ServerSocket();
            inetSocketAddress = new InetSocketAddress("localhost", port);
            serverSocket.bind(inetSocketAddress);

            Server server = serverClass.getDeclaredConstructor(constructorArguments).newInstance(serverSocket);
            server.startIfValid();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return inetSocketAddress;
    }
}
