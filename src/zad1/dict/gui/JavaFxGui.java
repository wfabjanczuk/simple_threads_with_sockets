package zad1.dict.gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zad1.dict.client.Client;

public class JavaFxGui extends Application implements Gui {
    private static Client client;

    private static Stage primaryStage;
    private static final Double sceneWidth = 640.0;
    private static final Double sceneHeight = 120.0;

    public void setClient(Client client) {
        JavaFxGui.client = client;
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
        JavaFxGui.primaryStage = primaryStage;
        setWindowTitle();

        VBox root = new VBox();
        root.getChildren().add(getMainGridPane(root));

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane getMainGridPane(VBox parent) {
        GridPane gridPane = new GridPane();
        gridPane.setPrefHeight(sceneHeight);
        gridPane.prefWidthProperty().bind(parent.widthProperty());

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.3);
            gridPane.getColumnConstraints().add(col);
        }

        gridPane.add(getTargetLanguagePane(gridPane), 0, 0);
        gridPane.add(getInputPane(gridPane), 1, 0);
        gridPane.add(getOutputPane(gridPane), 2, 0);

        return gridPane;
    }

    private StackPane getTargetLanguagePane(GridPane gridPane) {
        return getDefaultTopPane(
                new Label("Target language"),
                new Text("Text"),
                true,
                gridPane
        );
    }

    private StackPane getInputPane(GridPane gridPane) {
        return getDefaultTopPane(
                new Label("Enter word"),
                new Text("Text"),
                true,
                gridPane
        );
    }

    private StackPane getOutputPane(GridPane gridPane) {
        return getDefaultTopPane(
                new Label("Translation"),
                new Text("Text"),
                false,
                gridPane
        );
    }

    private StackPane getDefaultTopPane(Label label, Text text, Boolean rightSeparator, GridPane gridPane) {
        StackPane defaultTopPane;

        if (rightSeparator) {
            Separator separator = new Separator(Orientation.VERTICAL);
            defaultTopPane = new StackPane(label, text, separator);
            StackPane.setAlignment(separator, Pos.CENTER_RIGHT);
        } else {
            defaultTopPane = new StackPane(label, text);
        }

        StackPane.setAlignment(label, Pos.TOP_LEFT);
        StackPane.setAlignment(text, Pos.CENTER);

        defaultTopPane.prefHeightProperty().bind(gridPane.heightProperty());
        defaultTopPane.prefWidthProperty().bind(gridPane.widthProperty().divide(4));

        return defaultTopPane;
    }
}
