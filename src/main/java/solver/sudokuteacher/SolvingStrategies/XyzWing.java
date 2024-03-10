package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class XyzWing extends SolvingStrategy{


    public XyzWing(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getPossibilities().size() == 3){
                    if(xyzWingFinder(sudokuBoard[row][column])){
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private boolean xyzWingFinder(Cell hingeCell){
        boolean flag = false;
        int hingeCellRow = hingeCell.getRow();
        int hingeCellColumn = hingeCell.getColumn();
        int[] boxTop = sudoku.getBox(hingeCellRow, hingeCellColumn);
        ArrayList<Cell> possibleWingInRow = new ArrayList<>();
        ArrayList<Cell> possibleWingInColumn = new ArrayList<>();
        ArrayList<Cell> possibleWingInBox = new ArrayList<>();
        ArrayList<Integer> xyzWingPossibles = hingeCell.getPossibilities();
        LinkedList<Cell> xyzWing = new LinkedList<>();
        xyzWing.add(hingeCell);
        //find  possible wings
        for (int i = 0; i < 9; i++) {
            //row
            if(sudokuBoard[hingeCellRow][i].getPossibilities().size() == 2 && sudokuBoard[hingeCellRow][i] != hingeCell &&
                    !sudoku.areCellsInSameBox(sudokuBoard[hingeCellRow][i], hingeCell) && xyzWingPossibles.containsAll(sudokuBoard[hingeCellRow][i].getPossibilities())){
                possibleWingInRow.add(sudokuBoard[hingeCellRow][i]);
            }
            //column
            if(sudokuBoard[i][hingeCellColumn].getPossibilities().size() == 2 && sudokuBoard[i][hingeCellColumn] != hingeCell &&
                    !sudoku.areCellsInSameBox(sudokuBoard[i][hingeCellColumn], hingeCell) && xyzWingPossibles.containsAll(sudokuBoard[i][hingeCellColumn].getPossibilities())){
                possibleWingInColumn.add(sudokuBoard[i][hingeCellColumn]);
            }
        }
        //box
        for (int boxRow = boxTop[0]; boxRow < 3 + boxTop[0]; boxRow++) {
            for (int boxColumn = boxTop[1]; boxColumn < 3 + boxTop[1]; boxColumn++) {
                if ( sudokuBoard[boxRow][boxColumn] != hingeCell && sudokuBoard[boxRow][boxColumn].getPossibilities().size() == 2 &&
                        xyzWingPossibles.containsAll(sudokuBoard[boxRow][boxColumn].getPossibilities())){
                    possibleWingInBox.add(sudokuBoard[boxRow][boxColumn]);
                }
            }
        }

        //Find xyzWings and remove possibles seen by all three cells
        for (Cell boxCell: possibleWingInBox) {
            xyzWing.add(boxCell);
            for (Cell rowCell: possibleWingInRow) {
                if(rowCell.getPossibilities().contains(boxCell.getPossibilities().get(0)) ^ rowCell.getPossibilities().contains(boxCell.getPossibilities().get(1))){
                    xyzWing.add(rowCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }
        for (Cell boxCell: possibleWingInBox) {
            xyzWing.add(boxCell);
            for (Cell columnCell: possibleWingInColumn) {
                if(columnCell.getPossibilities().contains(boxCell.getPossibilities().get(0)) ^ columnCell.getPossibilities().contains(boxCell.getPossibilities().get(1))){
                    xyzWing.add(columnCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }

        for (Cell rowCell: possibleWingInRow) {
            xyzWing.add(rowCell);
            for (Cell columnCell: possibleWingInColumn) {
                if(columnCell.getPossibilities().contains(rowCell.getPossibilities().get(0)) ^ columnCell.getPossibilities().contains(rowCell.getPossibilities().get(1))){
                    xyzWing.add(columnCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }


        return flag;
    }

    private boolean xyzWingPossibleRemover(LinkedList<Cell> xyzWing){
        boolean flag = false;

        int possibleToRemove;

        if(xyzWing.get(1).getPossibilities().contains(xyzWing.getLast().getPossibilities().get(0))){
            possibleToRemove = xyzWing.getLast().getPossibilities().get(0);
        }else{
            possibleToRemove = xyzWing.getLast().getPossibilities().get(1);
        }

        ArrayList<Cell> cellsSeenByHinge = sudoku.cellsSeenHelper(xyzWing.getFirst());
        ArrayList<Cell> cellsSeenByWing1 = sudoku.cellsSeenHelper(xyzWing.get(1));
        ArrayList<Cell> cellsSeenByWing2 = sudoku.cellsSeenHelper(xyzWing.getLast());
        ArrayList<Cell> cellsSeenByAll = new ArrayList<>();

        for (Cell cell : cellsSeenByHinge) {
            if(cellsSeenByWing1.contains(cell) && cellsSeenByWing2.contains(cell)){
                cellsSeenByAll.add(cell);
            }
        }

        for (Cell cell: cellsSeenByAll) {
            if(cell.getPossibilities().removeIf(p -> p == possibleToRemove)){
                flag = true;
            }
        }

        return flag;
    }
}
