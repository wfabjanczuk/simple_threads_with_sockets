package zad1;

import zad1.dict.client.Application;
import zad1.dict.server.Server;
import zad1.dict.server.proxy.Proxy;
import zad1.dict.server.translator.server.*;

public class Main {
    public static void main(String[] args) {
        Server.startLocally(Proxy.class, Proxy.port);

        Server.startLocally(Translator_PL_DE.class, Translator_PL_DE.port);
        Server.startLocally(Translator_PL_EN.class, Translator_PL_EN.port);
        Server.startLocally(Translator_PL_ES.class, Translator_PL_ES.port);
        Server.startLocally(Translator_PL_FR.class, Translator_PL_FR.port);
        Server.startLocally(Translator_PL_RU.class, Translator_PL_RU.port);

        Application.startLocally(2);
    }
}
