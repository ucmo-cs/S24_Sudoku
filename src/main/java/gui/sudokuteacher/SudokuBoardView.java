package gui.sudokuteacher;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

public class SudokuBoardView extends GridPane {

    //ArrayList<CellController> cellsInBoard;
    CellController[][] cellsInBoard;

    public SudokuBoardView(Sudoku sudoku){
        super();
        super.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        super.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 4, 4, 4), null)));
        super.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        cellsInBoard = new CellController[9][9];
        buildBoard(sudoku);
    }

    public CellController[][] getCellsInBoard(){
        return this.cellsInBoard;
    }

    /**
     * keyPressed has been modified to support arrow keys
     * moveCell and getNode were added
     */

/*    private boolean checkIsBoardSolved(){
        for (int row = 0; row < 9; row++) {
            int rowSum = 0;
            int columnSum = 0;
            for (int column = 0; column < 9; column++) {
                 int rowCellSolution = ((CellView) getNodeByRowColumnIndex(row, column)).getSolution();
                 int columnCellSolution = ((CellView) getNodeByRowColumnIndex(column, row)).getSolution();
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
    }*/

/*    private boolean checkIsBoardSolvedBoxHelper(int boxRowTop, int boxColumnTop){
        boolean isBoxValid = false;

        int boxSum = 0;
        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3 ; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                int boxCellSolution = ((CellView) getNodeByRowColumnIndex(boxRow, boxColumn)).getSolution();
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
    }*/

    private void buildBoard(Sudoku sudoku){
        int index = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                CellController cell = new CellController(sudoku.getCell(row, column));
                //following code is used to help visual the sudoku board better by making each quadrant lines thicker than each individual square
                //sets style for top row of sudoku
                if ((row == 0 )) {
                    if (column == 0) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 0), null)));
                    }else if(column == 8){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 1, 1), null)));

                    }else if(column == 2 || column == 5){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 2, 1, 1), null)));

                    } else if(column == 3 || column == 6){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 2), null)));

                    }else {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 1), null)));
                    }
                }

                //sets style for row 2 & 5
                else if (row == 2 || row == 5){

                    if (column == 0) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 0), null)));
                    }else if(column == 8){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 2, 1), null)));

                    }else if(column == 2 || column == 5){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 2, 1), null)));

                    } else if(column == 3 || column == 6){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 2), null)));

                    }else {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 1), null)));
                    }
                }

                //sets style for rows 3 & 6
                else if (row == 3 || row == 6) {

                    if (column == 0) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 0), null)));
                    } else if (column == 8) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 0, 1, 1), null)));

                    } else if (column == 2 || column == 5) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 2, 1, 1), null)));

                    } else if (column == 3 || column == 6) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 2), null)));

                    } else{
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 1), null)));
                    }
                }

                //sets style for rows 1, 4, 5
                else if (row == 1 || row == 4 || row == 7){
                    if (column == 0) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 0), null)));
                    }else if(column == 8){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 1, 1), null)));

                    }else if(column == 2 || column == 5){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 1, 1), null)));

                    } else if(column == 3 || column == 6){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 2), null)));

                    }else {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1), null)));
                    }
                }
                //sets style for bottom row of board
                else  {
                    if (column == 0) {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 0), null)));
                    }else if(column == 8){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 1), null)));

                    }else if(column == 2 || column == 5){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 0, 1), null)));

                    } else if(column == 3 || column == 6){
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 2), null)));

                    }else {
                        cell.setCellBoarder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 1), null)));
                    }
                }
                index++;
                super.add(cell.getCellView(), column, row );
                cellsInBoard[row][column] = cell;
            }

        }
    }

}
