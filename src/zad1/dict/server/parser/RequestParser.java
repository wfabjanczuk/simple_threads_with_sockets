package zad1.dict.server.parser;

import java.util.regex.Pattern;

public class RequestParser {
    private static final String[] targetLanguages = {"EN", "FR", "DE", "RU", "ES"};
    private static final Pattern requestPattern = Pattern.compile(
            "^\\{(\\w+),(\\w{2}),(\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static ParseResult parseRequest(String request) {
        return new ParseResult(false, null, null, null);
    }
}
