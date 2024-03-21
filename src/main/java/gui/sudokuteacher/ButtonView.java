package gui.sudokuteacher;

import gui.sudokuteacher.controllers.SudokuBoardController;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class ButtonView extends GridPane{

    public static HBox createButtonBar() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 0, 10, 0));

        // Create clear button
        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #ff6666; -fx-font-size: 16px");
        clearBtn.setMinSize(100,20);
        clearBtn.setFocusTraversable(false);
        clearBtn.setOnAction(e -> {
            System.out.println("Clear Button Pressed");
        });

        // Create menu button
        Button menuBtn = new Button("Return to Menu");
        menuBtn.setStyle("-fx-background-color: #ffcc66; -fx-font-size: 16px");
        menuBtn.setMinSize(100,20);
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


    public static GridPane createNumberPad (SudokuBoardController sudokuController) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));

        int num = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(Integer.toString(num++));
                //button.setPrefSize(10,10 );
                button.setMinSize(80,100);
                button.setStyle("-fx-background-color: #00ccff; -fx-font-size: 16px");
                button.setFocusTraversable(false);
                int finalNum = num-1;
                button.setOnAction(e -> {
                    // Map the button click to corresponding digit key
                    KeyCode keyCode = mapButtonToKeyCode(finalNum);
                    // Call the keyPressed method of the provided SudokuBoardController instance
                    sudokuController.keyPressed(keyCode);
                });
                gridPane.add(button, col, row);
            }
        }

        return gridPane;
    }

    // Helper method to map button numbers to corresponding digit key codes
    private static KeyCode mapButtonToKeyCode(int buttonNumber) {
        switch (buttonNumber) {
            case 1: return KeyCode.DIGIT1;
            case 2: return KeyCode.DIGIT2;
            case 3: return KeyCode.DIGIT3;
            case 4: return KeyCode.DIGIT4;
            case 5: return KeyCode.DIGIT5;
            case 6: return KeyCode.DIGIT6;
            case 7: return KeyCode.DIGIT7;
            case 8: return KeyCode.DIGIT8;
            case 9: return KeyCode.DIGIT9;
            default: return null; // Return null for non-digit buttons (e.g., clear button)
        }
    }

    private void handleNumberPadButtonClick(String value) {
        System.out.println("Number pad button " + value + " pressed");
        // You can handle number pad button clicks here if needed
    }


}
