package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public class PointingPairAndTripleModel extends StrategyModel{

    ArrayList<Cell> strategyCells;
    ArrayList<Cell> affectedCells;
    int strategyPossible;

    public PointingPairAndTripleModel(String strategyName) {
        super(strategyName);
    }

    @Override
    public void draw(CellController[][] sudokuCells) {
        for (Cell cell: strategyCells) {
            cell.getCellController().highlightPossible(strategyPossible, Color.LIGHTGREEN);
        }
        for (Cell cell: affectedCells) {
            cell.getCellController().highlightPossible(strategyPossible, Color.YELLOW);
        }
    }

    @Override
    public String toString(){
        String cells = "";
        String possible = (" (" + strategyPossible + ") ");

        for (int i = 0; i < strategyCells.size(); i++) {
            cells +=  strategyCells.get(i).toString() + " ";
        }
        return (getStrategyName() + " found in cells: " + cells + "with possible" + possible);
    }

    public void setStrategyCells(ArrayList<Cell> strategyCells) {
        this.strategyCells = strategyCells;
    }

    public void setAffectedCells(ArrayList<Cell> affectedCells) {
        this.affectedCells = affectedCells;
    }

    public void setStrategyPossible(int strategyPossible) {
        this.strategyPossible = strategyPossible;
    }
}
