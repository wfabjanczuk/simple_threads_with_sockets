package zad1.dict.server.parser;

public class ParseResult {
    private boolean isValid;
    private String word;
    private String sourceLanguage;
    private String targetLanguage;
    private Integer port;

    public static ParseResult getInvalidParseResult() {
        return new ParseResult(false, null, null, null, null);
    }

    public ParseResult(boolean isValid, String word, String sourceLanguage, String targetLanguage, Integer port) {
        this.isValid = isValid;
        this.word = word;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.port = port;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getWord() {
        return word;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public Integer getPort() {
        return port;
    }
}
