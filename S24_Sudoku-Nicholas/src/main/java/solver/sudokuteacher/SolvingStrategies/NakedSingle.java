package solver.sudokuteacher.SolvingStrategies;
import solver.sudokuteacher.SolvingStrategiesModels.NakedSingleModel;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class NakedSingle extends SolvingStrategy{


    public NakedSingle(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {

        boolean flag = false;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudokuBoard[row][column].getPossibilities().size() == 1) {
                    if(executeStrategy) {
                        sudoku.updateCellSolution(sudokuBoard[row][column], sudokuBoard[row][column].getPossibilities().get(0));
                    }else{
                        NakedSingleModel nakedSingle= new NakedSingleModel();
                        nakedSingle.setStrategyFindsSolution(true);
                        nakedSingle.setCellWithSolution(sudokuBoard[row][column]);
                        nakedSingle.setCellSolution(sudokuBoard[row][column].getPossibilities().get(0));
                        strategyModels.add(nakedSingle);
                    }
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean findValidExecutions(){
        executeStrategy = false;
        boolean strategyFound = executeStrategy();
        executeStrategy = true;
        return strategyFound;
    }

}
