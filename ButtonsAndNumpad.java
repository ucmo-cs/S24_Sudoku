package gui.sudokuteacher;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ButtonsAndNumpad extends VBox {
    private ButtonController buttonController;

    public ButtonsAndNumpad() {
        super();
        initializeComponents();
    }

    private void initializeComponents() {
        // Create number pad buttons
        GridPane numberPad = createNumberPad();

        // Create other buttons (e.g., eraser, main menu, timer)
        Button eraseButton = new Button("Erase");
        Button mainMenuButton = new Button("Main Menu");
        Button timerButton = new Button("Timer");

        // Set up button controller
        buttonController = new ButtonController(this);

        // Add action listeners to buttons
        eraseButton.setOnAction(buttonController);
        mainMenuButton.setOnAction(buttonController);
        timerButton.setOnAction(buttonController);

        // Add components to the VBox
        this.getChildren().addAll(numberPad, eraseButton, mainMenuButton, timerButton);
    }

    private GridPane createNumberPad() {
        GridPane numberPad = new GridPane();
        numberPad.setAlignment(Pos.CENTER);

        // Create number pad buttons
        for (int i = 1; i <= 9; i++) {
            Button button = new Button(Integer.toString(i));
            button.setOnAction(buttonController);
            numberPad.add(button, (i - 1) % 3, (i - 1) / 3);
        }

        return numberPad;
    }
}
