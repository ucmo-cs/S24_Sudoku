package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;
import java.util.LinkedList;

public class NakedQuadModel extends StrategyModel{


    LinkedList<Cell> nakedQuadCells;
    ArrayList<Integer> strategyPossibles;
    ArrayList<Cell> affectedCells;

    public NakedQuadModel(String strategyName) {
        super(strategyName);
        nakedQuadCells = new LinkedList<>();
        strategyPossibles = new ArrayList<>(4);
        affectedCells = new ArrayList<>();
    }

    @Override
    public void draw(CellController[][] sudokuCells) {
        for (Cell cell: nakedQuadCells) {
            int row = cell.getRow();
            int column = cell.getColumn();
            for (Integer possible: cell.getPossibilities()) {
                sudokuCells[row][column].highlightPossible(possible, Color.LIGHTGREEN);
            }
        }

        for (Cell cell: affectedCells ) {
            int row = cell.getRow();
            int column = cell.getColumn();
            for (Integer possible: strategyPossibles) {
                if(cell.getPossibilities().contains(possible)){
                    sudokuCells[row][column].highlightPossible(possible, Color.YELLOW);
                }
            }
        }
    }

    @Override
    public String toString() {

        return (getStrategyName() + " found in cells: " + nakedQuadCells.get(0).toString() + " " + nakedQuadCells.get(1).toString()
                + " " + nakedQuadCells.get(2).toString() + " " + nakedQuadCells.get(3).toString() + " with possibles: (" + strategyPossibles.get(0) + "," + strategyPossibles.get(1) +
                "," + strategyPossibles.get(2) + "," + strategyPossibles.get(3) + ")");

    }

    public void setNakedQuadCells(LinkedList<Cell> nakedQuadCells) {
        this.nakedQuadCells = nakedQuadCells;
    }

    public void setStrategyPossibles(ArrayList<Integer> strategyPossibles) {
        this.strategyPossibles = strategyPossibles;
    }

    public void setAffectedCells(ArrayList<Cell> affectedCells) {
        this.affectedCells = affectedCells;
    }
}
