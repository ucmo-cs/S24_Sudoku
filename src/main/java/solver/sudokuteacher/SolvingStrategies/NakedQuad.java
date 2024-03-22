package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SolvingStrategiesModels.NakedQuadModel;
import solver.sudokuteacher.SolvingStrategiesModels.NakedTripleModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class NakedQuad extends SolvingStrategy{
    public NakedQuad(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {
        boolean flag = false;

        for (int i = 0; i < 9; i++) {
            if(nakedQuadRowHelper(i)){
                flag = true;
            }
            if(nakedQuadColumnHelper(i)){
                flag = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(nakedQuadBoxHelper(i * 3, j * 3)){
                    flag = true;
                }
            }
        }

        return flag;
    }

    private boolean nakedQuadRowHelper(int row){
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            int size = sudokuBoard[row][column].getPossibilities().size();
            if(size >= 2 && size <= 4){
                candidateCells.add(sudokuBoard[row][column]);
            }
        }

        if(nakedQuadHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }
    private boolean nakedQuadColumnHelper(int column){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            int size = sudokuBoard[row][column].getPossibilities().size();
            if(size >= 2 && size <= 4){
                candidateCells.add(sudokuBoard[row][column]);
            }
        }

        if(nakedQuadHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }
    private boolean nakedQuadBoxHelper(int boxRowTop, int boxColumnTop){
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                int size = sudokuBoard[boxRow][boxColumn].getPossibilities().size();
                if(size >= 2 && size <= 4){
                    candidateCells.add(sudokuBoard[boxRow][boxColumn]);
                }
            }
        }
        if(nakedQuadHelper(candidateCells)){
            flag = true;
        }

        return flag;
    }

    private boolean nakedQuadHelper(ArrayList<Cell> candidateCells){
        boolean flag = false;

        if(candidateCells.size() < 4){
            return false;
        }

        ArrayList<LinkedList<Cell>> nakedTriples = findNakedQuads(candidateCells);

        if(nakedTriples == null || nakedTriples.size() < 1){
            return false;
        }

        if(findNakedQuadEliminations(nakedTriples)){
            flag = true;
        }

        return flag;

    }

    private ArrayList<LinkedList<Cell>> findNakedQuads(ArrayList<Cell> candidateCells){
        ArrayList<LinkedList<Cell>> nakedQuad = new ArrayList<>();
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

        if(strategyPossibles.size() < 4){
            return null;
        }

        LinkedList<Integer> potentialStrategyPossibles = new LinkedList<>();
        for (int i = 0; i < strategyPossibles.size() - 3; i++) {
            potentialStrategyPossibles.add(strategyPossibles.get(i));
            for (int j = i + 1; j < strategyPossibles.size() - 2; j++) {
                potentialStrategyPossibles.add(strategyPossibles.get(j));
                for (int k = j + 1; k < strategyPossibles.size() - 1; k++) {
                    potentialStrategyPossibles.add(strategyPossibles.get(k));
                    for (int l = k + 1; l < strategyPossibles.size(); l++) {
                        potentialStrategyPossibles.add(strategyPossibles.get(l));

                        LinkedList<Cell> nakedQuadStrategyCells = new LinkedList<>();

                        for (Cell cell: candidateCells) {
                            if(potentialStrategyPossibles.containsAll(cell.getPossibilities())){
                                nakedQuadStrategyCells.add(cell);
                            }
                        }
                        if(nakedQuadStrategyCells.size() == 4){
                            nakedQuad.add(nakedQuadStrategyCells);
                        }
                        potentialStrategyPossibles.removeLast();
                    }
                    potentialStrategyPossibles.removeLast();
                }
                potentialStrategyPossibles.removeLast();
            }
            potentialStrategyPossibles.removeLast();
        }
        return nakedQuad;
    }

    private boolean findNakedQuadEliminations(ArrayList<LinkedList<Cell>> nakedQuads){
        boolean flag = false;

        for (LinkedList<Cell> nakedQuad: nakedQuads) {
            ArrayList<Integer> nakedQuadPossibles = new ArrayList<>(4);
            int row = nakedQuad.getFirst().getRow();
            int column = nakedQuad.getFirst().getColumn();
            int[] box = sudoku.getBox(row, column);
            boolean sameRow = true;
            boolean sameColumn = true;
            boolean sameBox = true;

            for (Cell cell: nakedQuad) {
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
                    if(!nakedQuadPossibles.contains(possible)){
                        nakedQuadPossibles.add(possible);
                    }
                }
            }

            if(nakedQuadPossibles.size() != 4){
                continue;
            }

            ArrayList<Cell> affectedCells = new ArrayList<>();
            if(sameBox){
                ArrayList<Cell> boxCells = sudoku.getCellsInBox(box);
                for (Cell cell: boxCells) {
                    if(!nakedQuad.contains(cell) && (cell.getPossibilities().contains(nakedQuadPossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(2)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(3)))){
                        affectedCells.add(cell);
                    }
                }
            }
            if(sameRow){
                ArrayList<Cell> rowCells = sudoku.getCellsInRow(row);
                for (Cell cell: rowCells) {
                    if(!nakedQuad.contains(cell) && (cell.getPossibilities().contains(nakedQuadPossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(2)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(3)))){
                        affectedCells.add(cell);
                    }
                }

            }
            if(sameColumn){
                ArrayList<Cell> columnCells = sudoku.getCellsInColumn(column);
                for (Cell cell: columnCells) {
                    if(!nakedQuad.contains(cell) && (cell.getPossibilities().contains(nakedQuadPossibles.get(0)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(1)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(2)) ||
                            cell.getPossibilities().contains(nakedQuadPossibles.get(3)))){
                        affectedCells.add(cell);
                    }
                }
            }

            if(affectedCells.size() < 1){
                return false;
            }
            if(executeStrategy) {
                for (Cell cell : affectedCells) {
                    if (cell.getPossibilities().removeIf(p -> nakedQuadPossibles.contains(p))) {
                        flag = true;
                    }
                }
            }else{
                NakedQuadModel nakedTripleModel = new NakedQuadModel("Naked Quad");
                nakedTripleModel.setNakedQuadCells(nakedQuad);
                nakedTripleModel.setAffectedCells(affectedCells);
                nakedTripleModel.setStrategyPossibles(nakedQuadPossibles);
                strategyModels.add(nakedTripleModel);
                flag = true;
            }
        }

        return flag;
    }
}
