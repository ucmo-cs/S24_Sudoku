package solver.sudokuteacher;

import solver.sudokuteacher.SolvingStrategiesModels.StrategyModel;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;

public class SudokuModelTester {
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku("000004028406000005100030600000301000087000140000709000002010003900000507670400000");
        ArrayList<StrategyModel> nextStrategy = sudoku.getNextStrategy();
        for (StrategyModel strategy: nextStrategy) {
            System.out.println(strategy.toString());
        }
    }
}
