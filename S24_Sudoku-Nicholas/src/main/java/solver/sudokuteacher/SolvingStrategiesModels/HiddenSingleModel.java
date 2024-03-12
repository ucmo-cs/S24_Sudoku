package solver.sudokuteacher.SolvingStrategiesModels;

public class HiddenSingleModel extends StrategyModel {

    public HiddenSingleModel() {
        super("Hidden Single");
    }

    @Override
    public String toString(){
        String unit;
        String unitNumber;
        if(getStrategyCells().get(0).getRow() == getStrategyCells().get(8).getRow()){
            unit = "Row";
            unitNumber = Integer.toString(getStrategyCells().get(0).getRow() + 1);
        }else if(getStrategyCells().get(0).getColumn() == getStrategyCells().get(8).getColumn()){
            unit = "Column";
            unitNumber =  Integer.toString(getStrategyCells().get(0).getColumn() + 1);
        }else{
            unit = "Box";
            unitNumber = ("[" + getStrategyCells().get(0).getRow() + "," + getStrategyCells().get(0).getColumn() +"]");
        }

        return (getStrategyName() + " found in " + unit + " " + unitNumber  + " in cell " + getCellWithSolution().toString() + " Solution is: " + getCellSolution());
    }
}
