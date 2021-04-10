package zad1.dict.client.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatorServerResponseParser {
    private static final Pattern pattern = Pattern.compile(
            "^\\{\"(.+)\"\\}$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS
    );

    public static TranslatorServerResponse parse(String translatorServerResponse) {
        Matcher matcher = pattern.matcher(translatorServerResponse);
        if (!matcher.find()) {
            return TranslatorServerResponse.getInvalidParseResult();
        }

        return new TranslatorServerResponse(true, matcher.group(1));
    }
}
