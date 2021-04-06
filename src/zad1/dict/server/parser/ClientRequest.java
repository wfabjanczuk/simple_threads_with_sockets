package zad1.dict.server.parser;

public class ClientRequest {
    private boolean isValid;
    private String word;
    private String targetLanguage;
    private Integer port;

    public static ClientRequest getInvalidParseResult() {
        return new ClientRequest(false, null, null, null);
    }

    public ClientRequest(boolean isValid, String word, String targetLanguage, Integer port) {
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
