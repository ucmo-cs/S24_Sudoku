package solver.sudokuteacher.SudokuCompenents;

import solver.sudokuteacher.SolvingStrategies.*;

import java.util.ArrayList;
import java.util.LinkedList;

/*
* TODO: -adjust methods that require row/column checks to combine the row and column methods
*       -comment code
*/

public class Sudoku {

    private final Cell[][] sudoku = new Cell[9][9];
    private boolean invalidCell = false;
    NakedSingle ns;
    HiddenSingle hs;
    NakedCandidate nc;
    HiddenCandidate hc;
    PointingPairsAndTriples pp;
    BoxLineReduction blr;
    XWing xWing;
    SimpleColoring sc;
    YWing yWing;
    EmptyRectangle er;
    Swordfish sf;
    XyzWing xyzWing;
    Bug bug;
    XChain xChain;
    XYChain xyChain;
    AlternatingInferenceChain aic;
    RecursiveGuess guess;

    public Sudoku(int[][] sudoku) {

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                this.sudoku[row][column] = new Cell(sudoku[row][column], row, column);
            }
        }
        //updated possibilities for sudoku based on hints in starting sudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(this.sudoku[i][j].getSolution() != 0){
                    removeSolutionFromAffectedPossibilities(i,j,this.sudoku[i][j].getSolution());
                }
            }
        }
        initializeStrategies();
    }

    public Sudoku(String sudoku){
        if(sudoku.length() != 81){
            //TODO: display error message
            return;
        }else{
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.charAt(index) == '.') {
                        this.sudoku[i][j] = new Cell(0, i, j);
                    } else {
                        this.sudoku[i][j] = new Cell(sudoku.charAt(index) - '0', i, j);
                    }
                    index++;
                }
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if(this.sudoku[i][j].getSolution() != 0){
                        removeSolutionFromAffectedPossibilities(i,j,this.sudoku[i][j].getSolution());
                    }
                }
            }
            initializeStrategies();
        }
    }

    public Cell[][] getSudoku(){
        return this.sudoku;
    }

    public boolean checkSolution(){

        for (int i = 0; i < 9; i++) {
            int rowSum = 0;
            int columnSum = 0;
            for (int j = 0; j < 9; j++) {
                rowSum += sudoku[i][j].getSolution();
                columnSum += sudoku[j][i].getSolution();
                if(sudoku[i][j].getSolution() == 0){
                    return false;
                }
            }
            if (rowSum != 45 && columnSum != 45){
                return false;
            }

        }

        for (int boxRowTop = 0; boxRowTop < 3; boxRowTop++) {
            for (int boxColumnTop = 0; boxColumnTop < 3; boxColumnTop++) {
                if(!checkBoxSolution(boxRowTop * 3, boxColumnTop * 3)){
                    return false;
                }
            }
        }
        return true;
    }

    public Cell getCell(int row, int column){
        return sudoku[row][column];
    }

    public boolean isUnsolved(){

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (getCell(i,j).getSolution() == 0){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkBoxSolution(int x, int y){

        int solution = 0;
        for (int i = x; i < x+ 3 ; i++) {
            for (int j = x; j < x + 3; j++) {
                if (sudoku[i][j].getSolution() == 0){
                    return false;
                }else{
                    solution += sudoku[i][j].getSolution();
                }
            }
        }
        return solution == 45;
    }

    public int[] getBox(int row, int column){
        if (row < 3) {
            row = 0;
        } else if (row < 6) {
            row = 3;
        } else {
            row = 6;
        }

        if (column < 3) {
            column = 0;
        } else if (column < 6) {
            column = 3;
        } else {
            column = 6;
        }

        return new int[]{row, column};
    }

    private void initializeStrategies(){
         ns = new NakedSingle(this);
         hs = new HiddenSingle(this);
         nc = new NakedCandidate(this);
         hc = new HiddenCandidate(this);
         pp = new PointingPairsAndTriples(this);
         blr = new BoxLineReduction(this);
         xWing = new XWing(this);
         sc = new SimpleColoring(this);
         yWing = new YWing(this);
         er = new EmptyRectangle(this);
         sf = new Swordfish(this);
         xyzWing = new XyzWing(this);
         bug = new Bug(this);
         xChain = new XChain(this);
         xyChain = new XYChain(this);
         aic = new AlternatingInferenceChain(this);
         guess = new RecursiveGuess(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean solve() {
        boolean guess = false;
        do {
            if (ns.executeStrategy()){
            } else if (hs.executeStrategy()){
            } else if (nc.executeStrategy( 2)) {
            } else if (nc.executeStrategy( 3)) {
            } else if (nc.executeStrategy( 4)) {
            } else if (hc.executeStrategy(2)) {
            } else if (hc.executeStrategy( 3)) {
            } else if (hc.executeStrategy( 4)) {
            } else if (pp.executeStrategy()) {
            } else if (blr.executeStrategy()) {
            } else if (xWing.executeStrategy()) {
            } else if (sc.executeStrategy()){
            } else if (yWing.executeStrategy()) {
            } else if (er.executeStrategy()){
            } else if (sf.executeStrategy()){
            } else if (xyzWing.executeStrategy()){
            } else if (bug.executeStrategy()){
            } else if (xChain.executeStrategy()){
            } else if (xyChain.executeStrategy()){
            } else if (aic.executeStrategy()){
            } else {
                if(guess) {
                    if(checkSolution()){
                        return true;
                    }else if(invalidCell){
                        return false;
                    }else{
                        return this.guess.executeStrategy();
                    }

                }else{
                    return false;
                }
            }

        }  while (isUnsolved()) ;

        return checkSolution();
    }

    public void updateCellSolution(Cell cell, int solution){
        cell.setSolution(solution);
        cell.getPossibilities().clear();
        removeSolutionFromAffectedPossibilities(cell.getRow(),cell.getColumn(), solution);

    }

    private void removeSolutionFromAffectedPossibilities(int row, int column, int value){
        int[] boxTop = getBox(row,column);
        //remove solution from its box possibilities
        for (int i = boxTop[0]; i < 3 + boxTop[0]; i++) {
            for (int j = boxTop[1]; j < 3 + boxTop[1]; j++) {
                if (sudoku[i][j].getSolution() == 0) {
                    sudoku[i][j].getPossibilities().remove(Integer.valueOf(value));
                    if(sudoku[i][j].getPossibilities().size() == 0){
                        invalidCell = true;
                    }
                }
            }
        }

        //remove solution from its row & column possibilities
        for (int i = 0; i < 9; i++) {
            if(sudoku[row][i].getSolution() == 0){
                sudoku[row][i].getPossibilities().remove(Integer.valueOf(value));
                if(sudoku[row][i].getPossibilities().size() == 0){
                    invalidCell = true;
                }
            }
            if(sudoku[i][column].getSolution() == 0){
                sudoku[i][column].getPossibilities().remove(Integer.valueOf(value));
                if(sudoku[i][column].getPossibilities().size() == 0){
                    invalidCell = true;
                }
            }
        }
    }

    public boolean doCellsSeeEachOther(Cell cell1, Cell cell2){

        return cell1.getRow() == cell2.getRow() || cell1.getColumn() == cell2.getColumn() || areCellsInSameBox(cell1, cell2);
    }

    public ArrayList<LinkedList<Cell>> getCellsWithStrongLinks(int possible){
        ArrayList<LinkedList<Cell>> cellsWithStrongLinks = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getPossibilities().contains(possible)){
                    LinkedList<Cell> strongLinks = strongLinksWithCell(sudoku[row][column], possible);
                    if(strongLinks.size() > 0){
                        strongLinks.addFirst(sudoku[row][column]);
                        cellsWithStrongLinks.add(strongLinks);
                    }
                }
            }
        }

        return cellsWithStrongLinks;
    }

    public LinkedList<Cell> strongLinksWithCell(Cell cell, int possible){
        LinkedList<Cell> links = new LinkedList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        int[] boxTop = getBox(row,column);

        Cell boxStrongLink = null;
        Cell rowStrongLink = null;
        Cell columnStrongLink = null;

        int boxCount = 0;
        int rowCount = 0;
        int columnCount = 0;

        for (int boxRow = boxTop[0]; boxRow < boxTop[0] + 3; boxRow++) {
            for (int boxColumn = boxTop[1]; boxColumn < boxTop[1] + 3; boxColumn++) {
                if(sudoku[boxRow][boxColumn].getSolution() == 0 && sudoku[boxRow][boxColumn] != cell && sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                    boxCount++;
                    if(boxCount == 1){
                        boxStrongLink = sudoku[boxRow][boxColumn];
                    }else{
                        break;
                    }
                }
            }
            if(boxCount > 1){
                break;
            }
        }

        if(boxCount == 1){
            links.add(boxStrongLink);

        }

        for (int i = 0; i < 9; i++) {
            if(sudoku[row][i].getSolution() == 0 && sudoku[row][i] != cell && sudoku[row][i].getPossibilities().contains(possible)){
                rowCount++;
                if(rowCount == 1){
                     rowStrongLink = sudoku[row][i];
                }
            }
            if(sudoku[i][column].getSolution() == 0 && sudoku[i][column] != cell && sudoku[i][column].getPossibilities().contains(possible)){
                columnCount++;
                if(columnCount == 1){
                    columnStrongLink = sudoku[i][column];
                }
            }
        }

        if(rowCount == 1){
            if(!links.contains(rowStrongLink)){
                links.add(rowStrongLink);
            }
        }
        if(columnCount == 1){
            if(!links.contains(columnStrongLink)){
                links.add(columnStrongLink);
            }
        }
        return links;
    }

    public boolean doCellsHaveStrongLink(Cell cell1, Cell cell2, int possible){
        boolean flag = false;
        int possibleCount = 0;


        if(cell1.getRow() == cell2.getRow()){
            int row = cell1.getRow();
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getPossibilities().contains(possible)){
                    possibleCount++;
                }
            }
        }else if( cell1.getColumn() == cell2.getColumn()){
            int column = cell1.getColumn();
            for (int row = 0; row < 9; row++) {
                if(sudoku[row][column].getPossibilities().contains(possible)){
                    possibleCount++;
                }
            }
        } else if(areCellsInSameBox(cell1, cell2)){
            int[] boxTops = getBox(cell1.getRow(), cell1.getColumn());
            for (int boxRow = boxTops[0]; boxRow < boxTops[0] + 3; boxRow++) {
                for (int boxColumn = boxTops[1]; boxColumn < boxTops[1] + 3; boxColumn++) {
                    if(sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                        possibleCount++;
                    }
                }
            }
        }

        if(possibleCount == 2){
            flag = true;
        }

        return flag;
    }

    public ArrayList<Cell> cellsSeenHelper(Cell cell){

        ArrayList<Cell> cellsSeen = new ArrayList<>();

        int row = cell.getRow();
        int column = cell.getColumn();
        int[] box = getBox(row, column);

        //box
        for (int boxRow = box[0]; boxRow < 3 + box[0]; boxRow++) {
            for (int boxColumn = box[1]; boxColumn < 3 + box[1]; boxColumn++) {
                if(sudoku[boxRow][boxColumn].getSolution() == 0 && boxRow != row && boxColumn != column){
                    cellsSeen.add(sudoku[boxRow][boxColumn]);
                }
            }
        }
        //row
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(sudoku[row][i]) && sudoku[row][i].getSolution() == 0 && i != column){
                cellsSeen.add(sudoku[row][i]);
            }
        }

        //column
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(sudoku[i][column]) && sudoku[i][column].getSolution() == 0 && i != row){
                cellsSeen.add(sudoku[i][column]);
            }
        }

        return cellsSeen;
    }


    public boolean areCellsInSameBox(Cell cell1, Cell cell2){
       int[] box1 = getBox(cell1.getRow(), cell1.getColumn());
       int[] box2 = getBox(cell2.getRow(), cell2.getColumn());

       return (box1[0] == box2[0] && box1[1] == box2[1]);
    }

    public ArrayList<Cell> getBoxCellsWithPossibility(int[] boxTop, int possible){
        ArrayList<Cell> cellsWithPossibleInBox = new ArrayList<>();
        int boxRowTop = boxTop[0];
        int boxColumnTop = boxTop[1];

        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                if(sudoku[boxRow][boxColumn].getSolution() == 0 && sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                    cellsWithPossibleInBox.add(sudoku[boxRow][boxColumn]);
                }
            }
        }
        return  cellsWithPossibleInBox;
    }

    public void display() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(sudoku[row][column].getSolution() + " ");
            }
            System.out.print("\n");
        }
    }

    @Override
    public String toString(){

        String sudokuString = "";

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                //noinspection StringConcatenationInLoop
                sudokuString += Integer.toString(sudoku[row][column].getSolution());
            }
        }

        return  sudokuString;
    }

    public boolean containsInvalidCell() {
        return invalidCell;
    }

}
