package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.Objects;

public class BoxLineReduction extends SolvingStrategy{

    public BoxLineReduction(Sudoku sudoku) {
      super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(boxLineReductionHelper(i * 3, j * 3)) {
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

    private boolean boxLineReductionHelper(int row, int column){
        boolean flag = false;

        int row2 = row + 1;
        int row3 = row + 2;

        int column2 = column + 1;
        int column3 = column + 2;
        ArrayList<Integer> possiblesInRow1 = new ArrayList<>();
        ArrayList<Integer> possiblesInRow2 = new ArrayList<>();
        ArrayList<Integer> possiblesInRow3 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn1 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn2 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn3 = new ArrayList<>();
        ArrayList<Integer> otherPossiblesInRow1 = boxLineReductionRowHelper(row, column);
        ArrayList<Integer> otherPossiblesInRow2 = boxLineReductionRowHelper(row2, column);
        ArrayList<Integer> otherPossiblesInRow3 = boxLineReductionRowHelper(row3, column);
        ArrayList<Integer> otherPossiblesInColumn1 = boxLineReductionColumnHelper(row, column);
        ArrayList<Integer> otherPossiblesInColumn2 = boxLineReductionColumnHelper(row, column2);
        ArrayList<Integer> otherPossiblesInColumn3 = boxLineReductionColumnHelper(row, column3);

        //get all the possibilities for each row & column in the box
        for (int boxRows = row; boxRows < 3 + row; boxRows++) {
            for (Integer possible: sudokuBoard[boxRows][column].getPossibilities()) {
                if(!possiblesInColumn1.contains(possible)){
                    possiblesInColumn1.add(possible);
                }
            }
            for (Integer possible: sudokuBoard[boxRows][column2].getPossibilities()) {
                if(!possiblesInColumn2.contains(possible)){
                    possiblesInColumn2.add(possible);
                }
            }
            for (Integer possible: sudokuBoard[boxRows][column3].getPossibilities()) {
                if(!possiblesInColumn3.contains(possible)){
                    possiblesInColumn3.add(possible);
                }
            }
        }

        for (int boxColumns = column; boxColumns < 3 + column; boxColumns++) {
            for (Integer possible: sudokuBoard[row][boxColumns].getPossibilities()) {
                if(!possiblesInRow1.contains(possible)){
                    possiblesInRow1.add(possible);
                }
            }
            for (Integer possible: sudokuBoard[row2][boxColumns].getPossibilities()) {
                if(!possiblesInRow2.contains(possible)){
                    possiblesInRow2.add(possible);
                }
            }
            for (Integer possible: sudokuBoard[row3][boxColumns].getPossibilities()) {
                if(!possiblesInRow3.contains(possible)){
                    possiblesInRow3.add(possible);
                }
            }
        }

        if(boxLineReductionRowPossibilityRemover(possiblesInRow1,otherPossiblesInRow1, row2, row3,column)){
            flag = true;
        }
        if(boxLineReductionRowPossibilityRemover(possiblesInRow2,otherPossiblesInRow2, row, row3,column)){
            flag = true;
        }
        if(boxLineReductionRowPossibilityRemover(possiblesInRow3,otherPossiblesInRow3, row, row2,column)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn1,otherPossiblesInColumn1, column2, column3, row)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn2,otherPossiblesInColumn2, column, column3, row)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn3,otherPossiblesInColumn3, column, column2, row)){
            flag = true;
        }

        return flag;
    }

    //gets the possibles for the other cells in row not in the box
    private ArrayList<Integer> boxLineReductionRowHelper(int row, int column){
        ArrayList<Integer> possibilities = new ArrayList<>();

        if (column < 3) {
            for (int i = 3; i < 9; i++) {
                if(sudokuBoard[row][i].getSolution() == 0){
                    for (Integer possible: sudokuBoard[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else if (column < 6) {
            for (int i = 0; i < 3; i++) {
                if(sudokuBoard[row][i].getSolution() == 0){
                    for (Integer possible: sudokuBoard[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
            for (int i = 6; i < 9; i++) {
                if(sudokuBoard[row][i].getSolution() == 0){
                    for (Integer possible: sudokuBoard[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if(sudokuBoard[row][i].getSolution() == 0){
                    for (Integer possible: sudokuBoard[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        }

        return possibilities;
    }
    //gets the possibles for the other cells in column not in the box
    private ArrayList<Integer> boxLineReductionColumnHelper(int row, int column){
        ArrayList<Integer> possibilities = new ArrayList<>();

        if (row < 3) {
            for (int i = 3; i < 9; i++) {
                if(sudokuBoard[i][column].getSolution() == 0){
                    for (Integer possible: sudokuBoard[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else if (row < 6) {
            for (int i = 0; i < 3; i++) {
                if(sudokuBoard[i][column].getSolution() == 0){
                    for (Integer possible: sudokuBoard[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
            for (int i = 6; i < 9; i++) {
                if(sudokuBoard[i][column].getSolution() == 0){
                    for (Integer possible: sudokuBoard[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if(sudokuBoard[i][column].getSolution() == 0){
                    for (Integer possible: sudokuBoard[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        }

        return possibilities;
    }

    private boolean boxLineReductionRowPossibilityRemover(ArrayList<Integer> possiblesInBoxRow, ArrayList<Integer> otherPossiblesInRow, int otherRow1, int otherRow2, int boxColumnTop){
        boolean flag = false;
        for (Integer possible: possiblesInBoxRow) {
            if(!otherPossiblesInRow.contains(possible)){
                for (int boxColumns = boxColumnTop; boxColumns < 3 + boxColumnTop; boxColumns++) {
                    if(sudokuBoard[otherRow1][boxColumns].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                    if(sudokuBoard[otherRow2][boxColumns].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                }

            }
        }

        return flag;
    }
    private boolean boxLineReductionColumnPossibilityRemover(ArrayList<Integer> possiblesInBoxColumn, ArrayList<Integer> otherPossiblesInColumn, int otherColumn1, int otherColumn2, int boxRowTop){
        boolean flag = false;
        for (Integer possible: possiblesInBoxColumn) {
            if(!otherPossiblesInColumn.contains(possible)){
                for (int boxRows = boxRowTop; boxRows < 3 + boxRowTop; boxRows++) {
                    if(sudokuBoard[boxRows][otherColumn1].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                    if(sudokuBoard[boxRows][otherColumn2].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }
}
