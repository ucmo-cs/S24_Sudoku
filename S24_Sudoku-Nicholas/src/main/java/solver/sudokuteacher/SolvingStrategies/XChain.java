package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class XChain extends SolvingStrategy{

    public XChain(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<LinkedList<Cell>> cellsWithStrongLink = sudoku.getCellsWithStrongLinks(possible);
            ArrayList<LinkedList<ArrayList<Cell>>> xChains = new ArrayList<>();
            if(cellsWithStrongLink.size() > 3){
                for (LinkedList<Cell> cells: cellsWithStrongLink) {
                    LinkedList<ArrayList<Cell>> xChain = xChainBuilder(cells.getFirst(), cellsWithStrongLink);
                    if(xChain.size() > 3){
                        xChains.add(xChain);
                    }
                }
                if(findXChainEliminations(xChains, possible)){
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
    private LinkedList<ArrayList<Cell>>  xChainBuilder (Cell startCell, ArrayList<LinkedList<Cell>>  cellsWithStrongLink){
        LinkedList<ArrayList<Cell>> xChain = new LinkedList<>();
        ArrayList<Cell> chainStart = new ArrayList<>();
        ArrayList<Cell> cellsAddedToChain = new ArrayList<>();

        cellsAddedToChain.add(startCell);
        chainStart.add(startCell);
        xChain.add(chainStart);

        boolean cellAdded;
        boolean getStrongLink = true;
        do{
            cellAdded = false;
            ArrayList<Cell> nextChainLevel = new ArrayList<>();

            for (Cell cell: xChain.getLast()) {
                if(getStrongLink){
                    for (LinkedList<Cell> strongLinkCells: cellsWithStrongLink) {
                        if(strongLinkCells.getFirst() == cell){
                            for (Cell strongLink: strongLinkCells) {
                                if(!cellsAddedToChain.contains(strongLink)){
                                    cellsAddedToChain.add(strongLink);
                                    nextChainLevel.add(strongLink);
                                    cellAdded = true;
                                }
                            }
                        }
                    }
                }else{
                    for (LinkedList<Cell> strongLinkCells: cellsWithStrongLink) {
                        if(!cellsAddedToChain.contains(strongLinkCells.getFirst()) &&
                                (strongLinkCells.getFirst().getRow() == cell.getRow() || strongLinkCells.getFirst().getColumn() == cell.getColumn() || sudoku.areCellsInSameBox(strongLinkCells.getFirst(), cell))){
                            cellsAddedToChain.add(strongLinkCells.getFirst());
                            nextChainLevel.add(strongLinkCells.getFirst());
                            cellAdded = true;
                        }
                    }
                }
            }
            if(nextChainLevel.size() > 0){
                xChain.add(nextChainLevel);
            }

            getStrongLink = !getStrongLink;
        }while(cellAdded);

        //if the last link was weak, remove it
        if(!getStrongLink){
            xChain.removeLast();
        }

        return xChain;
    }

    private boolean findXChainEliminations(ArrayList<LinkedList<ArrayList<Cell>>> xChains, int possible){
        boolean flag = false;


        for (LinkedList<ArrayList<Cell>> xChain: xChains) {

            ArrayList<Cell> cellsSeenByStart = sudoku.cellsSeenHelper(xChain.getFirst().get(0));
            for (Cell endCell: xChain.getLast()) {

                //This bit of logic is for a xCycle, will help a bit with GUI sudokuTeacher but doesn't solve any more puzzles but does decrease solving time
          /*      if(xChain.getFirst().get(0).getRow() == endCell.getRow() || xChain.getFirst().get(0).getColumn() == endCell.getColumn() || areCellsInSameBox(xChain.getFirst().get(0), endCell)){
                    ArrayList<LinkedList<Cell>> allChains = extractXChain(xChain, possible);
                    for (LinkedList<Cell> chain: allChains) {
                        if (chain.getLast() == endCell){
                            if(xCycleStrategy(chain, possible)){
                                flag = true;
                            }
                        }
                    }
                    if(flag){
                        continue;
                    }
                }*/
                ArrayList<Cell> cellsSeenByEnd = sudoku.cellsSeenHelper(endCell);
                ArrayList<Cell> cellsSeenByBoth = new ArrayList<>();

                for (Cell cell: cellsSeenByStart) {
                    if(cell.getPossibilities().contains(possible) && cellsSeenByEnd.contains(cell) && !isCellInXChain(xChain, cell)){
                        cellsSeenByBoth.add(cell);
                    }
                }

                for (Cell cell: cellsSeenByBoth) {
                    cell.getPossibilities().remove(Integer.valueOf(possible));
                    flag = true;
                }
            }

        }
        return flag;
    }

    private boolean isCellInXChain(LinkedList<ArrayList<Cell>> xChain, Cell cell){

        for (ArrayList<Cell> chainLevel: xChain) {
            for (Cell cellInChain: chainLevel) {
                if (cell == cellInChain){
                    return true;
                }
            }
        }

        return false;
    }

    //Method used for X-cycle logic
    private ArrayList<LinkedList<Cell>> extractXChain(LinkedList<ArrayList<Cell>> xChain, int possible){
        ArrayList<LinkedList<Cell>> xChains = new ArrayList<>();
        int depth = 0;
        for (ArrayList<Cell> chainLevel: xChain) {
            xChains = extractXChainHelper(xChains, chainLevel, depth, possible);
            depth++;

        }
        return xChains;
    }

    //TODO: refactor to use the end of the chain and work backwards to extract the x-chain
    private ArrayList<LinkedList<Cell>> extractXChainHelper(ArrayList<LinkedList<Cell>> xChains, ArrayList<Cell> chainLevel, int depth, int possible){
        ArrayList<LinkedList<Cell>> newXChains = new ArrayList<>();

        if(xChains.size() == 0 && chainLevel.size() == 1){
            LinkedList<Cell> chainStart = new LinkedList<>();
            chainStart.add(chainLevel.get(0));
            newXChains.add(chainStart);
        }else{
            for (LinkedList<Cell> xChain: xChains) {
                for (Cell cell: chainLevel) {
                    if(!xChain.contains(cell)){
                        if(cell.getRow() == xChain.getLast().getRow() || cell.getColumn() == xChain.getLast().getColumn() || sudoku.areCellsInSameBox(cell, xChain.getLast())){
                            LinkedList<Cell> newChain = new LinkedList<>(xChain);
                            if(depth % 2 != 0){
                                if(sudoku.doCellsHaveStrongLink(xChain.getLast(), cell, possible)){
                                    newChain.add(cell);
                                    newXChains.add(newChain);
                                }
                            }else {
                                newChain.add(cell);
                                newXChains.add(newChain);
                            }
                        }
                    }
                }
            }
        }

        return newXChains;
    }

    private boolean xCycleStrategy(LinkedList<Cell> xChain , int possible){
        boolean flag = false;
        Boolean[][] chainBoard= new Boolean[9][9];
        ArrayList<Cell> onCells = new ArrayList<>();
        ArrayList<Cell> offCells = new ArrayList<>();

        boolean onCell = true;
        for (Cell cell: xChain) {
            chainBoard[cell.getRow()][cell.getColumn()] = onCell;
            if(onCell){
                onCells.add(cell);
            }else{
                offCells.add(cell);
            }

            onCell = !onCell;
        }

        //remove possible in cells that can see both on and off cells of the chain
        Boolean[][] cellsWithPossibleNotInChain = new Boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getSolution() == 0 && sudokuBoard[row][column].getPossibilities().contains(possible) && chainBoard[row][column] == null){
                    cellsWithPossibleNotInChain[row][column] = true;
                }
            }
        }
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(cellsWithPossibleNotInChain[row][column] == null){
                    continue;
                }
                if (cellsWithPossibleNotInChain[row][column]){
                    int onCellsCount = 0;
                    int offCellsCount = 0;
                    for (Cell cell: onCells) {
                        if(cell.getRow() == row || cell.getColumn() == column || sudoku.areCellsInSameBox(cell, sudokuBoard[row][column])){
                            onCellsCount++;
                        }
                    }
                    for (Cell cell: offCells) {
                        if(cell.getRow() == row || cell.getColumn() == column || sudoku.areCellsInSameBox(cell, sudokuBoard[row][column])){
                            offCellsCount++;
                        }
                    }

                    if(onCellsCount >= 1 && offCellsCount >= 1){
                        if(sudokuBoard[row][column].getPossibilities().removeIf(p -> p == possible)){
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
}
