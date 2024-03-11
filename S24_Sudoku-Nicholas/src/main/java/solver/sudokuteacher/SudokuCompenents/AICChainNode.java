package solver.sudokuteacher.SudokuCompenents;


import java.util.ArrayList;


public class AICChainNode {

    private final Cell nodeCell;
    private final ArrayList<AICChainNode> strongLinkConnections;



    private final ArrayList<AICChainNode> weakLinkConnections;
    private final int strongLinkPossible;



    public AICChainNode(Cell cell, int possible) {
        nodeCell = cell;
        strongLinkPossible = possible;
        strongLinkConnections = new ArrayList<>();
        weakLinkConnections = new ArrayList<>();
    }


    public Cell getNodeCell() {
        return nodeCell;
    }

    public ArrayList<AICChainNode> getStrongLinkConnections() {
        return strongLinkConnections;
    }
    public ArrayList<AICChainNode> getWeakLinkConnections() {return weakLinkConnections;}
    public int getStrongLinkPossible() {
        return strongLinkPossible;
    }
}
