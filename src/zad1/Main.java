package zad1;

import zad1.dict.builder.ApplicationBuilder;
import zad1.dict.builder.ServerBuilder;
import zad1.dict.server.proxy.Proxy;
import zad1.dict.server.translator.server.*;

public class Main {
    public static void main(String[] args) {
        ServerBuilder.startLocally(Proxy.class, Proxy.port);

        ServerBuilder.startLocally(Translator_PL_DE.class, Translator_PL_DE.port);
        ServerBuilder.startLocally(Translator_PL_EN.class, Translator_PL_EN.port);
        ServerBuilder.startLocally(Translator_PL_ES.class, Translator_PL_ES.port);
        ServerBuilder.startLocally(Translator_PL_FR.class, Translator_PL_FR.port);
        ServerBuilder.startLocally(Translator_PL_RU.class, Translator_PL_RU.port);

        ApplicationBuilder.startMultipleLocally(2);
    }
}
