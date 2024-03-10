package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

public class Bug extends SolvingStrategy{


    public Bug(Sudoku sudoku) {
        super(sudoku);
    }
    @Override
    public boolean executeStrategy(){
        boolean flag = false;
        int count = 0;
        Cell bugCell = null;
        int possibleThatKillsBug = 0;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().size() == 2){
                    continue;
                }else if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().size() == 3){
                    bugCell = sudokuBoard[row][column];
                    count++;
                }

                if(count > 1 || sudokuBoard[row][column].getPossibilities().size() > 3){
                    return false;
                }
            }
        }
        if(count == 0){
            return false;
        }

        // ArrayList<Integer> bugCellPossibilities = new ArrayList<>(sudoku[bugCell[0]][bugCell[1]].getPossibilities());
        int[] boxTop = sudoku.getBox(bugCell.getRow(), bugCell.getColumn());

        for (Integer possible: bugCell.getPossibilities()) {
            int boxCount = sudoku.getBoxCellsWithPossibility(boxTop, possible).size();
            int rowCount = 0;
            int columnCount = 0;

            int bugRow = bugCell.getRow();
            int bugColumn = bugCell.getColumn();
            for (int i = 0; i < 9; i++) {
                if(sudokuBoard[bugRow][i].getSolution() == 0 && sudokuBoard[bugRow][i].getPossibilities().contains(possible)){
                    rowCount++;
                }
                if(sudokuBoard[i][bugColumn].getSolution() == 0 && sudokuBoard[i][bugColumn].getPossibilities().contains(possible)){
                    columnCount++;
                }
            }

            if(boxCount == 3 || columnCount == 3 || rowCount == 3){
                possibleThatKillsBug = possible;
                break;
            }
        }

        int finalPossibleThatKillsBug = possibleThatKillsBug;
        if(bugCell.getPossibilities().removeIf(p -> p != finalPossibleThatKillsBug)){
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

}
