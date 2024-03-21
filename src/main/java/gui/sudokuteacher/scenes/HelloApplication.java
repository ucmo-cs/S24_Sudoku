package gui.sudokuteacher.scenes;

import gui.sudokuteacher.controllers.SudokuBoardController;
import gui.sudokuteacher.views.ButtonView;
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


        SudokuBoardController sudoku = new SudokuBoardController("400000938732094100895310240370609004529001673604703090957008300103900400240030709");

        sudokuController = sudoku;
        BorderPane root = new BorderPane();

        root.setBottom(ButtonView.createButtonBar());
        root.setPadding(new Insets(10)); // Adjust padding as needed
        //root.setMargin(sudoku.getSudokuBoardView(), new Insets(10)); // Adjust margins as needed


        root.setCenter(sudoku.getSudokuBoardView());
        root.getCenter().autosize();
        root.setRight(ButtonView.createNumberPad(sudokuController));


        Scene scene = new Scene(root, 750, 600);

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
