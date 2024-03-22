package gui.sudokuteacher.scenes;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.sudokuteacher.controllers.SudokuBoardController;
import gui.sudokuteacher.views.ButtonView;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;


public class PlaySudokuScene extends Application {

    SudokuBoardController sudokuController;

    @Override
    public void start(Stage primaryStage) {

        Scene mainMenuScene = MainMenuScene.createMainMenu(primaryStage);

        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Sudoku Teacher");
        primaryStage.show();
    }

    public static void startSudokuGame(Stage primaryStage) {
        try {
            // Create the Sudoku game scene
            SudokuBoardController sudoku = new SudokuBoardController("000001030231090000065003100678924300103050006000136700009360570006019843300000000");
            BorderPane root = new BorderPane();
            root.setBottom(ButtonView.createButtonBar());
            root.setPadding(new Insets(10));
            root.setCenter(sudoku.getSudokuBoardView());

           // root.getCenter().autosize();
            root.setRight(ButtonView.createNumberPad(sudoku));

            Scene sudokuGameScene = new Scene(root, 750, 600);
            sudokuGameScene.setOnKeyPressed(e -> sudoku.keyPressed(e.getCode()));

            // Switch to the Sudoku game scene
            primaryStage.setScene(sudokuGameScene);
            primaryStage.setTitle("Sudoku Game");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyPressed(KeyEvent e){
        sudokuController.keyPressed(e.getCode());
    }

    public static void main(String[] args) {
        launch();
    }
}