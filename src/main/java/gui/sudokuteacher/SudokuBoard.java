package gui.sudokuteacher;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;



public class SudokuBoard extends GridPane {
    CellGUI currentCell;

    public SudokuBoard(String sudokuString) {
        super();
        if(sudokuString.length() != 81){
            System.out.println("There needs to be 81 characters to make a sudoku");
            return;
        }
        setOnKeyPressed(this::keyPressed);
        setOnMouseClicked(this::onMouseClick);
        currentCell = new CellGUI();
        super.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 4, 4, 4), null)));

        buildBoard(sudokuString);
    }

    public void onMouseClick(MouseEvent e){
        CellGUI cell;
        Node clickedNode = e.getPickResult().getIntersectedNode();
        if (clickedNode != this) {
            // click on descendant node
            Node parent = clickedNode.getParent();
            while (parent != this) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
/*            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + rowIndex + " And: " + colIndex);*/
        }

        cell = (CellGUI) clickedNode;

        currentCell.setSelected(false);
        currentCell = cell;
        currentCell.setSelected(true);
    }


    public void keyPressed(KeyEvent e){
        KeyCode keyPressed = e.getCode();
        switch (keyPressed){
            case DIGIT1, NUMPAD1 -> {currentCell.handleNumberInput(1);}
            case DIGIT2, NUMPAD2 -> {currentCell.handleNumberInput(2);}
            case DIGIT3, NUMPAD3 -> {currentCell.handleNumberInput(3);}
            case DIGIT4, NUMPAD4 -> {currentCell.handleNumberInput(4);}
            case DIGIT5, NUMPAD5 -> {currentCell.handleNumberInput(5);}
            case DIGIT6, NUMPAD6 -> {currentCell.handleNumberInput(6);}
            case DIGIT7, NUMPAD7 -> {currentCell.handleNumberInput(7);}
            case DIGIT8, NUMPAD8 -> {currentCell.handleNumberInput(8);}
            case DIGIT9, NUMPAD9 -> {currentCell.handleNumberInput(9);}
            case BACK_SPACE -> {currentCell.handleNumberInput(0);}
            case P -> {
                for (Node node: this.getChildren()) {
                    ((CellGUI) node).updateEditPencilMarks();
                }
            }
            default -> {}

        }
    }

    //TODO: in the future when a user can enter their own board, have to verify theres at least 17-integers, this is the minimum amount of clues for a valid board
    private void buildBoard(String sudokuBoard){
        int index = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI( sudokuBoard.charAt(index),i, j);
                cell.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

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
