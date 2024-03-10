package gui.sudokuteacher;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class SudokuBoardController {
    Sudoku sudokuModel;
    SudokuBoardView sudokuBoardView;
    CellController currentCell;
    CellController[][] cellsInBoardView;
    boolean editPossibles;

    public SudokuBoardController(String sudokuString) {

        if(sudokuString.length() != 81){
            System.out.println("There needs to be 81 characters to make a sudoku");
            return;
        }
        sudokuModel = new Sudoku(sudokuString);
        sudokuBoardView = new SudokuBoardView(sudokuModel);
        cellsInBoardView = sudokuBoardView.getCellsInBoard();
        editPossibles = false;

        sudokuBoardView.setOnMouseClicked(this::onMouseClick);
        sudokuBoardView.setOnKeyPressed(this::keyPressed);
    }

    public SudokuBoardView getSudokuBoardView() {
        return sudokuBoardView;
    }

    public void onMouseClick(MouseEvent e){

        Node clickedNode = e.getPickResult().getIntersectedNode();

        if (clickedNode != sudokuBoardView) {
            // click on descendant node
            Node parent = clickedNode.getParent();
            while (parent != sudokuBoardView) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
        }
        if(currentCell == null){
            currentCell = ((CellView) clickedNode).getCellController();
            currentCell.setSelected(true);
        }else {
            currentCell.setSelected(false);
            currentCell = ((CellView) clickedNode).getCellController();
            currentCell.setSelected(true);
        }
    }

    public void keyPressed(KeyEvent e){
        boolean numberAdded = false;
        int digit = -1;
        KeyCode keyPressed = e.getCode();
        switch (keyPressed){
            case DIGIT1, NUMPAD1 -> { digit = 1;}
            case DIGIT2, NUMPAD2 -> { digit = 2;}
            case DIGIT3, NUMPAD3 -> { digit = 3;}
            case DIGIT4, NUMPAD4 -> { digit = 4;}
            case DIGIT5, NUMPAD5 -> { digit = 5;}
            case DIGIT6, NUMPAD6 -> { digit = 6;}
            case DIGIT7, NUMPAD7 -> { digit = 7;}
            case DIGIT8, NUMPAD8 -> { digit = 8;}
            case DIGIT9, NUMPAD9 -> { digit = 9;}
            case BACK_SPACE -> { digit = 0;}
            case LEFT -> moveCell(-1, 0);
            case RIGHT -> moveCell(1, 0);
            case UP -> moveCell(0, -1);
            case DOWN -> moveCell(0, 1);
            case P -> {
                /*FIXME: even when P is pressed and you should be able to edit possibles, pressing a number
                    inputs a solution instead of removing/adding a possible*/

            /*        for (CellController[] cellUnit: cellsInBoardView) {
                        for (CellController cell: cellUnit) {
                            if(editPossibles){
                                cell.disablePencilMarks();
                            }else{
                                cell.enablePencilMarks();
                            }
                        }
                    }
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if(!cellsInBoardView[row][column].isSolutionHint) {
                            if (editPossibles) {
                                cellsInBoardView[row][column].disablePencilMarks();
                            } else {
                                cellsInBoardView[row][column].enablePencilMarks();
                            }
                        }
                    }
                }*/
            }
            default -> {}
        }


        if(digit > -1) {
            if (!currentCell.isSolutionHint ) {
                int currentCellValue = currentCell.getCellValue();
                if (currentCellValue == 0 && digit > 0) {
                    sudokuModel.updateCellSolution(currentCell.getCellModel(), digit);
                }else if(currentCellValue > 0 && digit != 0){
                    sudokuModel.removeSolutionFromCell(currentCell.getCellModel());
                    sudokuModel.updateCellSolution(currentCell.getCellModel(), digit);
                }else if(currentCellValue > 0){
                    sudokuModel.removeSolutionFromCell(currentCell.getCellModel());
                }
                updateBoardCells();
            }
        }
       /* if(numberAdded){
            if(checkIsBoardSolved()){
                //TODO: end game logic
            }

        }*/
    }

    private void moveCell(int dx, int dy) {
        int currentRow = currentCell.cellRow;
        int currentColumn = currentCell.cellColumn;

        int newRow = Math.min(Math.max(0, currentRow + dy), 8);
        int newCol = Math.min(Math.max(0, currentColumn + dx), 8);

        CellController nextCell = cellsInBoardView[newRow][newCol];

        if (nextCell != null) {
            currentCell.setSelected(false);
            currentCell = nextCell;
            currentCell.setSelected(true);
        }
    }

    private void updateBoardCells(){

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                cellsInBoardView[row][column].updateCellPossibilities();
            }
        }
    }
}