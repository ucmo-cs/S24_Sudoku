package solver.sudokuteacher.SolvingStrategies;


import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class XWing extends SolvingStrategy{


    public XWing(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            if(xWingRowStrategy(possible)){
                flag = true;
            }
            if (xWingColumnStrategy(possible)){
                flag = true;
            }

        }

        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private boolean xWingRowStrategy (int possible){
        boolean flag = false;
        ArrayList<LinkedList<Cell>> xWingPossibles = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            Cell cell1 = new Cell();
            Cell cell2 = new Cell();
            LinkedList<Cell> xWingCandidates = new LinkedList<>();
            int count = 0;
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)){
                    count++;
                    if(count == 1){
                        cell1 = sudokuBoard[row][column];
                    }else if(count == 2){
                        cell2= sudokuBoard[row][column];
                    }else{
                        break;
                    }
                }
            }
            if(count == 2){
                xWingCandidates.add(cell1);
                xWingCandidates.add(cell2);
                xWingPossibles.add(xWingCandidates);
            }
        }

        if(xWingPossibles.size() < 2){
            return false;
        }

        for (int i = 0; i < xWingPossibles.size(); i++) {
            for (int j = i + 1; j < xWingPossibles.size(); j++) {
                //this means we have an X-wing and can remove the possible from the columns in cells not in the X-wing
                if(xWingPossibles.get(i).getFirst().getColumn() == xWingPossibles.get(j).getFirst().getColumn() &&
                        xWingPossibles.get(i).getLast().getColumn() == xWingPossibles.get(j).getLast().getColumn()){

                    int row1 = xWingPossibles.get(i).getFirst().getRow();
                    int row2 = xWingPossibles.get(j).getFirst().getRow();
                    int column1 = xWingPossibles.get(i).getFirst().getColumn();
                    int column2 = xWingPossibles.get(j).getLast().getColumn();

                    for (int row = 0; row < 9; row++) {
                        if(!(row == row1 || row == row2)){
                            if(sudokuBoard[row][column1].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                            if(sudokuBoard[row][column2].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }

    private boolean xWingColumnStrategy (int possible){

        boolean flag = false;
        ArrayList<LinkedList<Cell>> xWingPossibles = new ArrayList<>();

        for (int column = 0; column < 9; column++) {
            Cell cell1 = new Cell();
            Cell cell2 = new Cell();
            LinkedList<Cell> xWingCandidates = new LinkedList<>();
            int count = 0;
            for (int row = 0; row < 9; row++) {
                if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible)){
                    count++;
                    if(count == 1){
                        cell1 = sudokuBoard[row][column];
                    }else if(count == 2){
                        cell2= sudokuBoard[row][column];
                    }else{
                        break;
                    }
                }
            }
            if(count == 2){
                xWingCandidates.add(cell1);
                xWingCandidates.add(cell2);
                xWingPossibles.add(xWingCandidates);
            }
        }

        if(xWingPossibles.size() < 2){
            return false;
        }

        for (int i = 0; i < xWingPossibles.size(); i++) {
            for (int j = i + 1; j < xWingPossibles.size(); j++) {
                //this means we have an X-wing and can remove the possible from the rows in cells not in the X-wing
                if(xWingPossibles.get(i).getFirst().getRow() == xWingPossibles.get(j).getFirst().getRow() &&
                        xWingPossibles.get(i).getLast().getRow() == xWingPossibles.get(j).getLast().getRow()){

                    int row1 = xWingPossibles.get(i).getFirst().getRow();
                    int row2 = xWingPossibles.get(j).getLast().getRow();
                    int column1 = xWingPossibles.get(i).getFirst().getColumn();
                    int column2 = xWingPossibles.get(j).getFirst().getColumn();

                    for (int column = 0; column < 9; column++) {
                        if(!(column == column1 || column == column2)){
                            if(sudokuBoard[row1][column].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                            if(sudokuBoard[row2][column].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }
}
