package solver.sudokuteacher;
import java.util.ArrayList;

public class Cell  {

    private volatile int solution;
    private volatile ArrayList<Integer> possibilities = new ArrayList<>();
    private int row;
    private int column;

    public Cell(int value, int row, int column ) {

        this.solution = value;
        if(this.solution == 0){
            for (int i = 0; i < 9; i++) {
                this.possibilities.add(i + 1);
            }
        }
        this.row = row;
        this.column = column;

    }
    public Cell(Cell cell, int row, int column){
        this.solution = cell.solution;
        this.possibilities = cell.possibilities;
        this.row = row;
        this.column = column;
    }

    public Cell(){}
    public ArrayList<Integer> getPossibilities(){
        return possibilities;
    }
    public void setRow(int row){
        this.row = row;
    }
    public void setColumn(int column){
        this.column = column;
    }

    public void setSolution(int val){
        this.solution = val;
    }
    public int getSolution(){
        return solution;
    }
    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }


}
