package zad1.dict.gui;

import zad1.dict.client.Client;

public interface Gui {
    void setClient(Client client);

    void initialize();

    void setWindowTitle();

    String getTranslation(String word, String targetLanguage);
}
