package zad1.dict.client.parser;

public class TranslatorResponse {
    private boolean isValid;
    private String translation;

    public static TranslatorResponse getInvalidParseResult() {
        return new TranslatorResponse(false, null);
    }

    public TranslatorResponse(boolean isValid, String translation) {
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
