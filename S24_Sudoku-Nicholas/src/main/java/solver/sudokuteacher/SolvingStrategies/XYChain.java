package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;
import solver.sudokuteacher.SudokuCompenents.XYChainNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class XYChain extends SolvingStrategy{

    public XYChain(Sudoku sudoku) {
       super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        ArrayList<Cell> xyChainCandidates = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().size() == 2) {
                    xyChainCandidates.add(sudokuBoard[row][column]);
                }
            }
        }

        ArrayList<XYChainNode> xyChainNodes = createXYNodes(xyChainCandidates);
        for (XYChainNode chainStart: xyChainNodes) {

            if(chainStart.getCell().getPossibilities().size() != 2){
                continue;
            }
            LinkedList<ArrayList<XYChainNode>> xyChains = xyChainBuilder(chainStart, xyChainNodes);

            if(xyChains.size() < 3){
                continue;
            }

            ArrayList<XYChainNode> possibleEndNodes = findChainEnds(chainStart.getStartPossible(), xyChains);

            for (XYChainNode chainEnd: possibleEndNodes) {
                LinkedList<Cell> chain = extractXYChain(chainEnd, xyChains);

                if( chain.getFirst() != chainStart.getCell() || chain.size() < 3){
                    continue;
                }
                if(findXYChainEliminations(chain, chainStart.getStartPossible())){
                    flag = true;
                }
            }

        }


        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private ArrayList<XYChainNode> createXYNodes(ArrayList<Cell> xyChainCandidates){
        ArrayList<XYChainNode> xyChainNodes = new ArrayList<>();
        for (Cell biValueCell: xyChainCandidates) {
            int possible1 = biValueCell.getPossibilities().get(0);
            int possible2 = biValueCell.getPossibilities().get(1);

            XYChainNode node1 = new XYChainNode(biValueCell, possible1, possible2);
            XYChainNode node2 = new XYChainNode(biValueCell, possible2, possible1);

            node1.setStrongLink(node2);
            node2.setStrongLink(node1);

            xyChainNodes.add(node1);
            xyChainNodes.add(node2);
        }

        for (XYChainNode node: xyChainNodes) {
            int startPossible = node.getStartPossible();
            Cell nodeCell = node.getCell();
            ArrayList<Cell> seenCells =  node.getCellsSeenWithStartPossible();
            for (Cell cell: xyChainCandidates) {
                if(cell != nodeCell && cell.getPossibilities().contains(startPossible) && sudoku.doCellsSeeEachOther(nodeCell, cell)){
                    seenCells.add(cell);
                }
            }
        }
        return xyChainNodes;
    }

    private LinkedList<ArrayList<XYChainNode>> xyChainBuilder(XYChainNode startNode, ArrayList<XYChainNode> xyChainNodes){
        LinkedList<ArrayList<XYChainNode>> xyChains = new LinkedList<>();
        ArrayList<XYChainNode> startLevel = new ArrayList<>(1);
        startLevel.add(startNode);
        xyChains.add(startLevel);
        ArrayList<XYChainNode> nodesAddedToChain = new ArrayList<>();
        nodesAddedToChain.add(startNode);

        boolean cellAdded;
        boolean getStrongLink = true;

        do {
            cellAdded = false;
            ArrayList<XYChainNode> nextChainLevel = new ArrayList<>();
            for (XYChainNode node: xyChains.getLast()) {
                if(getStrongLink){
                    if(!nodesAddedToChain.contains(node.getStrongLink()) && node.getStrongLink().getCell().getPossibilities().size() == 2){
                        nextChainLevel.add(node.getStrongLink());
                        nodesAddedToChain.add(node.getStrongLink());
                        cellAdded = true;
                    }
                }else{
                    for (XYChainNode nextNode: xyChainNodes) {
                        if( nextNode.getStrongLink().getCell().getPossibilities().size() != 2){
                            continue;
                        }
                        if(!nodesAddedToChain.contains(nextNode) &&
                                node.getStartPossible() == nextNode.getStartPossible() &&
                                node.getCellsSeenWithStartPossible().contains(nextNode.getCell())){
                            nextChainLevel.add(nextNode);
                            nodesAddedToChain.add(nextNode);
                            cellAdded = true;
                        }
                    }
                }
            }
            if(nextChainLevel.size() > 0) {
                xyChains.add(nextChainLevel);
            }
            getStrongLink = !getStrongLink;
        }while(cellAdded);

        if(!getStrongLink){
            xyChains.removeLast();
        }

        return xyChains;
    }

    private ArrayList<XYChainNode> findChainEnds(int startPossible, LinkedList<ArrayList<XYChainNode>> xyChains){
        ArrayList<XYChainNode> chainEnds = new ArrayList<>();

        for (int i = 3; i < xyChains.size(); i += 2) {
            for (XYChainNode xyChainNode: xyChains.get(i)) {
                if(xyChainNode.getStartPossible() == startPossible){
                    chainEnds.add(xyChainNode);
                }
            }
        }

        return chainEnds;
    }




    private boolean findXYChainEliminations(LinkedList<Cell> xyChain, int possible){
        boolean flag = false;
        Cell chainStart = xyChain.getFirst();
        Cell chainEnd = xyChain.getLast();

        ArrayList<Cell> cellsSeenByStart = sudoku.cellsSeenHelper(chainStart);
        ArrayList<Cell> cellsSeenByStartAndEnd = new ArrayList<>();

        for (Cell cell: cellsSeenByStart) {
            if (cell.getPossibilities().contains(possible) && sudoku.doCellsSeeEachOther(cell, chainEnd) && !xyChain.contains(cell) ){
                cellsSeenByStartAndEnd.add(cell);
            }
        }

        for (Cell cell: cellsSeenByStartAndEnd) {
            cell.getPossibilities().remove(Integer.valueOf(possible));
            flag = true;
        }

        return flag;
    }


    private LinkedList<Cell> extractXYChain(XYChainNode chainEnd, LinkedList<ArrayList<XYChainNode>> xyChainNodes){
        LinkedList<XYChainNode> nodesInChain = new LinkedList<>();
        LinkedList<Cell> xyChain = new LinkedList<>();
        nodesInChain.addLast(chainEnd);
        int depth = getXYChainEndNodeDepth(chainEnd, xyChainNodes);

        for (int i = depth - 1 ; i >= 0; i--) {
            if(i % 2 == 0){
                nodesInChain.addFirst(nodesInChain.getFirst().getStrongLink());
            }else{
                int startPossible = nodesInChain.getFirst().getStartPossible();
                for (XYChainNode chainNode: xyChainNodes.get(i)) {
                    if(chainNode.getStartPossible() == startPossible && !nodesInChain.contains(chainNode) &&
                            sudoku.doCellsSeeEachOther(nodesInChain.getFirst().getCell(), chainNode.getCell())){
                        nodesInChain.addFirst(chainNode);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < nodesInChain.size(); i += 2) {
            xyChain.add(nodesInChain.get(i).getCell());
        }

        return  xyChain;
    }


    private int getXYChainEndNodeDepth(XYChainNode chainEnd, LinkedList<ArrayList<XYChainNode>> xyChainNodes){
        int depth = 0;

        for (ArrayList<XYChainNode> chainNode: xyChainNodes) {
            for (XYChainNode node: chainNode) {
                if(node == chainEnd){
                    return depth;
                }
            }
            depth++;
        }

        return depth;
    }
}
