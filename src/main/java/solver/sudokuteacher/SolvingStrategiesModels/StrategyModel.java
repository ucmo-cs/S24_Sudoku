package solver.sudokuteacher.SolvingStrategiesModels;

import gui.sudokuteacher.controllers.CellController;

public abstract class StrategyModel {

    private String strategyName;
    private boolean strategyFindsSolution;

    public StrategyModel(String strategyName) {
        this.strategyName = strategyName;

    }

    public abstract void draw(CellController[][] sudokuCells);

    public String getStrategyName() {
        return strategyName;
    }


    public void setStrategyFindsSolution(boolean strategyFindsSolution) {
        this.strategyFindsSolution = strategyFindsSolution;
    }

}
