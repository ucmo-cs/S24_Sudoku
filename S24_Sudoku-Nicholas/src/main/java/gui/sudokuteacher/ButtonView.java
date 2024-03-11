package gui.sudokuteacher;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class ButtonView extends GridPane{

    public static HBox createButtonBar() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 0, 10, 0));

        // Create clear button
        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #ff6666; -fx-font-size: 16px");
        clearBtn.setFocusTraversable(false);
        clearBtn.setOnAction(e -> {
            System.out.println("Clear Button Pressed");
        });

        // Create menu button
        Button menuBtn = new Button("Return to Menu");
        menuBtn.setStyle("-fx-background-color: #ffcc66; -fx-font-size: 16px");
        menuBtn.setFocusTraversable(false);
        menuBtn.setOnAction(e -> {
            // Handle returning to menu
            System.out.println("Return to menu button pressed");
        });

        hbox.getChildren().addAll(clearBtn, menuBtn);
        menuBtn.setAlignment(Pos.TOP_CENTER);
        clearBtn.setAlignment(Pos.CENTER);

        return hbox;
    }

    public static GridPane createNumberPad () {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));

        int num = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(Integer.toString(num++));
                button.setPrefSize(40, 40);
                button.setStyle("-fx-background-color: #00ccff; -fx-font-size: 16px");
                button.setFocusTraversable(false);
                int finalNum = num-1;
                button.setOnAction(e -> {
                    System.out.println(finalNum +" button pressed");
                });
                gridPane.add(button, col, row);
            }
        }

        return gridPane;
    }

    private void handleNumberPadButtonClick(String value) {
        System.out.println("Number pad button " + value + " pressed");
        // You can handle number pad button clicks here if needed
    }


}
