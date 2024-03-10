package solver.sudokuteacher.SolvingStrategiesModels;

public class NakedSingleModel extends StrategyModel {

    public NakedSingleModel() {
        super("Naked Single");
    }

    @Override
    public String toString(){
        return (getStrategyName() + " found in Cell " + getCellWithSolution().toString()  +" Solution is: " + getCellSolution());
    }

}
