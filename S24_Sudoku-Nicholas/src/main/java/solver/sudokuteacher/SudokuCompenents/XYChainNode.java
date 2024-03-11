package solver.sudokuteacher.SudokuCompenents;

import java.util.ArrayList;

public class XYChainNode {
    private final Cell cell;
    private final int startPossible;
    private final int endPossible;
    private XYChainNode strongLink;
    private ArrayList<Cell> cellsSeenWithStartPossible;

    public XYChainNode(Cell cell, int currentPossible){
        this.cell = cell;
        this.startPossible = currentPossible;

        if(cell.getPossibilities().get(0) == currentPossible){
            endPossible = cell.getPossibilities().get(1);
        }else{
            endPossible = cell.getPossibilities().get(0);
        }
    }

    public XYChainNode(Cell cell, int startPossible, int endPossible){
        this.cell = cell;
        this.startPossible = startPossible;
        this.endPossible = endPossible;
        this.cellsSeenWithStartPossible = new ArrayList<>();
    }

    public int getEndPossible(){
        return this.endPossible;
    }
    public int getStartPossible(){
        return this.startPossible;
    }
    public XYChainNode getStrongLink() {return strongLink;}
    public void setStrongLink(XYChainNode strongLink) {this.strongLink = strongLink;}
    public ArrayList<Cell> getCellsSeenWithStartPossible() {return cellsSeenWithStartPossible;}
    public Cell getCell(){return this.cell;}
}
