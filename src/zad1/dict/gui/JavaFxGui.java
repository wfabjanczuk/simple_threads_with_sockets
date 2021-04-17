package zad1.dict.gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zad1.dict.client.Client;
import zad1.dict.server.translator.router.TranslatorRouter;
import zad1.dict.server.translator.server.Translator_PL_EN;

public class JavaFxGui extends Application implements Gui {
    private static Client client;

    private static Stage primaryStage;
    private static final Double sceneWidth = 640.0;
    private static final Double sceneHeight = 120.0;
    private static final Double inputTextFieldMaxWidth = 120.0;

    private static final String translationNotFoundMessage = "--";

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
        return getPane(
                new Label("Target language"),
                getTargetLanguageChoiceBox(),
                true,
                gridPane
        );
    }

    private ChoiceBox<String> getTargetLanguageChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(TranslatorRouter.getTargetLanguages());
        choiceBox.setValue(Translator_PL_EN.targetLanguage);
        return choiceBox;
    }

    private StackPane getInputPane(GridPane gridPane) {
        return getPane(
                new Label("Enter word"),
                getInputTextField(),
                true,
                gridPane
        );
    }

    private TextField getInputTextField() {
        TextField inputTextField = new TextField("");
        inputTextField.setMaxWidth(inputTextFieldMaxWidth);
        return inputTextField;
    }

    private StackPane getOutputPane(GridPane gridPane) {
        return getPane(
                new Label("Translation"),
                new Text(translationNotFoundMessage),
                false,
                gridPane
        );
    }

    private StackPane getPane(Label topLeftLabel, Node centerNode, Boolean rightSeparator, GridPane parentGridPane) {
        StackPane stackPane;

        if (rightSeparator) {
            Separator separator = new Separator(Orientation.VERTICAL);
            stackPane = new StackPane(topLeftLabel, centerNode, separator);
            StackPane.setAlignment(separator, Pos.CENTER_RIGHT);
        } else {
            stackPane = new StackPane(topLeftLabel, centerNode);
        }

        StackPane.setAlignment(topLeftLabel, Pos.TOP_LEFT);
        StackPane.setAlignment(centerNode, Pos.CENTER);

        stackPane.prefHeightProperty().bind(parentGridPane.heightProperty());
        stackPane.prefWidthProperty().bind(parentGridPane.widthProperty().divide(3));

        return stackPane;
    }
}
