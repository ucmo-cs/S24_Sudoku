package gui.sudokuteacher;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class BoardController implements EventHandler<KeyEvent> {
    private SudokuBoard sudokuBoard;

    public BoardController(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    @Override
    public void handle(KeyEvent event) {
        sudokuBoard.keyPressed(event);
    }


    public void handle(MouseEvent event) {
        sudokuBoard.onMouseClick(event);
    }
}