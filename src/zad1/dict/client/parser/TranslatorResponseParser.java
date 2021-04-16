package zad1.dict.client.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatorResponseParser {
    private static final Pattern pattern = Pattern.compile(
            "^\\{\"(.+)\"\\}$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS
    );

    public static TranslatorResponse parse(String translatorResponse) {
        Matcher matcher = pattern.matcher(translatorResponse);
        if (!matcher.find()) {
            return TranslatorResponse.getInvalidParseResult();
        }

        return new TranslatorResponse(true, matcher.group(1));
    }
}
