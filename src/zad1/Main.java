package zad1;

import zad1.dict.client.Client;
import zad1.dict.server.MainServer;
import zad1.dict.server.translator.TranslatorServer_PL_EN;

public class Main {
    public static void main(String[] args) {
        MainServer.main(args);
        TranslatorServer_PL_EN.main(args);
        Client.main(args);
    }
}
