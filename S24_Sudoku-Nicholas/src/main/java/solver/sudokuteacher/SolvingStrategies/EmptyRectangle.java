package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class EmptyRectangle extends SolvingStrategy{


    public EmptyRectangle(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<LinkedList<Cell>> cellsInRowWithStrongLink = new ArrayList<>();
            ArrayList<LinkedList<Cell>> cellsInColumnWithStrongLink = new ArrayList<>();
            int rowCount;
            int columnCount;

            for (int row = 0; row < 9; row++) {
                LinkedList<Cell> rowStrongLink = new LinkedList<>();
                LinkedList<Cell> columnStrongLink = new LinkedList<>();
                Cell rowCell1 = null;
                Cell rowCell2 = null;
                Cell columnCell1 = null;
                Cell columnCell2 = null;
                rowCount = 0;
                columnCount = 0;
                for (int column = 0; column < 9; column++) {
                    if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)) {
                        rowCount++;
                        if (rowCount == 1) {
                            rowCell1 = sudokuBoard[row][column];
                        } else if (rowCount == 2) {
                            rowCell2 = sudokuBoard[row][column];

                        }
                    }

                    if (sudokuBoard[column][row].getSolution() == 0 && sudokuBoard[column][row].getPossibilities().contains(possible)) {
                        columnCount++;
                        if (columnCount == 1) {
                            columnCell1 = sudokuBoard[column][row];
                        } else if (columnCount == 2) {
                            columnCell2 = sudokuBoard[column][row];
                        }
                    }
                }
                if (rowCount == 2 && !sudoku.areCellsInSameBox(rowCell1, rowCell2)) {
                    rowStrongLink.add(rowCell1);
                    rowStrongLink.add((rowCell2));
                    cellsInRowWithStrongLink.add(rowStrongLink);
                }
                if (columnCount == 2 && !sudoku.areCellsInSameBox(columnCell1, columnCell2)) {
                    columnStrongLink.add(columnCell1);
                    columnStrongLink.add(columnCell2);
                    cellsInColumnWithStrongLink.add(columnStrongLink);
                }
            }

            if(emptyRectangleStrategyRowHelper(cellsInRowWithStrongLink, possible)){
                flag = true;
            }
            if(emptyRectangleStrategyColumnHelper(cellsInColumnWithStrongLink, possible)){
                flag = true;
            }

        }
        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }


    private boolean emptyRectangleStrategyRowHelper(ArrayList<LinkedList<Cell>> cellsInRowWithStrongLink, int possible){
        boolean flag = false;

        if(cellsInRowWithStrongLink.size() < 1){
            return false;
        }
        for (LinkedList<Cell> strongLinkPair: cellsInRowWithStrongLink) {
            if((strongLinkPair.getFirst().getPossibilities().size() < 2 || strongLinkPair.getLast().getPossibilities().size() < 2) ||
                    (!strongLinkPair.getFirst().getPossibilities().contains(possible)  || !strongLinkPair.getLast().getPossibilities().contains(possible))){
                continue;
            }
            boolean strongLinkEmptyRectangle = false;
            for (int index = 0 ; index < strongLinkPair.size(); index++) {

                ArrayList<Cell> possibleWingCells = new ArrayList<>();
                Cell hingeCell = strongLinkPair.get(index);
                Cell wingCell1;
                if(index == 0){
                    wingCell1 = strongLinkPair.getLast();
                }else{
                    wingCell1 = strongLinkPair.getFirst();
                }

                int column = hingeCell.getColumn();
                int count = 0;
                for (int row = 0; row < 9; row++) {
                    if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)){
                        count++;
                        if(sudokuBoard[row][column] != hingeCell && !sudoku.areCellsInSameBox(sudokuBoard[row][column], hingeCell)){
                            possibleWingCells.add(sudokuBoard[row][column]);
                        }
                    }
                }
                //this ensures there isn't a strong link between the hinge cell and wing cell. That is an edge case with separate logic
                // try to find an empty rectangle with the possible wing cells
                for (Cell possibleWingCell: possibleWingCells) {
                    ArrayList<Cell>  intersectingBoxCellsWithPossible = sudoku.getBoxCellsWithPossibility(sudoku.getBox(possibleWingCell.getRow(), wingCell1.getColumn()), possible);
                    if(intersectingBoxCellsWithPossible.size() <= 1){
                        continue;
                    }
                    boolean isEmptyRectangle = true;
                    for (Cell cell : intersectingBoxCellsWithPossible) {
                        if (!(cell.getColumn() == wingCell1.getColumn() || cell.getRow() == possibleWingCell.getRow())) {
                            isEmptyRectangle = false;
                            break;
                        }
                    }
                    if(isEmptyRectangle){
                        if(count > 2) {
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            break;
                        }else if(count == 2){
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            wingCell1.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            strongLinkEmptyRectangle = true;
                            break;
                        }
                    }
                }
                if (strongLinkEmptyRectangle){
                    break;
                }
            }
        }

        return flag;
    }
    private boolean emptyRectangleStrategyColumnHelper(ArrayList<LinkedList<Cell>> cellsInColumnWithStrongLink, int possible){
        boolean flag = false;

        if(cellsInColumnWithStrongLink.size() < 1){
            return false;
        }

        for (LinkedList<Cell> strongLinkPair: cellsInColumnWithStrongLink) {
            if((strongLinkPair.getFirst().getPossibilities().size() < 2 || strongLinkPair.getLast().getPossibilities().size() < 2) ||
                    (!strongLinkPair.getFirst().getPossibilities().contains(possible)  || !strongLinkPair.getLast().getPossibilities().contains(possible))){
                continue;
            }
            boolean strongLinkEmptyRectangle = false;
            for (int index = 0 ; index < strongLinkPair.size(); index++) {
                ArrayList<Cell> possibleWingCells = new ArrayList<>();
                Cell hingeCell = strongLinkPair.get(index);
                Cell wingCell1;
                if(index == 0){
                    wingCell1 = strongLinkPair.getLast();
                }else{
                    wingCell1 = strongLinkPair.getFirst();
                }
                int row = hingeCell.getRow();
                int count = 0;
                for (int column = 0; column < 9; column++) {
                    if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)){
                        count++;
                        if(sudokuBoard[row][column] != hingeCell && !sudoku.areCellsInSameBox(sudokuBoard[row][column], hingeCell)){
                            possibleWingCells.add(sudokuBoard[row][column]);
                        }
                    }
                }
                for (Cell possibleWingCell: possibleWingCells) {
                    ArrayList<Cell>  intersectingBoxCellsWithPossible = sudoku.getBoxCellsWithPossibility(sudoku.getBox(possibleWingCell.getColumn(), wingCell1.getRow()), possible);
                    if(intersectingBoxCellsWithPossible.size() <= 1){
                        continue;
                    }
                    boolean isEmptyRectangle = true;
                    for (Cell cell : intersectingBoxCellsWithPossible) {
                        if (!(cell.getRow() == wingCell1.getRow() || cell.getColumn() == possibleWingCell.getColumn())) {
                            isEmptyRectangle = false;
                            break;
                        }
                    }
                    if(isEmptyRectangle){
                        if(count > 2) {
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                        }else if(count == 2){
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            wingCell1.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            strongLinkEmptyRectangle = true;
                            break;
                        }
                    }
                }
                if (strongLinkEmptyRectangle){
                    break;
                }
            }
        }
        return flag;
    }
}
