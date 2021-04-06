package zad1.dict.server.parser;

public class MainServerRequestParseResult {
    private boolean isValid;
    private String word;
    private String hostAddress;
    private Integer port;

    public static MainServerRequestParseResult getInvalidParseResult() {
        return new MainServerRequestParseResult(false, null, null, null);
    }

    public MainServerRequestParseResult(boolean isValid, String word, String hostAddress, Integer port) {
        this.isValid = isValid;
        this.word = word;
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getWord() {
        return word;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Integer getPort() {
        return port;
    }
}
