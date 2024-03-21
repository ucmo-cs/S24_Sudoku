package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;
import java.util.LinkedList;

public class NakedTripleModel extends StrategyModel{

    LinkedList<Cell> nakedTripleCells;
    ArrayList<Integer> strategyPossibles;
    ArrayList<Cell> affectedCells;
    String unit;

    public NakedTripleModel(){
        super("Naked Triple");
        nakedTripleCells = new LinkedList<>();
        strategyPossibles = new ArrayList<>(3);
        affectedCells = new ArrayList<>();

    }

    @Override
    public void draw(CellController[][] sudokuCells){
        for (Cell cell: nakedTripleCells) {
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

        return (getStrategyName() + " found in cells: " + nakedTripleCells.get(0).toString() + " " + nakedTripleCells.get(1).toString()
        + " " + nakedTripleCells.get(2).toString() + " with possibles: (" + strategyPossibles.get(0) + "," + strategyPossibles.get(1) +
                "," + strategyPossibles.get(2) + ")");

    }

    public LinkedList<Cell> getNakedTripleCells() {
        return nakedTripleCells;
    }

    public ArrayList<Integer> getStrategyPossibles() {
        return strategyPossibles;
    }

    public ArrayList<Cell> getAffectedCells() {
        return affectedCells;
    }

    public void setNakedTripleCells(LinkedList<Cell> nakedTripleCells) {
        this.nakedTripleCells = nakedTripleCells;
    }

    public void setStrategyPossibles(ArrayList<Integer> strategyPossibles) {
        this.strategyPossibles = strategyPossibles;
    }

    public void setAffectedCells(ArrayList<Cell> affectedCells) {
        this.affectedCells = affectedCells;
    }
}
