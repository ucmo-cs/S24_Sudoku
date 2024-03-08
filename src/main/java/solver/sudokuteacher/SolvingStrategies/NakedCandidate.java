package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;
import java.util.ArrayList;
import java.util.Objects;

public class NakedCandidate extends SolvingStrategy{
    int candidateNumber;

    public NakedCandidate(Sudoku sudoku) {
       super(sudoku);
    }

    public boolean executeStrategy(int candidateNumber){
        this.candidateNumber = candidateNumber;
        return executeStrategy();
    }

    @Override
    public boolean executeStrategy(){

        boolean flag = false;

        for (int i = 0; i < 9; i++) {
            if(nakedCandidateRowHelper(i, candidateNumber) ){
                flag = true;
            }
            if(nakedCandidateColumnHelper(i, candidateNumber)){
                flag = true;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(nakedCandidateBoxHelper(i * 3, j * 3, candidateNumber)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean nakedCandidateRowHelper(int row, int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int column = 0; column < 9; column++) {
            if (sudokuBoard[row][column].getSolution() == 0 &&
                    sudokuBoard[row][column].getPossibilities().size() <= candidateNumber &&
                    sudokuBoard[row][column].getPossibilities().size() >= 2) {

                candidateCells.add(sudokuBoard[row][column]);

            }
        }
        if(candidateCells.size() < candidateNumber){
            return false;
        }
        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size() ; i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int column = 0; column < 9; column++) {
                    if (nakedCandidateCells.get(i).contains(sudokuBoard[row][column])) {
                        continue;
                    }
                    for (Integer possible : nakedCandidatePossibles.get(i)) {
                        if (sudokuBoard[row][column].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                            flag = true;
                        }

                    }
                }
            }
        }

        return flag;
    }

