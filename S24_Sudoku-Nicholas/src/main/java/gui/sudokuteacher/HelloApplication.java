package gui.sudokuteacher;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {

    SudokuBoardController sudokuController;

    @Override
    public void start(Stage stage) throws IOException {


        SudokuBoardController sudoku = new SudokuBoardController("300967001040302080020000070070000090000873000500010003004705100905000207800621004");

        sudokuController = sudoku;
        BorderPane root = new BorderPane();
        root.setCenter(ButtonView.createNumberPad(sudokuController));
        root.setBottom(ButtonView.createButtonBar());
        root.setPadding(new Insets(10)); // Adjust padding as needed
        root.setMargin(sudoku.getSudokuBoardView(), new Insets(10)); // Adjust margins as needed


        root.setLeft(sudoku.getSudokuBoardView());


        Scene scene = new Scene(root, 600, 600);

        //TODO: fix the setOnKeyPress issue, this runs all mouse clicks through board logic which wont work for the application later
        //scene.setOnKeyPressed(sudoku.getSudokuBoardView().getOnKeyPressed());
        scene.setOnKeyPressed(this::onKeyPressed);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.show();
    }

    public void onKeyPressed(KeyEvent e){
        sudokuController.keyPressed(e.getCode());
    }

    public static void main(String[] args) {
        launch();
    }
}