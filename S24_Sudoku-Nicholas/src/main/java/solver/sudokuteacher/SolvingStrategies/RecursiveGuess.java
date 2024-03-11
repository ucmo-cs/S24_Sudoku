package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

//FIXME: Recursive solution solves boards properly when used with nakedSingle strategy but doesn't work when used by itself
//      At the end of the call stack it has the solution, but somewhere in the return it reverts to an unsolved state
public class RecursiveGuess extends SolvingStrategy{


    public RecursiveGuess(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {
        String currentState = sudoku.toString();
        Cell cell = new Cell();
        boolean cellFound = false;
        for (int i = 2; i <= 9; i++) {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().size() == i) {

                        cell = sudokuBoard[row][column];
                        cellFound = true;
                        break;
                    }
                }
                if (cellFound) {
                    break;
                }
            }
        }

        ArrayList<Integer> possibles = new ArrayList<>(cell.getPossibilities());

        for (int i = 0; i < cell.getPossibilities().size(); i++) {
            sudoku.updateCellSolution(cell, cell.getPossibilities().get(i));
            if (sudoku.solve()) {
                return true;
            }else{
                sudoku = new Sudoku(currentState);
                sudokuBoard = sudoku.getSudoku();
                cell = sudoku.getCell(cell.getRow(), cell.getColumn());
            }
        }

        return !sudoku.isUnsolved();
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

}
