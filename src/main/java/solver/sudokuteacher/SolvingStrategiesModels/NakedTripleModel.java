package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public class NakedTripleModel extends StrategyModel{

    ArrayList<Cell> nakedTripleCells;
    ArrayList<Integer> strategyPossibles;
    String unit;

    public NakedTripleModel(String unit){
        super("Naked Triple");
        nakedTripleCells = new ArrayList<>(3);
        strategyPossibles = new ArrayList<>(3);
        this.unit = unit;
    }

    @Override
    public void draw(CellController[][] sudokuCells){

    }

    @Override
    public String toString(){
        return "";
    }
}
