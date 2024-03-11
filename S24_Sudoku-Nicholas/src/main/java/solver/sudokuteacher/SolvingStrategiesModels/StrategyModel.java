package solver.sudokuteacher.SolvingStrategiesModels;

import solver.sudokuteacher.SudokuCompenents.Cell;

import java.util.ArrayList;

public abstract class StrategyModel {
    private ArrayList<Cell> strategyCells;
    private ArrayList<Integer> strategyPossibles;
    private ArrayList<Cell> affectedCells;
    private boolean strategyFindsSolution;
    private Cell cellWithSolution;
    private int cellSolution;
    private String strategyName;

    public StrategyModel(String strategyName) {
        this.strategyName = strategyName;
        strategyCells = new ArrayList<>();
        strategyPossibles = new ArrayList<>();
        affectedCells = new ArrayList<>();
        strategyFindsSolution = false;
        cellWithSolution = null;
        cellSolution = 0;
    }

    public ArrayList<Cell> getStrategyCells() {
        return strategyCells;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public ArrayList<Integer> getStrategyPossibles() {
        return strategyPossibles;
    }


    public ArrayList<Cell> getAffectedCells() {
        return affectedCells;
    }


    public boolean isStrategyFindsSolution() {
        return strategyFindsSolution;
    }

    public void setStrategyFindsSolution(boolean strategyFindsSolution) {
        this.strategyFindsSolution = strategyFindsSolution;
    }

    public Cell getCellWithSolution() {
        return cellWithSolution;
    }

    public void setCellWithSolution(Cell cellWithSolution) {
        this.cellWithSolution = cellWithSolution;
    }

    public int getCellSolution() {
        return cellSolution;
    }

    public void setCellSolution(int solution) {
        this.cellSolution = solution;
    }


}
