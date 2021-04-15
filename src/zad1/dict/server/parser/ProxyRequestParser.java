package zad1.dict.server.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyRequestParser {
    private static final Pattern pattern = Pattern.compile(
            "^\\{\"(\\w+)\",\"([\\d.]+)\",([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS
    );

    public static ProxyRequest parse(String mainServerRequest) {
        Matcher matcher = pattern.matcher(mainServerRequest);
        if (!matcher.find()) {
            return ProxyRequest.getInvalidParseResult();
        }

        String word = matcher.group(1).toLowerCase();
        String hostAddress = matcher.group(2);
        Integer port = Integer.parseInt(matcher.group(3));

        return new ProxyRequest(true, word, hostAddress, port);
    }
}
