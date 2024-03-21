package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public class NakedPairModel extends StrategyModel{
    Cell cell1;
    Cell cell2;
    ArrayList<Cell> affectedCells;
    ArrayList<Integer> possiblesThatMakeUpStrategy;

    public NakedPairModel(String strategyName) {
        super(strategyName);
        affectedCells = new ArrayList<>();
        possiblesThatMakeUpStrategy = new ArrayList<>();
    }

    public ArrayList<Cell> getAffectedCells(){
        return affectedCells;
    }

    public void setCell1(Cell cell1) {
        this.cell1 = cell1;
    }

    public void setCell2(Cell cell2){
        this.cell2 = cell2;
    }

    public ArrayList<Integer> getPossiblesThatMakeUpStrategy() {
        return possiblesThatMakeUpStrategy;
    }
    @Override
    public void draw(CellController[][] sudokuCells){
        sudokuCells[cell1.getRow()][cell1.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(0), Color.GREEN);
        sudokuCells[cell1.getRow()][cell1.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(1), Color.GREEN);
        sudokuCells[cell2.getRow()][cell2.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(0), Color.GREEN);
        sudokuCells[cell2.getRow()][cell2.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(1), Color.GREEN);

        for (Cell cell: affectedCells) {
            if (cell.getPossibilities().contains(possiblesThatMakeUpStrategy.get(0))){
                sudokuCells[cell.getRow()][cell.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(0), Color.YELLOW);
            }
            if(cell.getPossibilities().contains(possiblesThatMakeUpStrategy.get(1))){
                sudokuCells[cell.getRow()][cell.getColumn()].highlightPossible(possiblesThatMakeUpStrategy.get(1), Color.YELLOW);
            }
        }

    }
    @Override
    public String toString(){
        return (getStrategyName() + " found in Cells " + cell1.toString() + " & " + cell2.toString() +
                " with possibles (" + possiblesThatMakeUpStrategy.get(0) + "," + possiblesThatMakeUpStrategy.get(1) + ")");
    }
}
