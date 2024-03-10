package solver.sudokuteacher.SolvingStrategies;


import solver.sudokuteacher.SolvingStrategiesModels.StrategyModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

public abstract class SolvingStrategy {
    Sudoku sudoku;
    Cell[][] sudokuBoard;
    boolean executeStrategy;
    ArrayList<StrategyModel> strategyModels;

    public SolvingStrategy(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.sudokuBoard = sudoku.getSudoku();
        this.strategyModels = new ArrayList<>();
        executeStrategy = true;
    }

    public void setExecuteStrategy(boolean executeStrategy) {
        this.executeStrategy = executeStrategy;
    }

    public abstract boolean executeStrategy();
    public abstract boolean findValidExecutions();

    public ArrayList<StrategyModel> getStrategyModels(){
        return strategyModels;
    };
}
