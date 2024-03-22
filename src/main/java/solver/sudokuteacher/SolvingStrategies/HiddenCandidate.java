package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SolvingStrategiesModels.HiddenCandidateModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

public class HiddenCandidate extends SolvingStrategy{

    int candidateNumber;

    public HiddenCandidate(Sudoku sudoku, int candidateNumber) {
        super(sudoku);
        this.candidateNumber = candidateNumber;
    }

    @Override
    public boolean executeStrategy(){

        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if(hiddenCandidateRow(i, candidateNumber)){
                flag = true;
            }
            if( hiddenCandidateColumn(i, candidateNumber)){
                flag = true;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(hiddenCandidateBox(i * 3, j * 3, candidateNumber)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean hiddenCandidateRow(int row, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int column = 0; column < 9; column++) {
                if ( sudokuBoard[row][column].getPossibilities().contains(i)) {
                    count++;
                }
            }
            if(count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int column = 0; column < 9; column++) {
                if (sudokuBoard[row][column].getPossibilities().contains(possible)) {
                    if(!hiddenCandidateCells.contains(sudokuBoard[row][column])) {
                        hiddenCandidateCells.add(sudokuBoard[row][column]);
                    }
                }
            }
        }
        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }

        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }
    private boolean hiddenCandidateColumn(int column, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard[j][column].getPossibilities().contains(i) ) {
                    count++;
                }
            }
            if(count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int row = 0; row < 9; row++) {
                if (sudokuBoard[row][column].getPossibilities().contains(possible)) {
                    if(!hiddenCandidateCells.contains(sudokuBoard[row][column])) {
                        hiddenCandidateCells.add(sudokuBoard[row][column]);
                    }
                }
            }
        }
        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }
        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }
    private boolean hiddenCandidateBox(int row, int column, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int boxRow = row; boxRow < 3 + row; boxRow++) {
                for (int boxColumn = column; boxColumn < 3 + column; boxColumn++) {
                    if( sudokuBoard[boxRow][boxColumn].getPossibilities().contains(i)){
                        count++;
                    }
                }
            }
            if (count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int boxRow = row; boxRow < 3 + row; boxRow++) {
                for (int boxColumn = column; boxColumn < 3 + column; boxColumn++) {
                    if(sudokuBoard[boxRow][boxColumn].getPossibilities().contains(possible)){
                        if(!hiddenCandidateCells.contains(sudokuBoard[boxRow][boxColumn])) {
                            hiddenCandidateCells.add(sudokuBoard[boxRow][boxColumn]);
                        }
                    }
                }
            }
        }

        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }


        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }

    private boolean hiddenCandidateHelper(ArrayList<Cell> hiddenCandidateCells,  ArrayList<Integer> hiddenCandidatePossibles, int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> cellToRemove = new ArrayList<>();

        for (Cell cell: hiddenCandidateCells) {
            int count = 0;
            for (Integer possible: hiddenCandidatePossibles) {
                if(cell.getPossibilities().contains(possible)){
                    count++;
                }
                if(count > 2){
                    break;
                }
            }
            if(count < 2){
                cellToRemove.add(cell);
            }
        }
        for (Cell cell: cellToRemove) {
            for (Integer possible: cell.getPossibilities()) {
                hiddenCandidatePossibles.remove(possible);
            }
        }

        for (Cell cell: hiddenCandidateCells) {
            boolean containsPossible = false;
            for (Integer possible: hiddenCandidatePossibles) {
                if(cell.getPossibilities().contains(possible)){
                    containsPossible = true;
                    break;
                }
            }
            if(!containsPossible){
                cellToRemove.add(cell);
            }
        }

        for (Cell cell : cellToRemove) {
            hiddenCandidateCells.remove(cell);
        }


        if(hiddenCandidateCells.size() == candidateNumber && hiddenCandidatePossibles.size() == candidateNumber){
            if(executeStrategy) {
                for (Cell cell : hiddenCandidateCells) {
                    if (cell.getPossibilities().removeIf(p -> !hiddenCandidatePossibles.contains(p))) {
                        flag = true;
                    }
                }
            }else{
                for (Cell cell: hiddenCandidateCells) {
                    for (Integer possible: cell.getPossibilities()) {
                        if(!hiddenCandidatePossibles.contains(possible)){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        break;
                    }
                }

                if(!flag){
                    return false;
                }

                HiddenCandidateModel hiddenCandidateModel = null;
                if(candidateNumber == 2){
                     hiddenCandidateModel = new HiddenCandidateModel("Hidden Double");
                }else if(candidateNumber == 3){
                     hiddenCandidateModel = new HiddenCandidateModel("Hidden Triple");
                }else if(candidateNumber == 4){
                     hiddenCandidateModel = new HiddenCandidateModel("Hidden Quad");
                }

                if(hiddenCandidateModel != null){
                    hiddenCandidateModel.setHiddenCandidateCells(hiddenCandidateCells);
                    hiddenCandidateModel.setHiddenCandidatePossibles(hiddenCandidatePossibles);
                    strategyModels.add(hiddenCandidateModel);
                }
            }
        }
        return flag;
    }
}
