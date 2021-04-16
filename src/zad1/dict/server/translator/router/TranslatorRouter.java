package zad1.dict.server.translator.router;

import zad1.dict.server.translator.server.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class TranslatorRouter {
    public static final String host = "localhost";
    private static final Map<String, InetSocketAddress> addressMap = initAddressMap();

    private static Map<String, InetSocketAddress> initAddressMap() {
        Map<String, InetSocketAddress> addressMap = new HashMap<>();

        addressMap.put(
                Translator_PL_DE.targetLanguage,
                new InetSocketAddress(host, Translator_PL_DE.port)
        );
        addressMap.put(
                Translator_PL_EN.targetLanguage,
                new InetSocketAddress(host, Translator_PL_EN.port)
        );
        addressMap.put(
                Translator_PL_ES.targetLanguage,
                new InetSocketAddress(host, Translator_PL_ES.port)
        );
        addressMap.put(
                Translator_PL_FR.targetLanguage,
                new InetSocketAddress(host, Translator_PL_FR.port)
        );
        addressMap.put(
                Translator_PL_RU.targetLanguage,
                new InetSocketAddress(host, Translator_PL_RU.port)
        );

        return addressMap;
    }

    public static InetSocketAddress getAddressForLanguage(String language) {
        return addressMap.get(language);
    }
}
