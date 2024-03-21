package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

public class HiddenSingleModel extends StrategyModel {
    Cell cell;
    int solution;
    String unit;

    public HiddenSingleModel(Cell cell, int solution, String unit) {
        super("Hidden Single");
        setStrategyFindsSolution(true);
        this.cell = cell;
        this.solution = solution;
        this.unit = unit;
    }

    @Override
    public void draw(CellController[][] sudokuCells){
        CellController cellController = sudokuCells[cell.getRow()][cell.getColumn()];
        for (Integer possible: cellController.getCellModel().getPossibilities()) {
            if(possible == solution){
                cellController.highlightPossible(solution, Color.LIGHTGREEN);
            }else{
                cellController.highlightPossible(possible, Color.YELLOW);
            }
        }
    }

    @Override
    public String toString(){

        return (getStrategyName() + " found in " + unit + " in cell " +
                cell.toString() + " Solution is: " + solution);

    }
}
