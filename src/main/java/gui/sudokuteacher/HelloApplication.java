package gui.sudokuteacher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        AnchorPane anchorPane = new AnchorPane();

        SudokuBoard sudoku = new SudokuBoard();
        //CellGUI b = new CellGUI();
        anchorPane.setLayoutX(10);
        //anchorPane.getChildren().add(sudoku);
        anchorPane.getChildren().add(sudoku);
        Scene scene = new Scene(anchorPane, 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}