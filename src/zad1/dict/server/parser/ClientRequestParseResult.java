package zad1.dict.server.parser;

public class ClientRequestParseResult {
    private boolean isValid;
    private String word;
    private String targetLanguage;
    private Integer port;

    public static ClientRequestParseResult getInvalidParseResult() {
        return new ClientRequestParseResult(false, null, null, null);
    }

    public ClientRequestParseResult(boolean isValid, String word, String targetLanguage, Integer port) {
        this.isValid = isValid;
        this.word = word;
        this.targetLanguage = targetLanguage;
        this.port = port;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getWord() {
        return word;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public Integer getPort() {
        return port;
    }
}
