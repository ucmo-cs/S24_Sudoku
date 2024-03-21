package gui.sudokuteacher.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuScene {

    public static Scene createMainMenu(Stage primaryStage) {
        // Create welcome text
        Text welcomeText = new Text("Welcome to the Sudoku Teacher Application");
        welcomeText.setFont(Font.font("Arial", 20));

        // Create start game button
        Button newGameButton = new Button("Start a New Game");
        newGameButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px;");
        newGameButton.setOnAction(e -> {
            // Once "Start a New Game" is pressed, switch to the Sudoku game scene
            HelloApplication.startSudokuGame(primaryStage);
        });

        // Create VBox layout for menu
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.getChildren().addAll(welcomeText, newGameButton);
        menuLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Set up the main menu scene
        Scene mainMenuScene = new Scene(menuLayout, 500, 400);
        return mainMenuScene;
    }
}
