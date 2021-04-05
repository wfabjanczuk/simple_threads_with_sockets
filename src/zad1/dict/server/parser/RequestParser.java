package zad1.dict.server.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    private static final Pattern requestPattern = Pattern.compile(
            "^\\{(\\w+),(\\w{2}),([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static ParseResult parseRequest(String originalRequest) {
        Matcher matcher = requestPattern.matcher(originalRequest);
        if (!matcher.find()) {
            return ParseResult.getInvalidParseResult();
        }

        String word = matcher.group(1);
        String targetLanguage = matcher.group(2);
        Integer port = Integer.parseInt(matcher.group(3));

        return new ParseResult(true, originalRequest, word, targetLanguage, port);
    }
}
