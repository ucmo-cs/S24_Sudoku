package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public class HiddenCandidateModel extends StrategyModel{


    ArrayList<Cell> hiddenCandidateCells;
    ArrayList<Integer> hiddenCandidatePossibles;

    public HiddenCandidateModel(String strategyName) {
        super(strategyName);
        hiddenCandidateCells = new ArrayList<>();
        hiddenCandidatePossibles = new ArrayList<>();
    }

    @Override
    public void draw(CellController[][] sudokuCells) {
        for (Cell cell: hiddenCandidateCells) {
            int row = cell.getRow();
            int column = cell.getColumn();
            for (Integer possible: cell.getPossibilities()) {
                if(hiddenCandidatePossibles.contains(possible)){
                    sudokuCells[row][column].highlightPossible(possible, Color.LIGHTGREEN);
                }else{
                    sudokuCells[row][column].highlightPossible(possible, Color.YELLOW);
                }
            }
        }
    }

    public void setHiddenCandidateCells(ArrayList<Cell> hiddenCandidateCells) {
        this.hiddenCandidateCells = hiddenCandidateCells;
    }

    public void setHiddenCandidatePossibles(ArrayList<Integer> hiddenCandidatePossibles) {
        this.hiddenCandidatePossibles = hiddenCandidatePossibles;
    }
    @Override
    public String toString(){
        String cells = "";
        String possibles = "(";
        for (int i = 0; i < hiddenCandidateCells.size(); i++) {
            cells += hiddenCandidateCells.get(i).toString() + " ";
        }
        for (int i = 0; i < hiddenCandidatePossibles.size(); i++) {
            possibles += hiddenCandidatePossibles.get(i).toString();
            if(i < hiddenCandidatePossibles.size() - 1){
                possibles += ",";
            }
        }
        possibles += ')';

        return (getStrategyName()) + " found in cells: " + cells + "with possibles " + possibles;
    }
}
