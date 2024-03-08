package solver.sudokuteacher.SolvingStrategies;


import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public abstract class SolvingStrategy {
    Sudoku sudoku;
    Cell[][] sudokuBoard;

    public SolvingStrategy(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.sudokuBoard = sudoku.getSudoku();
    }

    abstract boolean executeStrategy();

}
