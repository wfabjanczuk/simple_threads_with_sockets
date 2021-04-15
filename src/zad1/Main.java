package zad1;

import zad1.dict.client.Client;
import zad1.dict.server.proxy.Proxy;
import zad1.dict.server.translator.server.Translator_PL_EN;

public class Main {
    public static void main(String[] args) {
        Proxy.main(args);
        Translator_PL_EN.main(args);
        Client.main(args);
    }
}
