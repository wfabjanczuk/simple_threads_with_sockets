package zad1.dict.application.gui;

public interface Gui {
    void initialize();

    void setWindowTitle();

    String getTranslation(String word, String targetLanguage);
}
