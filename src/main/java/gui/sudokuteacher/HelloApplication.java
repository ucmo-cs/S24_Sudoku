package gui.sudokuteacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        AnchorPane anchorPane = new AnchorPane();
        SudokuBoardController sudoku = new SudokuBoardController("300967001040302080020000070070000090000873000500010003004705100905000207800621004");
        anchorPane.setLayoutX(10);
        anchorPane.getChildren().add(sudoku.getSudokuBoardView());
        Scene scene = new Scene(anchorPane, 500, 500);
        //TODO: fix the setOnKeyPress issue, this runs all mouse clicks through board logic which wont work for the application later
        scene.setOnKeyPressed(sudoku.getSudokuBoardView().getOnKeyPressed());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}