package zad1.dict.server.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRequestParser {
    private static final Pattern pattern = Pattern.compile(
            "^\\{\"(\\w+)\",\"(\\w{2})\",([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static ClientRequest parse(String clientRequest) {
        Matcher matcher = pattern.matcher(clientRequest);
        if (!matcher.find()) {
            return ClientRequest.getInvalidParseResult();
        }

        String word = matcher.group(1).toLowerCase();
        String targetLanguage = matcher.group(2).toUpperCase();
        Integer port = Integer.parseInt(matcher.group(3));

        return new ClientRequest(true, word, targetLanguage, port);
    }
}