    private boolean nakedCandidateColumnHelper( int column,  int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int row = 0; row < 9; row++) {
            if (sudokuBoard[row][column].getSolution() == 0 &&
                    sudokuBoard[row][column].getPossibilities().size() <= candidateNumber &&
                    sudokuBoard[row][column].getPossibilities().size() >= 2) {

                candidateCells.add(sudokuBoard[row][column]);

            }
        }
        if(candidateCells.size() < candidateNumber){
            return false;
        }

        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size(); i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int row = 0; row < 9; row++) {
                    if (nakedCandidateCells.get(i).contains(sudokuBoard[row][column])) {
                        continue;
                    }
                    for (Integer possible : nakedCandidatePossibles.get(i)) {
                        if (sudokuBoard[row][column].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                            flag = true;
                        }

                    }
                }
            }
        }

        return flag;
    }
    private boolean nakedCandidateBoxHelper(int rowTop, int columnTop,  int candidateNumber){
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
            for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                if (sudokuBoard[boxRow][boxColumn].getSolution() == 0 &&
                        sudokuBoard[boxRow][boxColumn].getPossibilities().size() <= candidateNumber &&
                        sudokuBoard[boxRow][boxColumn].getPossibilities().size() >= 2) {
                    candidateCells.add(sudokuBoard[boxRow][boxColumn]);
                }
            }
        }
        if(candidateCells.size() < candidateNumber){
            return false;
        }

        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size(); i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
                    for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                        if (nakedCandidateCells.get(i).contains(sudokuBoard[boxRow][boxColumn])) {
                            continue;
                        }
                        for (Integer possible : nakedCandidatePossibles.get(i)) {
                            if (sudokuBoard[boxRow][boxColumn].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }
    private ArrayList<ArrayList<Integer>> nakedCandidatePossiblesHelper(ArrayList<ArrayList<Cell>> nakedCandidateCells, int candidateNumber){

        ArrayList<ArrayList<Integer>> nakedCandidatePossibles = new ArrayList<>();
        ArrayList<Integer> candidatePossibles = new ArrayList<>();
        ArrayList<Cell> candidateCells = nakedCandidateCells.get(0);
        ArrayList<Cell> cellsToRemove = new ArrayList<>();

        //first find all the cells that cannot be a naked candidate and remove them from the list
        for (int i = 0; i < candidateCells.size(); i++) {
            int count = 0;
            if(candidateCells.get(i).getPossibilities().size() == candidateNumber) {
                ArrayList<Integer> tempPossibles = candidateCells.get(i).getPossibilities();
                for (int j = 0; j < candidateCells.size(); j++) {
                    if( tempPossibles.containsAll(candidateCells.get(j).getPossibilities())){
                        count++;
                    }
                }
                if(count != candidateNumber){
                    cellsToRemove.add(candidateCells.get(i));
                }
            }
        }
        for (Cell cell: cellsToRemove) {
            candidateCells.remove(cell);
        }
        //then get all the possibles the remaining cells could be
        for (Cell cell : candidateCells) {
            for (Integer possible : cell.getPossibilities()) {
                if (!candidatePossibles.contains(possible)) {
                    candidatePossibles.add(possible);
                }
            }
        }

        //if there is a naked pair/triple/quad return it
        if(candidateCells.size() == candidateNumber && candidatePossibles.size() == candidateNumber){
            nakedCandidatePossibles.add(candidatePossibles);
            return nakedCandidatePossibles;
        }else{
            nakedCandidatePossibles.add(candidatePossibles);
        }

        boolean possibleRemoved;
        do {
            possibleRemoved = false;
            ArrayList<Integer> possibleToRemove = new ArrayList<>();
            for (Integer possible : candidatePossibles) {
                int count = 0;
                for (Cell candidate : candidateCells) {
                    if (candidate.getPossibilities().contains(possible)) {
                        count++;
                    }
                }
                if (count < 2) {
                    possibleToRemove.add(possible);
                    possibleRemoved = true;
                }
            }
            //noinspection Convert2MethodRef
            candidatePossibles.removeIf(p -> possibleToRemove.contains(p));

            for (int possible : possibleToRemove) {
                candidateCells.removeIf(cell -> cell.getPossibilities().contains(possible));
            }
        }while(possibleRemoved);

        //this means we have more than 1 naked pair
        if(candidateNumber == 2  && (nakedCandidateCells.get(0).size() == candidatePossibles.size()) && nakedCandidateCells.get(0).size() % 2 == 0){
            for (int i = 0; i < candidateCells.size(); i++) {
                ArrayList<Integer> temp = candidateCells.get(i).getPossibilities();
                for (int j = i + 1; j < candidateCells.size(); j++) {
                    if(candidateCells.get(j).getPossibilities().containsAll(temp)){
                        nakedCandidatePossibles.add(temp);
                        ArrayList<Cell> cells = new ArrayList<>();
                        cells.add(candidateCells.get(i));
                        cells.add(candidateCells.get(j));
                        nakedCandidateCells.add(cells);
                        break;
                    }
                }
            }
        }else{
            //this will find nakedCandidates only if 1 of the cells has all the possible candidates
            for (int i = 0; i < candidateCells.size(); i++) {
                ArrayList<Integer> tempPossibles = new ArrayList<>(candidateCells.get(i).getPossibilities());
                ArrayList<Cell> tempCells = new ArrayList<>();
                if(tempPossibles.size() == candidateNumber) {
                    for (int j = 0; j < candidateCells.size(); j++) {
                        if(tempPossibles.containsAll(candidateCells.get(j).getPossibilities())){
                            tempCells.add(candidateCells.get(j));
                        }
                    }

                    if(tempPossibles.size() == candidateNumber && tempCells.size() == candidateNumber){
                        nakedCandidatePossibles.add(tempPossibles);
                        nakedCandidateCells.add(tempCells);
                    }
                }else{
                   /* TODO: find a good way to find situation where the candidate numbers are spread throughout the cells w/out a single cell having them all
                            for example: candidates 1,2,3 spread through 3 cells like this (1,2)(2,3)(1,3)*/
                }
            }
        }

        return nakedCandidatePossibles;
    }
}
