package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimpleColoring extends SolvingStrategy{


    public SimpleColoring(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<LinkedList<Cell>> cellsWithStrongLink = sudoku.getCellsWithStrongLinks(possible);
            if(cellsWithStrongLink.size() < 4){
                continue;
            }
            if (simpleColoringHelper(cellsWithStrongLink, possible)){
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private boolean simpleColoringHelper(ArrayList<LinkedList<Cell>> cellsWithStrongLink, int possible){
        boolean flag = false;
        ArrayList<Cell> cellsWithOneLink = new ArrayList<>();
        LinkedList<ArrayList<Cell>> coloringChain;

        for (LinkedList<Cell> cellLinks: cellsWithStrongLink) {
            if(cellLinks.size() == 2){
                cellsWithOneLink.add(cellLinks.getFirst());
            }
        }

        do {
            if (cellsWithOneLink.size() == 0) {
                return false;
            }else {
                coloringChain = simpleColoringChainBuilder(cellsWithOneLink.get(0), cellsWithStrongLink);
                if (coloringChain.size() < 4) {
                    for (ArrayList<Cell> cells: coloringChain) {
                        for (Cell cell: cells) {
                            cellsWithOneLink.removeIf(p -> p == cell);
                        }
                    }
                }else{
                    for (ArrayList<Cell> cells: coloringChain) {
                        for (Cell cell: cells) {
                            cellsWithOneLink.removeIf(p -> p == cell);
                        }
                    }
                    if(simpleColoringPossibleRemover(coloringChain, possible)){
                        flag = true;
                    }
                }
            }


        }while(cellsWithOneLink.size() > 0);

        return flag;
    }



    private LinkedList<ArrayList<Cell>> simpleColoringChainBuilder(Cell startCell, ArrayList<LinkedList<Cell>> cellsWithStrongLink){
        LinkedList<ArrayList<Cell>> coloringChain = new LinkedList<>();
        ArrayList<Cell> chainStart = new ArrayList<>();
        ArrayList<Cell> cellsAddedToChain = new ArrayList<>();

        cellsAddedToChain.add(startCell);
        chainStart.add(startCell);
        coloringChain.add(chainStart);

        boolean cellAdded;
        do{
            cellAdded = false;
            ArrayList<Cell> nextChainLevel = new ArrayList<>();
            for (Cell cell: coloringChain.getLast()) {
                boolean cellAddedFromThisCell = false;
                for (LinkedList<Cell> strongLink: cellsWithStrongLink) {
                    if(strongLink.getFirst() == cell){
                        for (int i = 1; i < strongLink.size(); i++) {
                            if(!cellsAddedToChain.contains(strongLink.get(i))){
                                cellsAddedToChain.add(strongLink.get(i));
                                nextChainLevel.add(strongLink.get(i));
                                cellAddedFromThisCell = true;
                                cellAdded = true;
                            }
                        }
                    }
                    if(cellAddedFromThisCell){
                        break;
                    }
                }
            }
            if(nextChainLevel.size() > 0){
                coloringChain.add(nextChainLevel);
            }

        }while(cellAdded);

        return coloringChain;
    }

    private boolean simpleColoringPossibleRemover(LinkedList<ArrayList<Cell>> colorChain, int possible){
        boolean flag = false;
        Boolean[][] colorChainBoard = new Boolean[9][9];
        ArrayList<Cell> onCells = new ArrayList<>();
        ArrayList<Cell> offCells = new ArrayList<>();

        for (int i = 0; i < colorChain.size(); i++) {
            if(i % 2 == 0){
                for (Cell cell: colorChain.get(i)) {
                    colorChainBoard[cell.getRow()][cell.getColumn()] = true;
                    onCells.add(cell);
                }
            }else{
                for (Cell cell: colorChain.get(i)) {
                    colorChainBoard[cell.getRow()][cell.getColumn()] = false;
                    offCells.add(cell);
                }
            }
        }

        for (Cell onCell1: onCells) {
            for (Cell onCell2: onCells) {
                if(onCell2 != onCell1 && (onCell1.getRow() == onCell2.getRow() || onCell1.getColumn() == onCell2.getColumn() || sudoku.areCellsInSameBox(onCell1, onCell2))){
                    for (Cell onCell: onCells) {
                        onCell.getPossibilities().remove(Integer.valueOf(possible));
                    }
                    return true;
                }
            }
        }

        for (Cell offCell1: offCells) {
            for (Cell offCell2: offCells) {
                if(offCell2 != offCell1 && (offCell1.getRow() == offCell2.getRow() || offCell1.getColumn() == offCell2.getColumn() || sudoku.areCellsInSameBox(offCell1, offCell2))){
                    for (Cell offCell: offCells) {
                        offCell.getPossibilities().remove(Integer.valueOf(possible));
                    }
                    return true;
                }
            }
        }

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(colorChainBoard[row][column] == null && sudokuBoard[row][column].getPossibilities().contains(possible)){
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
                    if(onCellsCount >= 1 && offCellsCount >= 1) {
                        sudokuBoard[row][column].getPossibilities().remove(Integer.valueOf(possible));
                        flag = true;
                    }
                }
            }
        }


        return flag;
    }
}
