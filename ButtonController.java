package gui.sudokuteacher;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ButtonController implements EventHandler<ActionEvent> {
    private ButtonsAndNumpad buttonsAndNumpad;

    public ButtonController(ButtonsAndNumpad buttonsAndNumpad) {
        this.buttonsAndNumpad = buttonsAndNumpad;
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
