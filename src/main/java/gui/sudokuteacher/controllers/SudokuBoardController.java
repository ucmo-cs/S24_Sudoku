package gui.sudokuteacher.controllers;

import gui.sudokuteacher.views.CellView;
import gui.sudokuteacher.views.SudokuBoardView;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
<<<<<<< HEAD:src/main/java/gui/sudokuteacher/SudokuBoardController.java
=======
import solver.sudokuteacher.SolvingStrategiesModels.StrategyModel;
>>>>>>> Nicholas:src/main/java/gui/sudokuteacher/controllers/SudokuBoardController.java
import solver.sudokuteacher.SudokuCompenents.Sudoku;
import java.util.ArrayList;


//TODO: add logic for handling when a user input an incorrect solution (Number has already been added in unit) || solution produces invalid cell (no possible)
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
<<<<<<< HEAD:src/main/java/gui/sudokuteacher/SudokuBoardController.java
        //sudokuBoardView.setOnKeyPressed(this::keyPressed);
=======
        sudokuBoardView.setMaxSize(100,100);
>>>>>>> Nicholas:src/main/java/gui/sudokuteacher/controllers/SudokuBoardController.java
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

    public void keyPressed(KeyCode keyPressed){
        boolean numberAdded = false;
        int digit = -1;
<<<<<<< HEAD:src/main/java/gui/sudokuteacher/SudokuBoardController.java
        //KeyCode keyPressed = e.getCode();
=======
>>>>>>> Nicholas:src/main/java/gui/sudokuteacher/controllers/SudokuBoardController.java
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
                editPossibles = !editPossibles;
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        if(!cellsInBoardView[row][column].isSolutionHint) {
                            if (editPossibles) {
                                cellsInBoardView[row][column].enableEditPencilMarks();
                            } else {
                                cellsInBoardView[row][column].disableEditPencilMarks();
                            }
                        }
                    }
                }
            }
            case H -> {
                ArrayList<StrategyModel> nextStrategies = sudokuModel.getNextStrategy();
                System.out.println(nextStrategies.get(0).toString());
                nextStrategies.get(0).draw(cellsInBoardView);
            }
            default -> {}
        }



        if(digit > -1) {
            if (!currentCell.isSolutionHint && !currentCell.isEditPencilMarks()) {

                int currentCellValue = currentCell.getCellValue();
                ArrayList<CellController> cellsSeen = getAllCellsSeen(currentCell);

                if (currentCellValue == 0 && digit > 0) {
                    sudokuModel.updateCellSolution(currentCell.getCellModel(), digit);
                    removePossibleFromAffectedCells(cellsSeen, digit);
                }else if(currentCellValue > 0 && digit != 0){
                    sudokuModel.removeSolutionFromCell(currentCell.getCellModel());
                    addPossibleToAffectedCells(cellsSeen, currentCellValue);
                    sudokuModel.updateCellSolution(currentCell.getCellModel(), digit);
                    removePossibleFromAffectedCells(cellsSeen, digit);
                }else if(currentCellValue > 0){
                    sudokuModel.removeSolutionFromCell(currentCell.getCellModel());
                    addPossibleToAffectedCells(cellsSeen, currentCellValue);
                    currentCell.updateCellPossibilities();
                }
                numberAdded = true;
            }else if(!currentCell.isSolutionHint){
                currentCell.updatePencilMarks(digit);
            }
        }
        if(numberAdded){
            if(sudokuModel.isSolved()){
                //TODO: end game logic
                if(sudokuModel.checkSolution()){
                    System.out.println("Sudoku is solved!");
                }else{
                    System.out.println("Sudoku has an error");
                }
            }

        }
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

    private void removePossibleFromAffectedCells(ArrayList<CellController> cells, int possible){
        for (CellController cell: cells) {
            cell.removePencilMark(possible);
            cell.getCellModel().getPossibilities().remove(Integer.valueOf(possible));
        }
    }

    private void addPossibleToAffectedCells(ArrayList<CellController> cells, int possible){
        for (CellController cell: cells) {
            if(sudokuModel.isPossibleValidInCell(cell.getCellModel(), possible)){
                cell.addPencilMark(possible);
                cell.getCellModel().getPossibilities().add(possible);
            }
        }
    }

    private void updateBoardCells(){

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                cellsInBoardView[row][column].updateCellPossibilities();
            }
        }
    }

    public ArrayList<CellController> getAllCellsSeen(CellController cell){

        ArrayList<CellController> cellsSeen = new ArrayList<>();

        int row = cell.getCellRow();
        int column = cell.getCellColumn();
        int[] box = cell.getBox(row, column);

        //box
        for (int boxRow = box[0]; boxRow < 3 + box[0]; boxRow++) {
            for (int boxColumn = box[1]; boxColumn < 3 + box[1]; boxColumn++) {
                if(cellsInBoardView[boxRow][boxColumn] != cell && !cellsSeen.contains(cellsInBoardView[boxRow][boxColumn])){
                    cellsSeen.add(cellsInBoardView[boxRow][boxColumn]);
                }
            }
        }
        //row
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(cellsInBoardView[row][i]) && cellsInBoardView[row][i] != cell){
                cellsSeen.add(cellsInBoardView[row][i]);
            }
        }

        //column
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(cellsInBoardView[i][column]) && cellsInBoardView[i][column] != cell){
                cellsSeen.add(cellsInBoardView[i][column]);
            }
        }

        return cellsSeen;
    }
}
