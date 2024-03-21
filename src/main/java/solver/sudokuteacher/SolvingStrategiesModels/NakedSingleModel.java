package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

public class NakedSingleModel extends StrategyModel {

    Cell cell;
    int row;
    int column;
    int solution;

    public NakedSingleModel(Cell cell, int solution) {
        super("Naked Single");
        this.cell = cell;
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.solution = solution;
    }

    @Override
    public void draw(CellController[][] sudokuCells){
        sudokuCells[row][column].highlightPossible(solution, Color.LIGHTGREEN);
    }

    @Override
    public String toString(){
        return (getStrategyName() + " found in Cell " + cell.toString()  +" Solution is: " + solution);
    }

}
