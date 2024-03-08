package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.AICChainNode;
import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
//TODO: AIC strategies can be combined with Aligned Pair Exclusion Strategy which is currently unimplemented

public class AlternatingInferenceChain extends SolvingStrategy{

    private ArrayList<ArrayList<AICChainNode>> aicPossibleChainLinks;

    public AlternatingInferenceChain(Sudoku sudoku) {
        super(sudoku);
    }

    public boolean executeStrategy(){
        boolean flag = false;
        aicPossibleChainLinks = new ArrayList<>();
        createAICNodes();
        findLinksForAICNodes();

        for (ArrayList<AICChainNode> chainNodePossibleList: aicPossibleChainLinks) {
            for (AICChainNode aicNode: chainNodePossibleList) {
                LinkedList<LinkedList<AICChainNode>> aicChain = aicChainBuilder(aicNode);

                if(aicChain.size() > 3){
                    if(findAicChainEliminations(aicChain)){
                        flag = true;
                    }
                }
            }
        }


        return flag;
    }

    private boolean findAicChainEliminations(LinkedList<LinkedList<AICChainNode>> aicChain){
        boolean flag = false;
        ArrayList<AICChainNode> aicChainEnds = findAicChainEnds(aicChain);
        AICChainNode chainStart = aicChain.getFirst().getFirst();
        int chainStartPossible = chainStart.getStrongLinkPossible();

        for (AICChainNode chainEnd: aicChainEnds) {

            LinkedList<AICChainNode> validAicChain = extractAicChain(chainEnd, aicChain);

            if(validAicChain.getFirst() != chainStart || validAicChain.size() < 4){
                continue;
            }

            if(chainStart.getNodeCell() == chainEnd.getNodeCell()){
                if(aicChainLoop(validAicChain)){
                    flag = true;
                    break;
                }
            } else if(chainStartPossible != chainEnd.getStrongLinkPossible()){

                if(typeTwoAicChain(chainStart, chainEnd)){
                    flag = true;
                   // break;
                }
            }else if(chainStart.getStrongLinkPossible() == chainEnd.getStrongLinkPossible()){
                    if (typeOneAicChain(chainEnd, validAicChain)) {
                        flag = true;
                    }

            }


        }

        return flag;
    }

    private boolean typeOneAicChain(AICChainNode chainEnd, LinkedList<AICChainNode> aicChain){
        boolean flag = false;

        int startPossible = aicChain.getFirst().getStrongLinkPossible();
        if(chainEnd.getStrongLinkPossible() != startPossible){
            return false;
        }
        ArrayList<Cell> cellsSeenByStart = sudoku.cellsSeenHelper(aicChain.getFirst().getNodeCell());
        ArrayList<Cell> cellsSeenByStartAndEnd = new ArrayList<>();

        for (Cell cell: cellsSeenByStart) {
            if(cell.getPossibilities().contains(startPossible) && !isCellInAicChain(cell, aicChain) && sudoku.doCellsSeeEachOther(cell, chainEnd.getNodeCell())){
                cellsSeenByStartAndEnd.add(cell);
            }
        }

        for (Cell cell: cellsSeenByStartAndEnd) {
            cell.getPossibilities().remove(Integer.valueOf(startPossible));
            flag = true;
        }
        return flag;
    }
    private boolean typeTwoAicChain(AICChainNode chainStart, AICChainNode chainEnd){
        boolean flag = false;
        int chainStartPossible = chainStart.getStrongLinkPossible();

        if(chainEnd.getNodeCell().getPossibilities().contains(chainStartPossible)) {
            if (sudoku.doCellsSeeEachOther(chainEnd.getNodeCell(), chainStart.getNodeCell()) &&
                    (chainEnd.getNodeCell().getPossibilities().contains(chainStartPossible) &&
                            chainStart.getNodeCell().getPossibilities().contains(chainEnd.getStrongLinkPossible()))) {

                chainStart.getNodeCell().getPossibilities().remove(Integer.valueOf(chainEnd.getStrongLinkPossible()));
                chainEnd.getNodeCell().getPossibilities().remove(Integer.valueOf(chainStart.getStrongLinkPossible()));

                flag = true;
            } else if (sudoku.doCellsHaveStrongLink(chainStart.getNodeCell(), chainEnd.getNodeCell(), chainStartPossible)) {
                sudoku.updateCellSolution(chainStart.getNodeCell(), chainStartPossible);
            }
        }else if(sudoku.doCellsSeeEachOther(chainEnd.getNodeCell(), chainStart.getNodeCell()) &&
                chainStart.getNodeCell().getPossibilities().contains(chainEnd.getStrongLinkPossible())){
            chainStart.getNodeCell().getPossibilities().remove(Integer.valueOf(chainEnd.getStrongLinkPossible()));
            flag = true;
        }

        return flag;
    }

