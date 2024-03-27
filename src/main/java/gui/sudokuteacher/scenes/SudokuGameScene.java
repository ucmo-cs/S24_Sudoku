package gui.sudokuteacher.scenes;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import gui.sudokuteacher.controllers.SudokuBoardController;
import gui.sudokuteacher.views.ButtonView;

public class SudokuGameScene {

    public static void startSudokuGame(Stage primaryStage, String initialSudoku) {
        try {
            // Create the Sudoku game scene
            SudokuBoardController sudoku = new SudokuBoardController(initialSudoku);
            BorderPane root = new BorderPane();
            root.setBottom(ButtonView.createButtonBar());
            root.setCenter(sudoku.getSudokuBoardView());

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

}
