package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public class NakedCandidateModel extends StrategyModel{
    ArrayList<Cell> cellsThatMakeUpStrategy;

    public NakedCandidateModel(String strategyName) {
        super(strategyName);
        ArrayList<Cell> cellsThatMakeUpStrategy = new ArrayList<>();

    }

    @Override
    public void draw(CellController[][] sudokuCells){

    }
    @Override
    public String toString(){
        return (getStrategyName() + " found in Cells ");
    }
}
