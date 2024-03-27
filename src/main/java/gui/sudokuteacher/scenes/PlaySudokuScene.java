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

    //SudokuBoardController sudoku = new SudokuBoardController("087400030040008200100700000900050408001090060008007000000000009000064000030002010");

    public static void startSudokuGame(Stage primaryStage) {
        SudokuGameScene.startSudokuGame(primaryStage,"087400030040008200100700000900050408001090060008007000000000009000064000030002010" );
    }

    public void onKeyPressed(KeyEvent e){
        sudokuController.keyPressed(e.getCode());
    }

    public static void main(String[] args) {
        launch();
    }
}
