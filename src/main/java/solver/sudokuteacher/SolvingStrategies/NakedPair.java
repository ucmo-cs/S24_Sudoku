package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SolvingStrategiesModels.NakedPairModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class NakedPair extends SolvingStrategy{
    NakedPairModel nakedPair;

    public NakedPair(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {
        boolean flag = false;

        for (int i = 0; i < 9; i++) {
            if(nakedPairRowHelper(i) ){
                flag = true;
            }
            if(nakedPairColumnHelper(i)){
                flag = true;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(nakedPairBoxHelper(i * 3, j * 3)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean nakedPairRowHelper(int row) {
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            if(sudokuBoard[row][column].getPossibilities().size() == 2){
                candidateCells.add(sudokuBoard[row][column]);
            }

        }
        if(candidateCells.size() < 2){
            return false;
        }

        ArrayList<LinkedList<Cell>> nakedPairs = findNakedPairs(candidateCells);

        if(nakedPairFindEliminations(nakedPairs)){
            flag = true;
        }

        return flag;
    }

    private boolean nakedPairColumnHelper(int column) {
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            if(sudokuBoard[row][column].getPossibilities().size() == 2){
                candidateCells.add(sudokuBoard[row][column]);
            }
        }
        if(candidateCells.size() < 2){
            return false;
        }

       ArrayList<LinkedList<Cell>> nakedPairs = findNakedPairs(candidateCells);

        if(nakedPairFindEliminations(nakedPairs)){
            flag = true;
        }
        return flag;
    }

    private boolean nakedPairBoxHelper(int rowTop, int columnTop) {
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();

        for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
            for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                if(sudokuBoard[boxRow][boxColumn].getPossibilities().size() == 2){
                    candidateCells.add(sudokuBoard[boxRow][boxColumn]);
                }
            }
        }

        if(candidateCells.size() < 2){
            return false;
        }

        ArrayList<LinkedList<Cell>> nakedPairs = findNakedPairs(candidateCells);

        if(nakedPairFindEliminations(nakedPairs)){
            flag = true;
        }
        return flag;
    }

    private ArrayList<LinkedList<Cell>> findNakedPairs(ArrayList<Cell> candidateCells){
        ArrayList<LinkedList<Cell>> nakedPairs = new ArrayList<>();
        for (int i = 0; i < candidateCells.size(); i++) {
            LinkedList<Cell> nakedPair = new LinkedList<>();
            Cell currentCell = candidateCells.get(i);
            ArrayList<Integer> currentCellPossibles = currentCell.getPossibilities();
            for (int j = i + 1; j < candidateCells.size(); j++) {
                Cell nextCell = candidateCells.get(j);
                if(currentCellPossibles.containsAll(nextCell.getPossibilities())){
                    nakedPair.add(currentCell);
                    nakedPair.add(nextCell);
                    nakedPairs.add(nakedPair);
                    break;
                }
            }
        }
        return nakedPairs;
    }

    private boolean nakedPairFindEliminations(ArrayList<LinkedList<Cell>> nakedPairs){
        boolean flag = false;

        for (LinkedList<Cell> nakedPairPossible: nakedPairs) {
            int[] boxTop = new int[2];

            Cell cell1 = nakedPairPossible.getFirst();
            Cell cell2 = nakedPairPossible.getLast();

            if(cell1.getPossibilities().size() != 2 || cell2.getPossibilities().size() != 2){
                continue;
            }

            int possible1 = cell1.getPossibilities().get(0);
            int possible2 = cell1.getPossibilities().get(1);

            boolean areCellsInSameBox = sudoku.areCellsInSameBox(cell1, cell2);
            boolean areCellsInSameRow = (cell1.getRow() == cell2.getRow());
            boolean areCellsInSameColumn = (cell1.getColumn() == cell2.getColumn());
            if(areCellsInSameBox){
                boxTop = sudoku.getBox(cell1.getRow(), cell1.getColumn());
            }

            if(!executeStrategy){
                nakedPair = new NakedPairModel("NakedPair");
                nakedPair.setCell1(cell1);
                nakedPair.setCell2(cell2);
                nakedPair.getPossiblesThatMakeUpStrategy().add(possible1);
                nakedPair.getPossiblesThatMakeUpStrategy().add(possible2);
            }

            if(areCellsInSameBox){
                if(removePossibleFromBox(boxTop, cell1, cell2, possible1, possible2)){
                    flag = true;
                }
            }
            if(areCellsInSameRow){
                if(removePossibleFromRow(cell1.getRow(), cell1, cell2, possible1, possible2)){
                    flag = true;
                }
            }
            if(areCellsInSameColumn){
                if(removePossibleFromColumn(cell1.getColumn(), cell1, cell2, possible1, possible2)){
                    flag = true;
                }
            }

            if(flag){
                strategyModels.add(nakedPair);
            }
        }

        return flag;
    }

    private boolean removePossibleFromBox(int[] boxTop, Cell cell1, Cell cell2, int possible1, int possible2){
        boolean flag = false;

        for (int boxRow = boxTop[0]; boxRow < 3 + boxTop[0]; boxRow++) {
            for (int boxColumn = boxTop[1]; boxColumn < 3 + boxTop[1]; boxColumn++) {
                if(sudokuBoard[boxRow][boxColumn].getSolution() == 0 &&
                        !(sudokuBoard[boxRow][boxColumn] == cell1 || sudokuBoard[boxRow][boxColumn] == cell2) &&
                        (sudokuBoard[boxRow][boxColumn].getPossibilities().contains(possible1) || sudokuBoard[boxRow][boxColumn].getPossibilities().contains(possible2))){
                    if(executeStrategy) {
                        sudokuBoard[boxRow][boxColumn].getPossibilities().remove(Integer.valueOf(possible1));
                        sudokuBoard[boxRow][boxColumn].getPossibilities().remove(Integer.valueOf(possible2));
                    }else{
                        nakedPair.getAffectedCells().add(sudokuBoard[boxRow][boxColumn]);
                    }
                    flag = true;
                }
            }
        }
        return flag;
    }
    private boolean removePossibleFromRow(int row, Cell cell1, Cell cell2, int possible1, int possible2){
        boolean flag = false;
        for (int column = 0; column < 9; column++) {
            if(sudokuBoard[row][column].getSolution() == 0 &&
                    (sudokuBoard[row][column] != cell1 && sudokuBoard[row][column] != cell2) &&
                    (sudokuBoard[row][column].getPossibilities().contains(possible1) || sudokuBoard[row][column].getPossibilities().contains(possible2))){
                if(executeStrategy) {
                    sudokuBoard[row][column].getPossibilities().remove(Integer.valueOf(possible1));
                    sudokuBoard[row][column].getPossibilities().remove(Integer.valueOf(possible2));
                }else{
                    nakedPair.getAffectedCells().add(sudokuBoard[row][column]);
                }
                flag = true;
            }
        }
        return flag;
    }
    private boolean removePossibleFromColumn(int column, Cell cell1, Cell cell2, int possible1, int possible2){
        boolean flag = false;
        for (int row = 0; row < 9; row++) {
            if(sudokuBoard[row][column].getSolution() == 0 &&
                    (sudokuBoard[row][column] != cell1 && sudokuBoard[row][column] != cell2) &&
                    (sudokuBoard[row][column].getPossibilities().contains(possible1) || sudokuBoard[row][column].getPossibilities().contains(possible2))){
                if(executeStrategy) {
                    sudokuBoard[row][column].getPossibilities().remove(Integer.valueOf(possible1));
                    sudokuBoard[row][column].getPossibilities().remove(Integer.valueOf(possible2));
                }else{
                    nakedPair.getAffectedCells().add(sudokuBoard[row][column]);
                }
                flag = true;
            }
        }
        return flag;
    }



    @Override
    public boolean findValidExecutions() {
        strategyModels.clear();
        executeStrategy = false;
        boolean strategyFound = executeStrategy();
        executeStrategy = true;
        return strategyFound;
    }
}
