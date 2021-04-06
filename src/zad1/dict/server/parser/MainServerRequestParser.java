package zad1.dict.server.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServerRequestParser {
    private static final Pattern requestPattern = Pattern.compile(
            "^\\{\"(\\w+)\",\"([\\d.]+)\",([1-9]\\d+)\\}$",
            Pattern.CASE_INSENSITIVE
    );

    public static MainServerRequestParseResult parseRequest(String mainServerRequest) {
        Matcher matcher = requestPattern.matcher(mainServerRequest);
        if (!matcher.find()) {
            return MainServerRequestParseResult.getInvalidParseResult();
        }

        String word = matcher.group(1);
        String hostAddress = matcher.group(2);
        Integer port = Integer.parseInt(matcher.group(3));

        return new MainServerRequestParseResult(true, word, hostAddress, port);
    }
}
