package solver.sudokuteacher.SolvingStrategies;
import solver.sudokuteacher.SolvingStrategiesModels.HiddenSingleModel;
import solver.sudokuteacher.SolvingStrategiesModels.NakedSingleModel;
import solver.sudokuteacher.SolvingStrategiesModels.StrategyModel;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class HiddenSingle extends SolvingStrategy{


    public HiddenSingle(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy() {

        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if(hiddenSingleRowHelper(i)){
                flag = true;
            }
            if(hiddenSingleColumnHelper(i)){
                flag = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(hiddenSingleBoxHelper(i * 3, j * 3)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    private boolean hiddenSingleRowHelper(int row){
        boolean flag = false;
        Cell cell = new Cell();

        for (int possible = 1; possible <= 9; possible++) {
            int count = 0;
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getPossibilities().contains(possible)){
                    count++;
                    cell = sudokuBoard[row][column];
                }
                if (count > 1){
                    break;
                }
            }
            //hidden single
            if(count == 1){
                if(executeStrategy) {
                    sudoku.updateCellSolution(cell, possible);
                }else{
                    strategyModels.add(new HiddenSingleModel(cell, possible, "row"));
                }
                flag = true;
            }
        }
        return flag;
    }
    private boolean hiddenSingleColumnHelper(int column){
        boolean flag = false;

        Cell cell = new Cell();
        for (int possible = 1; possible <= 9; possible++) {
            int count = 0;
            for (int row = 0; row < 9; row++) {
                if(sudokuBoard[row][column].getPossibilities().contains(possible)){
                    count++;
                    cell = sudokuBoard[row][column];
                }
                if (count > 1){
                    break;
                }
            }
            //hidden single
            if(count == 1){
                if(executeStrategy) {
                    sudoku.updateCellSolution(cell, possible);
                }else{
                    strategyModels.add(new HiddenSingleModel(cell, possible, "Column"));
                }
                flag = true;
            }
        }
        return flag;
    }

    private boolean hiddenSingleBoxHelper(int rowTop, int columnTop){
        boolean flag = false;

        Cell cell = new Cell();
        for (int possible = 1; possible <= 9; possible++) {
            int count = 0;
            for (int boxRow = rowTop; boxRow < rowTop + 3; boxRow++) {
                for (int boxColumn = columnTop; boxColumn < columnTop + 3; boxColumn++) {
                    if(sudokuBoard[boxRow][boxColumn].getPossibilities().contains(possible)){
                        count++;
                        cell = sudokuBoard[boxRow][boxColumn];
                    }
                    if(count > 1){
                        break;
                    }
                }
            }
            if(count == 1){
                if(executeStrategy) {
                    sudoku.updateCellSolution(cell, possible);
                }else{
                    strategyModels.add(new HiddenSingleModel(cell, possible, "Box"));
                }
                flag = true;
            }
        }

        return flag;
    }
}

