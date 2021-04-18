package zad1.dict.server.translator.router;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class TranslatorRouter {
    private static final Map<String, InetSocketAddress> addressMap = new HashMap<>();

    public static void putAddressForLanguage(String language, InetSocketAddress inetSocketAddress) {
        addressMap.put(language, inetSocketAddress);
    }

    public static InetSocketAddress getAddressForLanguage(String language) {
        return addressMap.get(language);
    }
}
