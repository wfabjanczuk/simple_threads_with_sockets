package zad1.dict.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import zad1.dict.client.Client;

public class JavaFxGui extends Application implements Gui {
    private Client client;
    private Stage primaryStage;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setWindowTitle() {
        String windowTitle = client.getThreadLabel() + " on port " + client.getLocalPort();
        primaryStage.setTitle(windowTitle);
    }

    public String getTranslation(String word, String targetLanguage) {
        try {
            return client.getTranslation(word, targetLanguage);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void initialize() {
        launch();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.closeResources();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
    }
}
