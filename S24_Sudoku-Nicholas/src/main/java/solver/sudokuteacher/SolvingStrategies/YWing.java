package solver.sudokuteacher.SolvingStrategies;

import solver.sudokuteacher.SudokuCompenents.Cell;
import solver.sudokuteacher.SudokuCompenents.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class YWing extends SolvingStrategy{

    public YWing(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    public boolean executeStrategy(){

        boolean flag = false;
        ArrayList<Cell> cellsWithTwoPossibles = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuBoard[row][column].getPossibilities().size() == 2){
                    cellsWithTwoPossibles.add(sudokuBoard[row][column]);
                }
            }
        }

        for (Cell possibleHinge: cellsWithTwoPossibles) {
            if(possibleHinge.getPossibilities().size() < 2){
                continue;
            }
            int possible1 = possibleHinge.getPossibilities().get(0);
            int possible2 = possibleHinge.getPossibilities().get(1);
            int row = possibleHinge.getRow();
            int column = possibleHinge.getColumn();
            ArrayList<Cell> possibleWings = new ArrayList<>();
            for (Cell possibleWing : cellsWithTwoPossibles) {
                if(row == possibleWing.getRow() || column == possibleWing.getColumn() || sudoku.areCellsInSameBox(possibleHinge, possibleWing)) {
                    if (possibleWing != possibleHinge && possibleWing.getPossibilities().contains(possible1) ^ possibleWing.getPossibilities().contains(possible2)) {
                        possibleWings.add(possibleWing);
                    }
                }
            }
            if(possibleWings.size() < 2){
                continue;
            }
            LinkedList<Cell> possibleYWing = new LinkedList<>();
            possibleYWing.add(possibleHinge);
            for (Cell wingCell1: possibleWings) {
                if(wingCell1.getPossibilities().size() < 2){
                    continue;
                }
                possibleYWing.add(wingCell1);
                for (Cell wingCell2: possibleWings) {
                    if(wingCell2 != wingCell1 && wingCell2.getPossibilities().size() > 1){
                        possibleYWing.add(wingCell2);
                        if(yWingFinderHelper(possibleYWing)){
                            if(removePossibleVisibleToBothYWingEdges(possibleYWing)){
                                flag = true;
                            }
                        }
                        possibleYWing.removeLast();
                    }
                }
                possibleYWing.removeLast();
            }
        }

        return flag;
    }

    @Override
    public boolean findValidExecutions() {
        return false;
    }

    private boolean yWingFinderHelper(LinkedList<Cell> possibleYWing){
        int hingeCellPossible1 = possibleYWing.getFirst().getPossibilities().get(0);
        int hingeCellPossible2 = possibleYWing.getFirst().getPossibilities().get(1);
        int wing1CellPossible1 = possibleYWing.get(1).getPossibilities().get(0);
        int wing1CellPossible2 = possibleYWing.get(1).getPossibilities().get(1);
        int wing2CellPossible1 = possibleYWing.getLast().getPossibilities().get(0);
        int wing2CellPossible2 = possibleYWing.getLast().getPossibilities().get(1);

        boolean b = wing1CellPossible1 == wing2CellPossible1 ^ wing1CellPossible1 == wing2CellPossible2 ^ wing1CellPossible2 == wing2CellPossible1 ^ wing1CellPossible2 == wing2CellPossible2;
        if (hingeCellPossible1 == wing1CellPossible1 || hingeCellPossible1 == wing1CellPossible2) {
            if (hingeCellPossible2 == wing2CellPossible1 || hingeCellPossible2 == wing2CellPossible2) {
                return b;
            }
        } else if (hingeCellPossible2 == wing1CellPossible1 || hingeCellPossible2 == wing1CellPossible2) {
            if (hingeCellPossible1 == wing2CellPossible1 || hingeCellPossible1 == wing2CellPossible2) {
                return b;
            }
        }

        return false;
    }

    private boolean removePossibleVisibleToBothYWingEdges(LinkedList<Cell> yWing){

        boolean flag = false;

        int possibleToRemove;

        Cell yWingEdge1 = yWing.get(1);
        Cell yWingEdge2 = yWing.getLast();

        if (yWingEdge1.getPossibilities().contains(yWingEdge2.getPossibilities().get(0))) {
            possibleToRemove = yWingEdge2.getPossibilities().get(0);
        }else{
            possibleToRemove = yWingEdge2.getPossibilities().get(1);
        }

        ArrayList<Cell> list1 = sudoku.cellsSeenHelper(yWingEdge1);
        ArrayList<Cell> list2 = sudoku.cellsSeenHelper(yWingEdge2);
        ArrayList<Cell> cellsSeenByBothEdges = new ArrayList<>();

        for (Cell cell1: list1) {
            for (Cell cell2: list2) {
                if(cell1.getRow() == cell2.getRow() && cell1.getColumn() == cell2.getColumn()){
                    if(!cellsSeenByBothEdges.contains(cell2) && !yWing.contains(cell2)){
                        cellsSeenByBothEdges.add(cell2);
                    }
                }
            }
        }

        for (Cell cell: cellsSeenByBothEdges) {
            if(cell.getPossibilities().removeIf(p -> p == possibleToRemove)){
                flag = true;
            }
        }

        return flag;
    }
}
