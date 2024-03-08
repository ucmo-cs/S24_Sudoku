package solver.sudokuteacher.SolvingStrategies;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class NakedSingle extends SolvingStrategy{

    public NakedSingle(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {

        boolean flag = false;
        sudokuBoard = sudoku.getSudoku();

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudokuBoard[row][column].getPossibilities().size() == 1) {
                    sudoku.updateCellSolution( sudokuBoard[row][column], sudokuBoard[row][column].getPossibilities().get(0));
                    flag = true;
                }
            }
        }
        return flag;
    }
}
