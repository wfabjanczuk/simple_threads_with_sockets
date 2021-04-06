package zad1.dict.server.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRequestParser {
    private static final Pattern requestPattern = Pattern.compile(
            "^\\{\"(\\w+)\",\"(\\w{2})\",([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static ClientRequestParseResult parseRequest(String clientRequest) {
        Matcher matcher = requestPattern.matcher(clientRequest);
        if (!matcher.find()) {
            return ClientRequestParseResult.getInvalidParseResult();
        }

        String word = matcher.group(1).toLowerCase();
        String targetLanguage = matcher.group(2).toUpperCase();
        Integer port = Integer.parseInt(matcher.group(3));

        return new ClientRequestParseResult(true, word, targetLanguage, port);
    }
}
