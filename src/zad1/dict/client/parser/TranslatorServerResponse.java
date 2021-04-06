package zad1.dict.client.parser;

public class TranslatorServerResponse {
    private boolean isValid;
    private String translation;

    public static TranslatorServerResponse getInvalidParseResult() {
        return new TranslatorServerResponse(false, null);
    }

    public TranslatorServerResponse(boolean isValid, String translation) {
        this.isValid = isValid;
        this.translation = translation;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getTranslation() {
        return translation;
    }
}
