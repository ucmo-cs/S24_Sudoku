package gui.sudokuteacher;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import solver.sudokuteacher.Sudoku;

import java.io.IOException;

public class SudokuSolverGUI extends Application {

    TextField[][] textFields = new TextField[9][9];

    @Override
    public void start(Stage stage) throws IOException {


        BorderPane borderPane = new BorderPane();
        borderPane.setMinWidth(300);
        borderPane.setStyle("-fx-background-color: rgba(255,241,147,0.84);");

        //create sudoku board
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setStyle("" +
                "-fx-background-color: rgba(255,241,147,0.84);" );

        int[][] testBoard = {

/*                { 0,0,0, 5,0,4, 0,0,9 },
                { 0,5,0, 0,0,0, 0,7,0 },
                { 6,0,0, 0,0,0, 0,0,3 },

                { 1,6,2, 0,0,0, 0,0,0 },
                { 0,0,7, 0,0,8, 0,0,0 },
                { 0,0,3, 0,9,7, 0,0,0 },

                { 0,3,0, 0,2,0, 6,0,0 },
                { 0,0,0, 0,1,0, 0,0,0 },
                { 0,0,0, 7,0,0, 2,1,0 }*/

                { 0,0,0, 0,0,0, 0,0,0 },
                { 0,0,6, 0,1,0, 9,0,0 },
                { 0,0,7, 8,0,0, 0,0,2 },
                { 0,0,0, 0,4,0, 0,0,3 },
                { 0,0,1, 0,7,0, 0,0,8 },
                { 0,7,4, 0,9,0, 0,2,0 },
                { 4,3,0, 0,0,0, 0,0,0 },
                { 0,0,2, 5,0,0, 0,0,6 },
                { 6,0,0, 1,0,0, 0,3,0 }

        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField num = new TextField();

                num.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
                num.setAlignment(Pos.CENTER);
                num.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, new Insets(1, 1, 1, 1))));



                //following code is used to help visual the sudoku board better by making each quadrant lines thicker than each individual square
                //sets style for top row of sudoku
                if ((i == 0 )) {
                    if (j == 0) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 1, 1, 4), null)));
                    }else if(j == 8){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 4, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 1, 1, 2), null)));

                    }else {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 1, 1, 1), null)));
                    }
                }

                //sets style for row 2 & 5
                else if (i == 2 || i == 5){

                    if (j == 0) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 4), null)));
                    }else if(j == 8){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 4, 2, 1), null)));

                    }else if(j == 2 || j == 5){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 2, 1), null)));

                    } else if(j == 3 || j == 6){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 2), null)));

                    }else {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 1), null)));
                    }
                }

                //sets style for rows 3 & 6
                else if (i == 3 || i == 6) {

                    if (j == 0) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 4), null)));
                    } else if (j == 8) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 4, 1, 1), null)));

                    } else if (j == 2 || j == 5) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 2, 1, 1), null)));

                    } else if (j == 3 || j == 6) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 2), null)));

                    } else{
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 1), null)));
                    }
                }

                //sets style for rows 1, 4, 5
                else if (i == 1 || i == 4 || i == 7){
                    if (j == 0) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 4), null)));
                    }else if(j == 8){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 4, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 2), null)));

                    }else {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1), null)));
                    }
                }
                //sets style for bottom row of board
                else  {
                    if (j == 0) {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 4, 4), null)));
                    }else if(j == 8){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 4, 4, 1), null)));

                    }else if(j == 2 || j == 5){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 4, 1), null)));

                    } else if(j == 3 || j == 6){
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 4, 2), null)));

                    }else {
                        num.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 4, 1), null)));
                    }
                }



                if (testBoard[i][j] != 0) {
                    num.setText(Integer.toString(testBoard[i][j]));
                }
                textFields[i][j] = num;
                gridPane.add(num, j, i );
            }

        }

        //create buttons
        Button btSolve = new Button("Solve");
        Button btClear = new Button("Clear");

        GridPane buttonHolder = new GridPane();
        buttonHolder.setAlignment(Pos.BOTTOM_RIGHT);
        buttonHolder.add(btSolve, 1, 0);
        buttonHolder.add(btClear, 0, 0);
        borderPane.setBottom(buttonHolder);

        //style for top of border pane
        Label title = new Label("Nick's Logical Sudoku Solver");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTextFill(Color.MEDIUMPURPLE);
        borderPane.topProperty();
        borderPane.setTop(title);

        borderPane.setAlignment(title, Pos.CENTER);
        borderPane.setCenter(gridPane);


        Scene scene = new Scene(borderPane, 400, 400);
        stage.setTitle("Nick's Sudoku Solver");
        stage.setScene(scene);
        stage.show();

        btSolve.setOnAction(e -> {


            int[][] board = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {

                        try {
                            board[i][j] = Integer.parseInt(textFields[i][j].getText());
                        }catch (NumberFormatException nfe){
                            board[i][j] = 0;
                        }
                    }

                }

            double startTime = System.nanoTime();

            Sudoku sudokuLinear = new Sudoku(board);
            sudokuLinear.solve();

            double endTime = System.nanoTime();

                updateTextFields(sudokuLinear);

            double duration = (endTime - startTime);
            System.out.println("Linear Solver solved in: " + duration / 1000000);

        });

        btClear.setOnAction(e -> {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    textFields[i][j].setText(" ");
                }
            }
        });

    }

    private void updateTextFields(Sudoku sudoku) {

        Platform.runLater( () -> {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                textFields[i][j].setText(Integer.toString(sudoku.getCell(i,j).getSolution()));

            }
        }
        });
    }


    public static void main(String[] args) {
        launch();
    }
}
