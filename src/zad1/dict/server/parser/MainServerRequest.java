package zad1.dict.server.parser;

public class MainServerRequest {
    private boolean isValid;
    private String word;
    private String hostAddress;
    private Integer port;

    public static MainServerRequest getInvalidParseResult() {
        return new MainServerRequest(false, null, null, null);
    }

    public MainServerRequest(boolean isValid, String word, String hostAddress, Integer port) {
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
