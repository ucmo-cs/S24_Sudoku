package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SolvingStrategiesModels.NakedTripleModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class NakedTriple extends SolvingStrategy{

    public NakedTriple(Sudoku sudoku){super(sudoku);}

    @Override
    public boolean executeStrategy() {
        boolean flag = false;

        for (int i = 0; i < 9; i++) {
            if(nakedTripleRowHelper(i)){
                flag = true;
            }
            if(nakedTripleColumnHelper(i)){
                flag = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(nakedTripleBoxHelper(i * 3, j * 3)){
                    flag = true;
                }
            }
        }
        
        return flag;
    }
    
    private boolean nakedTripleRowHelper(int row){
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            int size = sudokuBoard[row][column].getPossibilities().size();
            if(size >= 2 && size <= 3){
                candidateCells.add(sudokuBoard[row][column]);
            }
        }

        if(nakedTripleHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }
    private boolean nakedTripleColumnHelper(int column){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            int size = sudokuBoard[row][column].getPossibilities().size();
            if(size >= 2 && size <= 3){
                candidateCells.add(sudokuBoard[row][column]);
            }
        }

        if(nakedTripleHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }
    private boolean nakedTripleBoxHelper(int boxRowTop, int boxColumnTop){   
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                int size = sudokuBoard[boxRow][boxColumn].getPossibilities().size();
                if(size >= 2 && size <= 3){
                    candidateCells.add(sudokuBoard[boxRow][boxColumn]);
                }
            }
        }
        if(nakedTripleHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }

    private boolean nakedTripleHelper(ArrayList<Cell> candidateCells){
        boolean flag = false;

        if(candidateCells.size() < 3){
            return false;
        }

        ArrayList<LinkedList<Cell>> nakedTriples = findNakedTriples(candidateCells);

        if(nakedTriples == null || nakedTriples.size() < 1){
            return false;
        }

        if(findNakedTripleEliminations(nakedTriples)){
            flag = true;
        }

        return flag;

    }

    private ArrayList<LinkedList<Cell>> findNakedTriples(ArrayList<Cell> candidateCells){
        ArrayList<LinkedList<Cell>> nakedTriples = new ArrayList<>();
        ArrayList<Integer> strategyPossibles = new ArrayList<>();
        ArrayList<Integer> possiblesToRemove = new ArrayList<>();
        int[] strategyPossiblesCount = new int[10];

        for (Cell cell: candidateCells) {
            for (Integer possible: cell.getPossibilities()) {
                strategyPossiblesCount[possible]++;
                if (!strategyPossibles.contains(possible)){
                    strategyPossibles.add(possible);
                }
            }
        }

        for (int i = 1; i < 10; i++) {
            if (strategyPossiblesCount[i] < 2){
                possiblesToRemove.add(i);
                strategyPossibles.remove(Integer.valueOf(i));
            }
        }

        if(strategyPossibles.size() < 3){
            return null;
        }

        LinkedList<Integer> potentialStrategyPossibles = new LinkedList<>();
        for (int i = 0; i < strategyPossibles.size() - 2; i++) {
            potentialStrategyPossibles.add(strategyPossibles.get(i));
            for (int j = i + 1; j < strategyPossibles.size() - 1; j++) {
                potentialStrategyPossibles.add(strategyPossibles.get(j));
                for (int k = j + 1; k < strategyPossibles.size(); k++) {

                    potentialStrategyPossibles.add(strategyPossibles.get(k));
                    LinkedList<Cell> nakedTriplesStrategyCells = new LinkedList<>();

                    for (Cell cell: candidateCells) {
                        if(potentialStrategyPossibles.containsAll(cell.getPossibilities())){
                            nakedTriplesStrategyCells.add(cell);
                        }
                    }
                    if(nakedTriplesStrategyCells.size() == 3){
                        nakedTriples.add(nakedTriplesStrategyCells);
                    }

                    potentialStrategyPossibles.removeLast();
                }
                potentialStrategyPossibles.removeLast();
            }
            potentialStrategyPossibles.removeLast();
        }
        return nakedTriples;
    }

    private boolean findNakedTripleEliminations(ArrayList<LinkedList<Cell>> nakedTriples){
        boolean flag = false;

        for (LinkedList<Cell> nakedTriple: nakedTriples) {
            ArrayList<Integer> nakedTriplePossibles = new ArrayList<>(3);
            int row = nakedTriple.getFirst().getRow();
            int column = nakedTriple.getFirst().getColumn();
            int[] box = sudoku.getBox(row, column);
            boolean sameRow = true;
            boolean sameColumn = true;
            boolean sameBox = true;

            for (Cell cell: nakedTriple) {
                int cellRow = cell.getRow();
                int cellColumn = cell.getColumn();
                int[] cellBox = sudoku.getBox(cellRow, cellColumn);

                if(row != cellRow){
                    sameRow = false;
                }
                if(column != cellColumn){
                    sameColumn = false;
                }
                if(box[0] != cellBox[0] || box[1] != cellBox[1]){
                    sameBox = false;
                }

                for (Integer possible: cell.getPossibilities()) {
                    if(!nakedTriplePossibles.contains(possible)){
                        nakedTriplePossibles.add(possible);
                    }
                }
            }

            if(nakedTriplePossibles.size() != 3){
                continue;
            }

            ArrayList<Cell> affectedCells = new ArrayList<>();
            if(sameBox){
                ArrayList<Cell> boxCells = sudoku.getCellsInBox(box);
                for (Cell cell: boxCells) {
                    if(!nakedTriple.contains(cell) && (cell.getPossibilities().contains(nakedTriplePossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(2)))){
                        affectedCells.add(cell);
                    }
                }
            }
            if(sameRow){
                ArrayList<Cell> rowCells = sudoku.getCellsInRow(row);
                for (Cell cell: rowCells) {
                    if(!nakedTriple.contains(cell) && (cell.getPossibilities().contains(nakedTriplePossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(2)))){
                        affectedCells.add(cell);
                    }
                }

            }
            if(sameColumn){
                ArrayList<Cell> columnCells = sudoku.getCellsInColumn(column);
                for (Cell cell: columnCells) {
                    if(!nakedTriple.contains(cell) && (cell.getPossibilities().contains(nakedTriplePossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedTriplePossibles.get(2)))){
                        affectedCells.add(cell);
                    }
                }
            }

            if(affectedCells.size() < 1){
                return false;
            }
            if(executeStrategy) {
                for (Cell cell : affectedCells) {
                    if (cell.getPossibilities().removeIf(p -> nakedTriplePossibles.contains(p))) {
                        flag = true;
                    }
                }
            }else{
                NakedTripleModel nakedTripleModel = new NakedTripleModel();
                nakedTripleModel.setNakedTripleCells(nakedTriple);
                nakedTripleModel.setAffectedCells(affectedCells);
                nakedTripleModel.setStrategyPossibles(nakedTriplePossibles);
                strategyModels.add(nakedTripleModel);
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
