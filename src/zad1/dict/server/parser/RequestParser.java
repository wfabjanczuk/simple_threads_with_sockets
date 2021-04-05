package zad1.dict.server.parser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    private static final String[] targetLanguages = {"EN", "FR", "DE", "RU", "ES"};
    private static final Pattern requestPattern = Pattern.compile(
            "^\\{(\\w+),(\\w{2}),([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static ParseResult parseRequest(String request) {
        Matcher matcher = requestPattern.matcher(request);
        if (!matcher.find()) {
            return ParseResult.getInvalidParseResult();
        }

        String targetLanguage = matcher.group(2);
        if (!Arrays.asList(targetLanguages).contains(targetLanguage)) {
            return ParseResult.getInvalidParseResult();
        }

        String word = matcher.group(1);
        String sourceLanguage = "PL";
        Integer port = Integer.parseInt(matcher.group(3));

        return new ParseResult(true, word, sourceLanguage, targetLanguage, port);
    }
}
