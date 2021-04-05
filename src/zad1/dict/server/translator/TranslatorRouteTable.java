package zad1.dict.server.translator;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class TranslatorRouteTable {
    public static final String host = "localhost";
    private static final Map<String, InetSocketAddress> addressMap = initAddressMap();

    private static Map<String, InetSocketAddress> initAddressMap() {
        Map<String, InetSocketAddress> addressMap = new HashMap<>();

        addressMap.put(
                TranslatorServer_PL_DE.targetLanguage,
                new InetSocketAddress(host, TranslatorServer_PL_DE.port)
        );
        addressMap.put(
                TranslatorServer_PL_EN.targetLanguage,
                new InetSocketAddress(host, TranslatorServer_PL_EN.port)
        );
        addressMap.put(
                TranslatorServer_PL_ES.targetLanguage,
                new InetSocketAddress(host, TranslatorServer_PL_ES.port)
        );
        addressMap.put(
                TranslatorServer_PL_FR.targetLanguage,
                new InetSocketAddress(host, TranslatorServer_PL_FR.port)
        );
        addressMap.put(
                TranslatorServer_PL_RU.targetLanguage,
                new InetSocketAddress(host, TranslatorServer_PL_RU.port)
        );

        return addressMap;
    }

    public static InetSocketAddress getAddressForLanguage(String language) {
        return addressMap.get(language);
    }
}
