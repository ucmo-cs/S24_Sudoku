package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

public class PointingPairsAndTriples extends SolvingStrategy{


    public PointingPairsAndTriples(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if(pointingPairsRow(row * 3, column * 3)){
                    flag = true;
                }
                if(pointingPairsColumn(row * 3, column * 3)){
                    flag = true;
                }
            }
        }

        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }


    private boolean pointingPairsRow(int row, int column){
        boolean flag = false;

        //create lists to hold rows
        ArrayList<Integer> row1Possibilities = new ArrayList<>();
        ArrayList<Integer> row2Possibilities = new ArrayList<>();
        ArrayList<Integer> row3Possibilities = new ArrayList<>();

        int row2 = row + 1;
        int row3 = row + 2;

        for (int i = column; i < column + 3; i++) {
            //get possibilities in row 1
            if (sudokuBoard[row][i].getSolution() == 0) {
                for (Integer possible: sudokuBoard[row][i].getPossibilities()) {
                    if (!row1Possibilities.contains(possible)) {
                        row1Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in row 2
            if (sudokuBoard[row2][i].getSolution() == 0) {
                for (Integer possible: sudokuBoard[row2][i].getPossibilities()) {
                    if (!row2Possibilities.contains(possible)) {
                        row2Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in row 3
            if (sudokuBoard[row3][i].getSolution() == 0) {
                for (Integer possible: sudokuBoard[row3][i].getPossibilities()) {
                    if (!row3Possibilities.contains(possible)) {
                        row3Possibilities.add(possible);
                    }
                }
            }
        }

        if(pointingPairRowHelper(row, column, row1Possibilities, row2Possibilities, row3Possibilities)){
            flag = true;
        }
        if(pointingPairRowHelper(row2, column, row2Possibilities, row1Possibilities, row3Possibilities)){
            flag = true;
        }
        if(pointingPairRowHelper(row3, column, row3Possibilities, row1Possibilities, row2Possibilities)){
            flag = true;
        }

        return flag;
    }

    private boolean pointingPairsColumn(int row, int column){
        boolean flag = false;

        //create lists to hold rows
        ArrayList<Integer> column1Possibilities = new ArrayList<>();
        ArrayList<Integer> column2Possibilities = new ArrayList<>();
        ArrayList<Integer> column3Possibilities = new ArrayList<>();

        int column2 = column + 1;
        int column3 = column + 2;

        for (int i = row; i < row + 3; i++) {
            //get possibilities in column 1
            if (sudokuBoard[i][column].getSolution() == 0) {
                for (Integer possible: sudokuBoard[i][column].getPossibilities()) {
                    if (!column1Possibilities.contains(possible)) {
                        column1Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in column 2
            if (sudokuBoard[i][column2].getSolution() == 0) {
                for (Integer possible: sudokuBoard[i][column2].getPossibilities()) {
                    if (!column2Possibilities.contains(possible)) {
                        column2Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in column 3
            if (sudokuBoard[i][column3].getSolution() == 0) {
                for (Integer possible: sudokuBoard[i][column3].getPossibilities()) {
                    if (!column3Possibilities.contains(possible)) {
                        column3Possibilities.add(possible);
                    }
                }
            }
        }

        if(pointingPairColumnHelper(row, column, column1Possibilities, column2Possibilities, column3Possibilities)){
            flag = true;
        }
        if(pointingPairColumnHelper(row, column2, column2Possibilities, column1Possibilities, column3Possibilities)){
            flag = true;
        }
        if(pointingPairColumnHelper(row, column3, column3Possibilities, column1Possibilities, column2Possibilities)){
            flag = true;
        }

        return flag;
    }

    private boolean pointingPairRowHelper(int row, int column, ArrayList<Integer> currentUnit, ArrayList<Integer> otherUnit1, ArrayList<Integer> otherUnit2){
        boolean flag = false;

        for (Integer possibility : currentUnit) {
            //if unit contains unique solution, remove from other cells in other box rows
            if (!(otherUnit1.contains(possibility) || otherUnit2.contains(possibility))) {
                if (column < 3) {
                    for (int i = 3; i < 9; i++) {
                        if (sudokuBoard[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else if (column < 6) {
                    for (int i = 0; i < 3; i++) {
                        if (sudokuBoard[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }

                    for (int i = 6; i < 9; i++) {
                        if (sudokuBoard[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        if (sudokuBoard[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
    private boolean pointingPairColumnHelper(int row, int column, ArrayList<Integer> currentUnit, ArrayList<Integer> otherUnit1, ArrayList<Integer> otherUnit2){
        boolean flag = false;

        for (Integer possibility : currentUnit) {
            //if unit contains unique solution, remove from other cells in other box rows
            if (!(otherUnit1.contains(possibility) || otherUnit2.contains(possibility))) {
                if (row < 3) {
                    for (int i = 3; i < 9; i++) {
                        if (sudokuBoard[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else if (row < 6) {
                    for (int i = 0; i < 3; i++) {
                        if (sudokuBoard[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }

                    for (int i = 6; i < 9; i++) {
                        if (sudokuBoard[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        if (sudokuBoard[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
}
