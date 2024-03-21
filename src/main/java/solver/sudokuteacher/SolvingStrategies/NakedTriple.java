package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class NakedTriple extends SolvingStrategy{

    public NakedTriple(Sudoku sudoku){super(sudoku);}

    @Override
    public boolean executeStrategy() {
        return false;
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
