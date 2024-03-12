package solver.sudokuteacher.SudokuCompenents;
import gui.sudokuteacher.CellController;

import java.util.ArrayList;

public class Cell  {
    CellController cellController;
    private int solution;
    private final ArrayList<Integer> possibilities = new ArrayList<>();
    private int row;
    private int column;

    public Cell(int value, int row, int column ) {

        this.solution = value;
        if(this.solution == 0){
            for (int i = 0; i < 9; i++) {
                this.possibilities.add(i + 1);
            }
        }
        this.row = row;
        this.column = column;
    }

    public Cell(){}
    public void setCellController(CellController cellController) {this.cellController = cellController;}
    public ArrayList<Integer> getPossibilities(){
        return possibilities;
    }
    public void setSolution(int solution){
        if(cellController != null){
            cellController.updateCellSolution(solution);
        }
        this.solution = solution;
    }
    public int getSolution(){
        return solution;
    }
    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    @Override
    public String toString(){
        return ("[" + (row + 1) + "," + (column + 1) + "]");
    }

}
