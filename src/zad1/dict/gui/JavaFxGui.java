package zad1.dict.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zad1.dict.client.Client;
import zad1.dict.server.translator.router.TranslatorRouter;
import zad1.dict.server.translator.server.Translator_PL_EN;
import zad1.dict.server.translator.worker.TranslatorWorker;

public class JavaFxGui extends Application implements Gui {
    private static Client client;

    private static Stage primaryStage;
    private static final Double sceneWidth = 640.0;
    private static final Double sceneHeight = 150.0;
    private static final Double inputTextFieldMaxWidth = 120.0;

    private static ChoiceBox<String> languageChoiceBox;
    private static TextField inputField;
    private static Text outputField;
    private static final String translationNotFoundMessage = "(no translation)";
    private static final String[] wordList = {"dom", "szkoła", "nauczyciel", "droga", "krzesło"};

    public void setClient(Client client) {
        JavaFxGui.client = client;
    }

    public void setWindowTitle() {
        String windowTitle = client.getThreadLabel() + " on port " + client.getLocalPort();
        primaryStage.setTitle(windowTitle);
    }

    public String getTranslation(String word, String targetLanguage) {
        try {
            String translation = client.getTranslation(word, targetLanguage);

            if (translation.equals(TranslatorWorker.noTranslationMessage)) {
                return null;
            }

            return translation;
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
        root.getChildren().add(prepareMainGridPane(root));

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane prepareMainGridPane(VBox parent) {
        GridPane gridPane = new GridPane();
        gridPane.prefHeightProperty().bind(parent.heightProperty());
        gridPane.prefWidthProperty().bind(parent.widthProperty());

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25.0);
            gridPane.getColumnConstraints().add(col);
        }

        gridPane.add(prepareWordListPane(gridPane), 0, 0);
        gridPane.add(prepareTargetLanguagePane(gridPane), 1, 0);
        gridPane.add(prepareInputPane(gridPane), 2, 0);
        gridPane.add(prepareOutputPane(gridPane), 3, 0);

        return gridPane;
    }

    private StackPane prepareWordListPane(GridPane gridPane) {
        return preparePane(
                new Label("Word list"),
                prepareWordListText(),
                true,
                gridPane
        );
    }

    private Text prepareWordListText() {
        return new Text(String.join(System.lineSeparator(), wordList));
    }

    private StackPane prepareTargetLanguagePane(GridPane gridPane) {
        return preparePane(
                new Label("Target language"),
                prepareTargetLanguageChoiceBox(),
                true,
                gridPane
        );
    }

    private ChoiceBox<String> prepareTargetLanguageChoiceBox() {
        languageChoiceBox = new ChoiceBox<>();
        languageChoiceBox.getItems().addAll(TranslatorRouter.getTargetLanguages());
        languageChoiceBox.setValue(Translator_PL_EN.targetLanguage);
        return languageChoiceBox;
    }

    private StackPane prepareInputPane(GridPane gridPane) {
        return prepareInputPane(
                new Label("Enter word"),
                prepareInputTextField(),
                prepareTranslateButton(),
                gridPane
        );
    }

    private TextField prepareInputTextField() {
        inputField = new TextField("");
        inputField.setMaxWidth(inputTextFieldMaxWidth);
        return inputField;
    }

    private Button prepareTranslateButton() {
        Button translateButton = new Button("Translate");
        translateButton.setOnAction(actionEvent -> translateInput());
        return translateButton;
    }

    private StackPane prepareOutputPane(GridPane gridPane) {
        return preparePane(
                new Label("Translation"),
                prepareOutputField(),
                false,
                gridPane
        );
    }

    private Text prepareOutputField() {
        outputField = new Text(translationNotFoundMessage);
        return outputField;
    }

    private StackPane preparePane(Label topLeftLabel, Node centerNode, Boolean rightSeparator, GridPane parentGridPane) {
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

    private StackPane prepareInputPane(Label topLeftLabel, Node centerNode, Button translateButton, GridPane parentGridPane) {
        StackPane stackPane = preparePane(topLeftLabel, centerNode, true, parentGridPane);

        stackPane.getChildren().add(translateButton);
        StackPane.setAlignment(translateButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(translateButton, new Insets(15, 10, 10, 10));

        return stackPane;
    }

    private void translateInput() {
        String translation = getTranslation(inputField.getText(), languageChoiceBox.getValue());

        if (translation == null) {
            outputField.setText(translationNotFoundMessage);
        } else {
            outputField.setText(translation);
        }
    }
}
