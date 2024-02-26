package gui.sudokuteacher;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.nio.charset.CharsetEncoder;


public class SudokuBoard extends GridPane {
    CellGUI currentCell;

    public SudokuBoard(String sudokuString) {
        super();
        super.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        if(sudokuString.length() != 81){
            System.out.println("There needs to be 81 characters to make a sudoku");
            return;
        }
        setOnKeyPressed(this::keyPressed);
        setOnMouseClicked(this::onMouseClick);
        currentCell = new CellGUI();
        super.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 4, 4, 4), null)));
        buildBoard(sudokuString);
        super.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
    }

    public void onMouseClick(MouseEvent e) {
        CellGUI cell;
        Node clickedNode = e.getPickResult().getIntersectedNode();
        if (clickedNode != this) {
            // click on descendant node
            Node parent = clickedNode.getParent();
            while (parent != this) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
        }

        cell = (CellGUI) clickedNode;

        currentCell.unhighlightRowAndColumn();
        currentCell = cell;
        currentCell.highlightRowAndColumn();
    }

    public void handleArrowKeyPress(KeyCode keyCode) {
        int dx = 0, dy = 0;
        switch (keyCode) {
            case UP:
                dy = -1;
                break;
            case DOWN:
                dy = 1;
                break;
            case LEFT:
                dx = -1;
                break;
            case RIGHT:
                dx = 1;
                break;
        }

        int newRow = Math.min(Math.max(0, GridPane.getRowIndex(currentCell) + dy), 8);
        int newCol = Math.min(Math.max(0, GridPane.getColumnIndex(currentCell) + dx), 8);
        Node nextCellNode = getNodeByRowColumnIndex(newRow, newCol);

        if (nextCellNode != null && nextCellNode instanceof CellGUI) {
            currentCell.unhighlightRowAndColumn();
            currentCell = (CellGUI) nextCellNode;
            currentCell.highlightRowAndColumn();
        }
    }

    /**
     * keyPressed has been modified to support arrow keys
     */
    public void keyPressed(KeyEvent e) {
        KeyCode keyPressed = e.getCode();
        int number = -1; // Default value

        switch (keyPressed) {
            // Handle arrow key presses
            case UP, DOWN, LEFT, RIGHT:
                handleArrowKeyPress(keyPressed);
                return; // Exit the method to prevent further processing
            // Handle Tab key
            case TAB:
                moveCell(1, 0);
                return; // Exit the method to prevent further processing
            // Handle number input from numpad
            case NUMPAD1:
                number = 1;
                break;
            case NUMPAD2:
                number = 2;
                break;
            case NUMPAD3:
                number = 3;
                break;
            case NUMPAD4:
                number = 4;
                break;
            case NUMPAD5:
                number = 5;
                break;
            case NUMPAD6:
                number = 6;
                break;
            case NUMPAD7:
                number = 7;
                break;
            case NUMPAD8:
                number = 8;
                break;
            case NUMPAD9:
                number = 9;
                break;
            // Handle number input from main keyboard
            case DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9:
                number = Integer.parseInt(keyPressed.getName());
                break;
            // Handle delete/backspace key
            case DELETE, BACK_SPACE:
                currentCell.handleNumberInput(0); // Delete the value
                return; // Exit the method to prevent further processing
        }

        // If a valid number key was pressed, handle number input
        if (number != -1) {
            currentCell.handleNumberInput(number);

            // Check if the board is solved after input
            if (checkIsBoardSolved()) {
                // TODO: End game logic
            }
        }
    }





    private boolean checkIsBoardSolved(){
        for (int row = 0; row < 9; row++) {
            int rowSum = 0;
            int columnSum = 0;
            for (int column = 0; column < 9; column++) {
                int rowCellSolution = ((CellGUI) getNodeByRowColumnIndex(row, column)).getSolution();
                int columnCellSolution = ((CellGUI) getNodeByRowColumnIndex(column, row)).getSolution();
                if(rowCellSolution == 0){
                    return false;
                }else {
                    rowSum += rowCellSolution;
                }

                if(columnCellSolution == 0){
                    return false;
                }else{
                    columnSum += columnCellSolution;
                }
            }
            if(rowSum != 45 && columnSum != 45){
                //board is unsolved OR solved incorrectly
                System.out.println("There is an error in the board");
                return false;
            }
        }

        for (int boxRowTop = 0; boxRowTop < 3; boxRowTop++) {
            for (int boxColumnTop = 0; boxColumnTop < 3; boxColumnTop++) {
                if(!checkIsBoardSolvedBoxHelper(boxRowTop * 3, boxColumnTop * 3)){
                    System.out.println("There is an error in the board");
                    return false;
                }
            }
        }

        System.out.println("Board is solved!");

        return true;
    }

    private boolean checkIsBoardSolvedBoxHelper(int boxRowTop, int boxColumnTop){
        boolean isBoxValid = false;

        int boxSum = 0;
        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3 ; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                int boxCellSolution = ((CellGUI) getNodeByRowColumnIndex(boxRow, boxColumn)).getSolution();
                if(boxCellSolution == 0){
                    return isBoxValid;
                }else{
                    boxSum += boxCellSolution;
                }
            }
        }
        if(boxSum == 45){
            isBoxValid = true;
        }
        return isBoxValid;
    }

    private void moveCell(int dx, int dy) {
        int currentRow = GridPane.getRowIndex(currentCell);
        int currentCol = GridPane.getColumnIndex(currentCell);
        int newRow = Math.min(Math.max(0, currentRow + dy), 8);
        int newCol = Math.min(Math.max(0, currentCol + dx), 8);
        Node nextCell = getNodeByRowColumnIndex(newRow, newCol);
        if (nextCell instanceof CellGUI) {
            currentCell.unhighlightRowAndColumn();
            currentCell = (CellGUI) nextCell;
            currentCell.highlightRowAndColumn();
        }
    }

    private Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        for (Node node : this.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    //TODO: in the future when a user can enter their own board, have to verify theres at least 17-integers, this is the minimum amount of clues for a valid board
    private void buildBoard(String sudokuBoard){
        int index = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI( sudokuBoard.charAt(index),i, j);
                //following code is used to help visual the sudoku board better by making each quadrant lines thicker than each individual square
                //sets style for top row of sudoku
                if ((i == 0 )) {
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 1), null)));
                    }
                }

                //sets style for row 2 & 5
                else if (i == 2 || i == 5){

                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 2, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 2, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 1), null)));
                    }
                }

                //sets style for rows 3 & 6
                else if (i == 3 || i == 6) {

                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 0), null)));
                    } else if (j == 8) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 0, 1, 1), null)));

                    } else if (j == 2 || j == 5) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 2, 1, 1), null)));

                    } else if (j == 3 || j == 6) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 2), null)));

                    } else{
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 1), null)));
                    }
                }

                //sets style for rows 1, 4, 5
                else if (i == 1 || i == 4 || i == 7){
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1), null)));
                    }
                }
                //sets style for bottom row of board
                else  {
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 0, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 1), null)));
                    }
                }
                index++;
                super.add(cell, j, i );
            }

        }
    }

}
