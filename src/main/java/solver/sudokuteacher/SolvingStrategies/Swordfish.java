package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class Swordfish extends SolvingStrategy{

    public Swordfish(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            if(swordfishRow(possible)){
                flag = true;
            }
            if(swordfishColumn(possible)){
                flag = true;
            }
        }

        return flag;
    }
    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private boolean swordfishRow(int possible){
        boolean flag = false;
        int count;
        Cell cell1;
        Cell cell2;
        Cell cell3;
        ArrayList<LinkedList<Cell>> possibleSwordfishCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            count = 0;
            cell1 = null;
            cell2 = null;
            cell3 = null;
            LinkedList<Cell> cellsWithPossibleInRow = new LinkedList<>();
            for (int column = 0; column < 9; column++) {
                if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)) {
                    count++;
                    switch (count) {
                        case 1 -> cell1 = sudokuBoard[row][column];
                        case 2 -> cell2 = sudokuBoard[row][column];
                        case 3 -> cell3 = sudokuBoard[row][column];
                        default -> {
                        }
                    }
                    if(count > 3){
                        break;
                    }
                }
            }
            if(count <= 3 && count >= 2){
                cellsWithPossibleInRow.add(cell1);
                cellsWithPossibleInRow.add(cell2);
                cellsWithPossibleInRow.add(cell3);
                possibleSwordfishCells.add(cellsWithPossibleInRow);
            }
        }

        if(possibleSwordfishCells.size() < 3){
            return false;
        }
        ArrayList<Integer> columnsRow1 = new ArrayList<>();
        ArrayList<Integer> columnsRow2 = new ArrayList<>();
        ArrayList<Integer> columnsRow3 = new ArrayList<>();
        ArrayList<Integer> totalColumns = new ArrayList<>();
        LinkedList<ArrayList<Integer>> columnsInRows = new LinkedList<>();

        for (int row1 = 0; row1 < possibleSwordfishCells.size() - 2; row1++) {
            for (Cell row1Cell: possibleSwordfishCells.get(row1)) {
                if(row1Cell != null) {
                    int column = row1Cell.getColumn();
                    if (!columnsRow1.contains(column)) {
                        columnsRow1.add(column);
                    }
                }
            }
            columnsInRows.add(columnsRow1);
            for (int row2 = row1 + 1; row2 < possibleSwordfishCells.size() - 1; row2++) {
                for (Cell row2Cell: possibleSwordfishCells.get(row2)) {
                    if(row2Cell != null) {
                        int column = row2Cell.getColumn();
                        if (!columnsRow2.contains(column)) {
                            columnsRow2.add(column);
                        }
                    }
                }
                columnsInRows.add(columnsRow2);
                for (int row3 = row2 + 1; row3 < possibleSwordfishCells.size(); row3++) {
                    for (Cell row3Cell: possibleSwordfishCells.get(row3)) {
                        if(row3Cell != null) {
                            int column = row3Cell.getColumn();
                            if (!columnsRow3.contains(column)) {
                                columnsRow3.add(column);
                            }
                        }
                    }
                    columnsInRows.add(columnsRow3);
                    for (ArrayList<Integer> cellColumns: columnsInRows) {
                        for (Integer column: cellColumns) {
                            if(!totalColumns.contains(column)){
                                totalColumns.add(column);
                            }
                        }
                    }
                    if(totalColumns.size() == 3){
                        for (Integer column: totalColumns) {
                            for (int row = 0; row < 9; row++) {
                                if(sudokuBoard[row][column].getPossibilities().contains(possible) &&
                                        !possibleSwordfishCells.get(row1).contains(sudokuBoard[row][column]) &&
                                        !possibleSwordfishCells.get(row2).contains(sudokuBoard[row][column]) &&
                                        !possibleSwordfishCells.get(row3).contains(sudokuBoard[row][column])){
                                    if(sudokuBoard[row][column].getPossibilities().removeIf(p -> p == possible)){
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                    columnsRow3.clear();
                    totalColumns.clear();
                    columnsInRows.removeLast();
                }
                columnsRow2.clear();
                columnsInRows.removeLast();
            }
            columnsRow1.clear();
            columnsInRows.clear();
        }

        return flag;
    }
    private boolean swordfishColumn(int possible){
        boolean flag = false;
        int count;
        Cell cell1;
        Cell cell2;
        Cell cell3;
        ArrayList<LinkedList<Cell>> possibleSwordfishCells = new ArrayList<>();

        for (int column = 0; column < 9; column++) {
            count = 0;
            cell1 = null;
            cell2 = null;
            cell3 = null;
            LinkedList<Cell> cellsWithPossibleInColumn = new LinkedList<>();
            for (int row = 0; row < 9; row++) {
                if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)) {
                    count++;
                    switch (count) {
                        case 1 -> cell1 = sudokuBoard[row][column];
                        case 2 -> cell2 = sudokuBoard[row][column];
                        case 3 -> cell3 = sudokuBoard[row][column];
                        default -> {
                        }
                    }
                    if(count > 3){
                        break;
                    }
                }
            }
            if(count <= 3 && count >= 2){
                cellsWithPossibleInColumn.add(cell1);
                cellsWithPossibleInColumn.add(cell2);
                cellsWithPossibleInColumn.add(cell3);
                possibleSwordfishCells.add(cellsWithPossibleInColumn);
            }
        }

        if(possibleSwordfishCells.size() < 3){
            return false;
        }
        ArrayList<Integer> rowsColumn1 = new ArrayList<>();
        ArrayList<Integer> rowsColumn2 = new ArrayList<>();
        ArrayList<Integer> rowsColumn3 = new ArrayList<>();
        ArrayList<Integer> totalRows = new ArrayList<>();
        LinkedList<ArrayList<Integer>> rowsInColumns = new LinkedList<>();

        for (int column1 = 0; column1 < possibleSwordfishCells.size() - 2; column1++) {
            for (Cell column1Cell: possibleSwordfishCells.get(column1)) {
                if(column1Cell != null) {
                    int row = column1Cell.getRow();
                    if (!rowsColumn1.contains(row)) {
                        rowsColumn1.add(row);
                    }
                }
            }
            rowsInColumns.add(rowsColumn1);
            for (int column2 = column1 + 1; column2 < possibleSwordfishCells.size() - 1; column2++) {
                for (Cell column2Cell: possibleSwordfishCells.get(column2)) {
                    if(column2Cell != null) {
                        int row = column2Cell.getRow();
                        if (!rowsColumn2.contains(row)) {
                            rowsColumn2.add(row);
                        }
                    }
                }
                rowsInColumns.add(rowsColumn2);
                for (int column3 = column2 + 1; column3 < possibleSwordfishCells.size(); column3++) {
                    for (Cell column3Cell: possibleSwordfishCells.get(column3)) {
                        if(column3Cell != null) {
                            int row = column3Cell.getRow();
                            if (!rowsColumn3.contains(row)) {
                                rowsColumn3.add(row);
                            }
                        }
                    }
                    rowsInColumns.add(rowsColumn3);
                    for (ArrayList<Integer> cellRows: rowsInColumns) {
                        for (Integer row: cellRows) {
                            if(!totalRows.contains(row)){
                                totalRows.add(row);
                            }
                        }
                    }
                    if(totalRows.size() == 3){
                        for (Integer row: totalRows) {
                            for (int column = 0; column < 9; column++) {
                                if(sudokuBoard[row][column].getPossibilities().contains(possible) &&
                                        !possibleSwordfishCells.get(column1).contains(sudokuBoard[row][column]) &&
                                        !possibleSwordfishCells.get(column2).contains(sudokuBoard[row][column]) &&
                                        !possibleSwordfishCells.get(column3).contains(sudokuBoard[row][column])){
                                    if(sudokuBoard[row][column].getPossibilities().removeIf(p -> p == possible)){
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                    rowsColumn3.clear();
                    totalRows.clear();
                    rowsInColumns.removeLast();
                }
                rowsColumn2.clear();
                rowsInColumns.removeLast();
            }
            rowsColumn1.clear();
            rowsInColumns.removeFirst();
        }
        return flag;
    }
}