    private boolean aicChainLoop(LinkedList<AICChainNode> aicChain){
        boolean flag = chainLoopRemovePossibleInChain(aicChain);

        //TODO: figure out why removing possibles outside of chain is creating invalid Sudoku's
      /*  if(chainLoopRemovePossiblesOutsideChain(aicChain)){
            flag = true;
        }*/


        return flag;
    }

    private boolean chainLoopRemovePossibleInChain(LinkedList<AICChainNode> aicChain){
        boolean flag = false;

        for (int i = 0; i < aicChain.size(); i++) {
            ArrayList<Integer> possiblesInChainInCell = new ArrayList<>();
            AICChainNode currentNode = aicChain.get(i);
            Cell currentCell = currentNode.getNodeCell();
            possiblesInChainInCell.add(currentNode.getStrongLinkPossible());

            if(i > 0 && i < aicChain.size() - 1){
                AICChainNode nextNode = aicChain.get(i + 1);

                if(nextNode.getNodeCell() == currentCell){
                    possiblesInChainInCell.add(nextNode.getStrongLinkPossible());
                    if (currentCell.getPossibilities().removeIf(p -> !possiblesInChainInCell.contains(p))) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean chainLoopRemovePossiblesOutsideChain(LinkedList<AICChainNode> aicChain){
        boolean flag = false;
        ArrayList<Cell> cellsInChain = new ArrayList<>();
        ArrayList<Integer> possiblesInChain = new ArrayList<>();

        for (AICChainNode chainNode: aicChain) {
            if(!cellsInChain.contains(chainNode.getNodeCell())){
                cellsInChain.add(chainNode.getNodeCell());
            }
            if(!possiblesInChain.contains(chainNode.getStrongLinkPossible())){
                possiblesInChain.add(chainNode.getStrongLinkPossible());
            }
        }
        ArrayList<Cell> cellsChainSees = new ArrayList<>();

        for (Cell currentCell: cellsInChain) {
            ArrayList<Cell> currentCellSees = sudoku.cellsSeenHelper(currentCell);
            for (Cell seenCell: currentCellSees) {
                if(!cellsChainSees.contains(seenCell) && !isCellInAicChain(seenCell, aicChain) && cellOutsideChainContainsPossibleInChain(seenCell, possiblesInChain)){
                    cellsChainSees.add(seenCell);
                }
            }
        }

        for (Cell cell: cellsChainSees) {
            ArrayList<Integer> possiblesToRemove = new ArrayList<>();
            for (Integer possible: cell.getPossibilities()) {
                int count = 0;
                for (AICChainNode cellInChain: aicChain) {
                    if(cellInChain.getStrongLinkPossible() == possible && sudoku.doCellsSeeEachOther(cell, cellInChain.getNodeCell())){
                        count++;
                    }
                }
                if(count > 1){
                    possiblesToRemove.add(possible);
                }
            }
            for (Integer possible: possiblesToRemove) {
                cell.getPossibilities().remove(possible);
                flag = true;
            }
        }

        return flag;
    }


    private boolean cellOutsideChainContainsPossibleInChain(Cell cell, ArrayList<Integer> possibles){
        for (Integer possible: cell.getPossibilities()) {
            if(possibles.contains(possible)){
                return true;
            }
        }

        return false;
    }

    private ArrayList<AICChainNode> findAicChainEnds(LinkedList<LinkedList<AICChainNode>> aicChain){
        ArrayList<AICChainNode> aicChainEnds = new ArrayList<>();
        AICChainNode chainStart = aicChain.getFirst().getFirst();
        int chainStartPossible = aicChain.getFirst().getFirst().getStrongLinkPossible();

        for (int i = 3; i < aicChain.size(); i += 2) {
            for (AICChainNode possibleEndCell: aicChain.get(i)) {
                if(possibleEndCell.getStrongLinkPossible() == chainStartPossible ||
                        (chainStart.getNodeCell() != possibleEndCell.getNodeCell() && possibleEndCell.getNodeCell().getPossibilities().contains(chainStartPossible)) ||
                        (sudoku.doCellsSeeEachOther(chainStart.getNodeCell(), possibleEndCell.getNodeCell()) && chainStart.getNodeCell().getPossibilities().contains(possibleEndCell.getStrongLinkPossible())) ){
                    aicChainEnds.add(possibleEndCell);
                }
            }
        }

        return aicChainEnds;
    }

    private boolean isCellInAicChain(Cell cell, LinkedList<AICChainNode> aicChain){

        for (AICChainNode chainNode: aicChain) {
            if(cell == chainNode.getNodeCell()){
                return true;
            }
        }

        return false;
    }


    private LinkedList<AICChainNode> extractAicChain(AICChainNode chainEnd, LinkedList<LinkedList<AICChainNode>> aicChain){
        LinkedList<AICChainNode> aicChainNodes = new LinkedList<>();
        aicChainNodes.addLast(chainEnd);

        int depth = getAicChainEndNodeDepth(chainEnd, aicChain);

        return extractAicChainHelper(aicChainNodes, aicChain, --depth);
    }

    private LinkedList<AICChainNode> extractAicChainHelper(LinkedList<AICChainNode> aicChainNodes ,LinkedList<LinkedList<AICChainNode>> aicChain, int depth){

        if (depth < 0){
            return aicChainNodes;
        }else if(aicChainNodes.getFirst() == aicChain.getFirst().getFirst()){
            return aicChainNodes;
        }

        for (AICChainNode chainNode: aicChain.get(depth)) {
            AICChainNode workingNode = aicChainNodes.getFirst();
            if(chainNode.getNodeCell().getPossibilities().size() < 2 ||
                    aicChainNodes.contains(chainNode) ||
                    !chainNode.getNodeCell().getPossibilities().contains(chainNode.getStrongLinkPossible())){
                continue;
            }
            if(depth % 2 != 0){
                if((chainNode.getWeakLinkConnections().contains(workingNode) ||
                        chainNode.getStrongLinkConnections().contains(workingNode)) ){
                    aicChainNodes.addFirst(chainNode);
                    int newDepth = depth - 1;
                    LinkedList<AICChainNode> tempChain = extractAicChainHelper(aicChainNodes, aicChain, newDepth);
                    if(tempChain.getFirst() != aicChain.getFirst().getFirst()){
                        aicChainNodes.removeFirst();
                        continue;
                    }
                    aicChainNodes = tempChain;
                    break;
                }
            }else{
                if(chainNode.getStrongLinkConnections().contains(workingNode)){
                    aicChainNodes.addFirst(chainNode);
                    int newDepth = depth - 1;
                    LinkedList<AICChainNode> tempChain = extractAicChainHelper(aicChainNodes, aicChain, newDepth);
                    if(tempChain.getFirst() != aicChain.getFirst().getFirst()){
                        aicChainNodes.removeFirst();
                        continue;
                    }

                    aicChainNodes = tempChain;
                    break;
                }
            }
        }

        return aicChainNodes;
    }


    private int getAicChainEndNodeDepth(AICChainNode chainEnd, LinkedList<LinkedList<AICChainNode>> aicChain){
        int depth = 0;

        for (LinkedList<AICChainNode> chainLevel: aicChain) {
            for (AICChainNode node : chainLevel) {
                if(node == chainEnd){
                    return depth;
                }
            }
            depth++;
        }

        return depth;
    }

    private  LinkedList<LinkedList<AICChainNode>> aicChainBuilder(AICChainNode startNode){
        LinkedList<LinkedList<AICChainNode>> aicChain = new LinkedList<>();
        LinkedList<AICChainNode> firstChainLevel = new LinkedList<>();
        ArrayList<AICChainNode> nodesAddedToChain = new ArrayList<>();
        nodesAddedToChain.add(startNode);
        firstChainLevel.add(startNode);
        aicChain.add(firstChainLevel);

        boolean nodeAdded;
        boolean getStrongLink = true;

        do{
            nodeAdded = false;
            LinkedList<AICChainNode> nextChainLevel = new LinkedList<>();

            for (AICChainNode node: aicChain.getLast()){
                if(getStrongLink){
                    for (AICChainNode strongLink: node.getStrongLinkConnections()) {
                        if(!strongLink.getNodeCell().getPossibilities().contains(node.getStrongLinkPossible())){
                            continue;
                        }
                        if(!nodesAddedToChain.contains(strongLink)){
                            nextChainLevel.add(strongLink);
                            nodesAddedToChain.add(strongLink);
                            nodeAdded = true;
                        }
                    }
                }else{
                    for (AICChainNode strongLink: node.getStrongLinkConnections()) {
                        if(!strongLink.getNodeCell().getPossibilities().contains(node.getStrongLinkPossible())){
                            continue;
                        }
                        if(!nodesAddedToChain.contains(strongLink)){
                            nextChainLevel.add(strongLink);
                            nodesAddedToChain.add(strongLink);
                            nodeAdded = true;
                        }
                    }
                    for (AICChainNode weakLink: node.getWeakLinkConnections()) {
                        if(!weakLink.getNodeCell().getPossibilities().contains(node.getStrongLinkPossible())){
                            continue;
                        }
                        if(!nodesAddedToChain.contains(weakLink)){
                            nextChainLevel.add(weakLink);
                            nodesAddedToChain.add(startNode);
                            nodeAdded = true;
                        }
                    }
                }
            }

            if(nextChainLevel.size() > 0){
                aicChain.add(nextChainLevel);
            }
            getStrongLink = !getStrongLink;
        }while(nodeAdded);

        if(!getStrongLink){
            aicChain.removeLast();
        }
        return aicChain;
    }

    private void createAICNodes(){
        ArrayList<ArrayList<LinkedList<Cell>>> strongLinks = new ArrayList<>();
        //first get all the cells with strong links for each possible that don't only have two possibilities
        for (int i = 1; i <= 9; i++) {
            ArrayList<AICChainNode> nodePossibles = new ArrayList<>();
            strongLinks.add(sudoku.getCellsWithStrongLinks(i));
            for (LinkedList<Cell> strongLinkCells: strongLinks.get( i - 1)) {
                if(strongLinkCells.getFirst().getPossibilities().size() != 2){
                    nodePossibles.add(new AICChainNode(strongLinkCells.getFirst(), i));
                }
            }
            aicPossibleChainLinks.add(nodePossibles);
        }

        //get all the 2 possible cells, they can represent 2 different links in the chain because they have a strong link between themselves
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getPossibilities().size() == 2){
                    AICChainNode node1 = new AICChainNode(sudokuBoard[row][column], sudokuBoard[row][column].getPossibilities().get(0));
                    AICChainNode node2 = new AICChainNode(sudokuBoard[row][column], sudokuBoard[row][column].getPossibilities().get(1));

                    node1.getStrongLinkConnections().add(node2);
                    node2.getStrongLinkConnections().add(node1);

                    aicPossibleChainLinks.get(node1.getStrongLinkPossible() - 1).add(node1);
                    aicPossibleChainLinks.get(node2.getStrongLinkPossible() - 1).add(node2);

                }
            }
        }
    }

    private void findLinksForAICNodes(){

        for (int i = 0; i < aicPossibleChainLinks.size(); i++) {
            for (int j = 0; j < aicPossibleChainLinks.get(i).size() ; j++) {
                AICChainNode currentNode = aicPossibleChainLinks.get(i).get(j);
                for (AICChainNode node : aicPossibleChainLinks.get(i)) {
                    if(currentNode != node && sudoku.doCellsHaveStrongLink(currentNode.getNodeCell(), node.getNodeCell(), i + 1)){
                        currentNode.getStrongLinkConnections().add(node);
                    }else if(currentNode != node && sudoku.doCellsSeeEachOther(currentNode.getNodeCell(), node.getNodeCell())){
                        currentNode.getWeakLinkConnections().add(node);
                    }
                }

                for (Integer possible: currentNode.getNodeCell().getPossibilities()) {
                    if(possible == currentNode.getStrongLinkPossible()){
                        continue;
                    }
                    for (AICChainNode node: aicPossibleChainLinks.get(possible - 1)) {
                        if(currentNode.getNodeCell() == node.getNodeCell()){
                            currentNode.getWeakLinkConnections().add(node);
                        }
                    }
                }
            }
        }
    }

}
