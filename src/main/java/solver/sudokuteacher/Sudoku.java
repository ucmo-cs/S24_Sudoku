package solver.sudokuteacher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/*
* TODO: -examine sudoku solving methods to find which ones can be written more efficiently
*       -adjust methods that require row/column checks to combine the row and column methods
*       -comment code
*/

public class Sudoku {

    private Cell[][] sudoku = new Cell[9][9];
    private boolean invalidCell = false;

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
        return true;
    }


    public int getValue(int row, int column) {

        return sudoku[row][column].getSolution();
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

    public boolean isBoxUnsolved(int x, int y){

        if (x < 3) {
            x = 0;
        } else if (x < 6) {
            x = 3;
        } else {
            x = 6;
        }

        if (y < 3) {
            y = 0;
        } else if (y < 6) {
            y = 3;
        } else {
            y = 6;
        }

        for (int i = x; i < x + 3 ; i++) {
            for (int j = y; j < y + 3; j++) {
                if (sudoku[i][j].getSolution() == 0){
                    return true;
                }

            }
        }
        return false;
    }

    private int[] getBox(int row, int column){
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean solve(){
        boolean guess = false;
        do {
            if (nakedSingle()){
            } else if (hiddenSingle()){
            } else if (nakedCandidate(2)) {
            } else if (nakedCandidate(3)) {
            } else if (nakedCandidate(4)) {
            } else if (hiddenCandidate(2)) {
            } else if (hiddenCandidate(3)) {
            } else if (hiddenCandidate(4)) {
            } else if (pointingPairsAndTriples()) {
            } else if (boxLineReduction()) {
            } else if (xWingStrategy()) {
            } else if (simpleColoringStrategy()){
            } else if (yWingStrategy()) {
            } else if (emptyRectangleStrategy()){
            } else if (swordfishStrategy()){
            } else if (xyzWingStrategy()){
            } else if (bugStrategy()){
            } else if (xChainStrategy()){
            } else if (xyChainStrategy()){
            }/* else if (aicChainStrategy()){
            }*/ else {
                if(guess) {
                    return guess();
                }else{
                    return false;
                }
            }

        }  while (isUnsolved()) ;

        return checkSolution();
    }

    private void updateCellSolution(int x, int y, int solution){
        sudoku[x][y].setSolution(solution);
        sudoku[x][y].getPossibilities().clear();
        removeSolutionFromAffectedPossibilities(x,y, solution);

    }
    private void updateCellSolution(Cell cell, int solution){
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

    public boolean nakedSingle(){
        boolean flag = false;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudoku[row][column].getPossibilities().size() == 1) {
                    updateCellSolution( row, column, sudoku[row][column].getPossibilities().get(0));
                    flag = true;
                }
            }
        }
        return flag;
    }
    //If there is a possibility where the solution is unique to box/row/column
    public boolean hiddenSingle(){
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if(hiddenSingleRowHelper(i)){
                flag = true;
            }
            if(hiddenSingleColumnHelper(i)){
                flag = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(hiddenSingleBoxHelper(i * 3, j * 3)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

   private boolean hiddenSingleRowHelper(int row){
       boolean flag = false;
       Cell cell = new Cell();

       for (int possible = 1; possible <= 9; possible++) {
           int count = 0;
           for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getPossibilities().contains(possible)){
                   count++;
                   cell = sudoku[row][column];
               }
               if (count > 1){
                   break;
               }
           }
           //hidden single
           if(count == 1){
               updateCellSolution(cell,possible);
               flag = true;
           }
       }
       return flag;
   }
    private boolean hiddenSingleColumnHelper(int column){
        boolean flag = false;

        Cell cell = new Cell();
        for (int possible = 1; possible <= 9; possible++) {
            int count = 0;
            for (int row = 0; row < 9; row++) {
             if(sudoku[row][column].getPossibilities().contains(possible)){
                    count++;
                    cell = sudoku[row][column];
                }
                if (count > 1){
                    break;
                }
            }
            //hidden single
            if(count == 1){
                updateCellSolution(cell,possible);
                flag = true;
            }
        }
        return flag;
    }

    private boolean hiddenSingleBoxHelper(int rowTop, int columnTop){
        boolean flag = false;

        Cell cell = new Cell();
        for (int possible = 1; possible <= 9; possible++) {
            int count = 0;
            for (int boxRow = rowTop; boxRow < rowTop + 3; boxRow++) {
                for (int boxColumn = columnTop; boxColumn < columnTop + 3; boxColumn++) {
                    if(sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                        count++;
                        cell = sudoku[boxRow][boxColumn];
                    }
                    if(count > 1){
                        break;
                    }
                }
            }
            if(count == 1){
                updateCellSolution(cell,possible);
                flag = true;
            }
        }

        return flag;
    }


        private boolean nakedCandidate(int candidateNumber){
        boolean flag = false;

        for (int i = 0; i < 9; i++) {
            if(nakedCandidateRowHelper(i, candidateNumber) ){
                flag = true;
            }
            if(nakedCandidateColumnHelper(i, candidateNumber)){
                flag = true;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(nakedCandidateBoxHelper(i * 3, j * 3, candidateNumber)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean nakedCandidateRowHelper(int row, int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int column = 0; column < 9; column++) {
            if (sudoku[row][column].getSolution() == 0 &&
                    sudoku[row][column].getPossibilities().size() <= candidateNumber &&
                    sudoku[row][column].getPossibilities().size() >= 2) {

                candidateCells.add(sudoku[row][column]);

            }
        }
        if(candidateCells.size() < candidateNumber){
            return flag;
        }
        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size() ; i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int column = 0; column < 9; column++) {
                    if (nakedCandidateCells.get(i).contains(sudoku[row][column])) {
                        continue;
                    }
                    for (Integer possible : nakedCandidatePossibles.get(i)) {
                        if (sudoku[row][column].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                            flag = true;
                        }

                    }
                }
            }
        }

        return flag;
    }

    private boolean nakedCandidateColumnHelper( int column,  int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int row = 0; row < 9; row++) {
            if (sudoku[row][column].getSolution() == 0 &&
                    sudoku[row][column].getPossibilities().size() <= candidateNumber &&
                    sudoku[row][column].getPossibilities().size() >= 2) {

                candidateCells.add(sudoku[row][column]);

            }
        }
        if(candidateCells.size() < candidateNumber){
            return flag;
        }

        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size(); i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int row = 0; row < 9; row++) {
                    if (nakedCandidateCells.get(i).contains(sudoku[row][column])) {
                        continue;
                    }
                    for (Integer possible : nakedCandidatePossibles.get(i)) {
                        if (sudoku[row][column].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                            flag = true;
                        }

                    }
                }
            }
        }

        return flag;
    }
    private boolean nakedCandidateBoxHelper(int rowTop, int columnTop,  int candidateNumber){
        boolean flag = false;
        ArrayList<Cell> candidateCells = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nakedCandidatePossibles;

        for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
            for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                if (sudoku[boxRow][boxColumn].getSolution() == 0 &&
                        sudoku[boxRow][boxColumn].getPossibilities().size() <= candidateNumber &&
                        sudoku[boxRow][boxColumn].getPossibilities().size() >= 2) {
                    candidateCells.add( sudoku[boxRow][boxColumn]);
                }
            }
        }
        if(candidateCells.size() < candidateNumber){
            return flag;
        }

        ArrayList<ArrayList<Cell>> nakedCandidateCells = new ArrayList<>();
        nakedCandidateCells.add(candidateCells);

        nakedCandidatePossibles = nakedCandidatePossiblesHelper(nakedCandidateCells, candidateNumber);

        for (int i = 0; i < nakedCandidatePossibles.size(); i++) {
            if (nakedCandidateCells.get(i).size() == candidateNumber && nakedCandidatePossibles.get(i).size() == candidateNumber) {
                for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
                    for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                        if (nakedCandidateCells.get(i).contains(sudoku[boxRow][boxColumn])) {
                            continue;
                        }
                        for (Integer possible : nakedCandidatePossibles.get(i)) {
                            if (sudoku[boxRow][boxColumn].getPossibilities().removeIf(p -> Objects.equals(p, possible))) {
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }
    private ArrayList<ArrayList<Integer>> nakedCandidatePossiblesHelper(ArrayList<ArrayList<Cell>> nakedCandidateCells, int candidateNumber){

        ArrayList<ArrayList<Integer>> nakedCandidatePossibles = new ArrayList<>();
        ArrayList<Integer> candidatePossibles = new ArrayList<>();
        ArrayList<Cell> candidateCells = nakedCandidateCells.get(0);
        ArrayList<Cell> cellsToRemove = new ArrayList<>();

        //first find all the cells that cannot be a naked candidate and remove them from the list
        for (int i = 0; i < candidateCells.size(); i++) {
            int count = 0;
            if(candidateCells.get(i).getPossibilities().size() == candidateNumber) {
                ArrayList<Integer> tempPossibles = candidateCells.get(i).getPossibilities();
                for (int j = 0; j < candidateCells.size(); j++) {
                    if( tempPossibles.containsAll(candidateCells.get(j).getPossibilities())){
                        count++;
                    }
                }
                if(count != candidateNumber){
                    cellsToRemove.add(candidateCells.get(i));
                }
            }
        }
        for (Cell cell: cellsToRemove) {
            candidateCells.remove(cell);
        }
        //then get all the possibles the remaining cells could be
        for (Cell cell : candidateCells) {
            for (Integer possible : cell.getPossibilities()) {
                if (!candidatePossibles.contains(possible)) {
                    candidatePossibles.add(possible);
                }
            }
        }

        //if there is a naked pair/triple/quad return it
        if(candidateCells.size() == candidateNumber && candidatePossibles.size() == candidateNumber){
            nakedCandidatePossibles.add(candidatePossibles);
            return nakedCandidatePossibles;
        }else{
            nakedCandidatePossibles.add(candidatePossibles);
        }

        boolean possibleRemoved;
        do {
            possibleRemoved = false;
            ArrayList<Integer> possibleToRemove = new ArrayList<>();
            for (Integer possible : candidatePossibles) {
                int count = 0;
                for (Cell candidate : candidateCells) {
                    if (candidate.getPossibilities().contains(possible)) {
                        count++;
                    }
                }
                if (count < 2) {
                    possibleToRemove.add(possible);
                    possibleRemoved = true;
                }
            }
            //noinspection Convert2MethodRef
            candidatePossibles.removeIf(p -> possibleToRemove.contains(p));

            for (int possible : possibleToRemove) {
                candidateCells.removeIf(cell -> cell.getPossibilities().contains(possible));
            }
        }while(possibleRemoved);

        //this means we have more than 1 naked pair
        if(candidateNumber == 2  && (nakedCandidateCells.get(0).size() == candidatePossibles.size()) && nakedCandidateCells.get(0).size() % 2 == 0){
            for (int i = 0; i < candidateCells.size(); i++) {
                ArrayList<Integer> temp = candidateCells.get(i).getPossibilities();
                for (int j = i + 1; j < candidateCells.size(); j++) {
                    if(candidateCells.get(j).getPossibilities().containsAll(temp)){
                        nakedCandidatePossibles.add(temp);
                        ArrayList<Cell> cells = new ArrayList<>();
                        cells.add(candidateCells.get(i));
                        cells.add(candidateCells.get(j));
                        nakedCandidateCells.add(cells);
                        break;
                    }
                }
            }
        }else{
            //this will find nakedCandidates only if 1 of the cells has all the possible candidates
            for (int i = 0; i < candidateCells.size(); i++) {
                ArrayList<Integer> tempPossibles = new ArrayList<>(candidateCells.get(i).getPossibilities());
                ArrayList<Cell> tempCells = new ArrayList<>();
                if(tempPossibles.size() == candidateNumber) {
                    for (int j = 0; j < candidateCells.size(); j++) {
                        if(tempPossibles.containsAll(candidateCells.get(j).getPossibilities())){
                            tempCells.add(candidateCells.get(j));
                        }
                    }

                    if(tempPossibles.size() == candidateNumber && tempCells.size() == candidateNumber){
                        nakedCandidatePossibles.add(tempPossibles);
                        nakedCandidateCells.add(tempCells);
                    }
                }else{
                   /* TODO: find a good way to find situation where the candidate numbers are spread throughout the cells w/out a single cell having them all
                            for example: candidates 1,2,3 spread through 3 cells like this (1,2)(2,3)(1,3)*/
                }
            }
        }

        return nakedCandidatePossibles;
    }


    //Not sure if hiddenCandidate(3 || 4) will find each possible condition for the strategy, works with no errors for now until I find a puzzle it doesn't work with
    private boolean hiddenCandidate(int candidateNumber){
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if(hiddenCandidateRow(i, candidateNumber)){
                flag = true;
            }
            if( hiddenCandidateColumn(i, candidateNumber)){
                flag = true;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(hiddenCandidateBox(i * 3, j * 3, candidateNumber)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean hiddenCandidateRow(int row, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int column = 0; column < 9; column++) {
                if ( sudoku[row][column].getPossibilities().contains(i)) {
                    count++;
                }
            }
            if(count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int column = 0; column < 9; column++) {
                if (sudoku[row][column].getPossibilities().contains(possible)) {
                    if(!hiddenCandidateCells.contains(sudoku[row][column])) {
                        hiddenCandidateCells.add(sudoku[row][column]);
                    }
                }
            }
        }
        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }

        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }
    private boolean hiddenCandidateColumn(int column, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int j = 0; j < 9; j++) {
                if (sudoku[j][column].getPossibilities().contains(i) ) {
                    count++;
                }
            }
            if(count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int row = 0; row < 9; row++) {
                if (sudoku[row][column].getPossibilities().contains(possible)) {
                    if(!hiddenCandidateCells.contains(sudoku[row][column])) {
                        hiddenCandidateCells.add(sudoku[row][column]);
                    }
                }
            }
        }
        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }
        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }
    private boolean hiddenCandidateBox(int row, int column, int candidateNumber){
        ArrayList<Integer> hiddenCandidatePossibles = new ArrayList<>();
        ArrayList<Cell> hiddenCandidateCells = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            int count = 0;
            for (int boxRow = row; boxRow < 3 + row; boxRow++) {
                for (int boxColumn = column; boxColumn < 3 + column; boxColumn++) {
                    if( sudoku[boxRow][boxColumn].getPossibilities().contains(i)){
                        count++;
                    }
                }
            }
            if (count >= 2 && count <= candidateNumber){
                hiddenCandidatePossibles.add(i);
            }
        }
        if(hiddenCandidatePossibles.size() < candidateNumber){
            return false;
        }

        for (Integer possible: hiddenCandidatePossibles) {
            for (int boxRow = row; boxRow < 3 + row; boxRow++) {
                for (int boxColumn = column; boxColumn < 3 + column; boxColumn++) {
                    if(sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                        if(!hiddenCandidateCells.contains(sudoku[boxRow][boxColumn])) {
                            hiddenCandidateCells.add(sudoku[boxRow][boxColumn]);
                        }
                    }
                }
            }
        }

        if(hiddenCandidateCells.size() < candidateNumber){
            return false;
        }


        return hiddenCandidateHelper(hiddenCandidateCells, hiddenCandidatePossibles, candidateNumber);
    }

    private boolean hiddenCandidateHelper(ArrayList<Cell> hiddenCandidateCells,  ArrayList<Integer> hiddenCandidatePossibles, int candidateNumber){
        boolean flag = false;

        ArrayList<Cell> cellToRemove = new ArrayList<>();

        for (Cell cell: hiddenCandidateCells) {
            int count = 0;
            for (Integer possible: hiddenCandidatePossibles) {
                if(cell.getPossibilities().contains(possible)){
                    count++;
                }
                if(count > 2){
                    break;
                }
            }
            if(count < 2){
                cellToRemove.add(cell);
            }
        }
        for (Cell cell: cellToRemove) {
            for (Integer possible: cell.getPossibilities()) {
                hiddenCandidatePossibles.remove(possible);
            }
        }

        for (Cell cell : cellToRemove) {
            hiddenCandidateCells.remove(cell);
        }


        if(hiddenCandidateCells.size() == candidateNumber && hiddenCandidatePossibles.size() == candidateNumber){
            for (Cell cell: hiddenCandidateCells){
                if (cell.getPossibilities().removeIf(p -> !hiddenCandidatePossibles.contains(p))) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    private boolean pointingPairsAndTriples(){
        boolean flag = false;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if(pointingPairsRow(row * 3, column * 3)){
                    flag = true;
                }
                if(pointingPairsColumn(row * 3, column * 3)){
                    flag = true;
                }
            }
        }

        return flag;
    }

    private boolean pointingPairsRow(int row, int column){
        boolean flag = false;

        //create lists to hold rows
        ArrayList<Integer> row1Possibilities = new ArrayList<>();
        ArrayList<Integer> row2Possibilities = new ArrayList<>();
        ArrayList<Integer> row3Possibilities = new ArrayList<>();

        int row2 = row + 1;
        int row3 = row + 2;

        for (int i = column; i < column + 3; i++) {
            //get possibilities in row 1
            if (sudoku[row][i].getSolution() == 0) {
                for (Integer possible: sudoku[row][i].getPossibilities()) {
                    if (!row1Possibilities.contains(possible)) {
                        row1Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in row 2
            if (sudoku[row2][i].getSolution() == 0) {
                for (Integer possible: sudoku[row2][i].getPossibilities()) {
                    if (!row2Possibilities.contains(possible)) {
                        row2Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in row 3
            if (sudoku[row3][i].getSolution() == 0) {
                for (Integer possible: sudoku[row3][i].getPossibilities()) {
                    if (!row3Possibilities.contains(possible)) {
                        row3Possibilities.add(possible);
                    }
                }
            }
        }

        if(pointingPairRowHelper(row, column, row1Possibilities, row2Possibilities, row3Possibilities)){
            flag = true;
        }
        if(pointingPairRowHelper(row2, column, row2Possibilities, row1Possibilities, row3Possibilities)){
            flag = true;
        }
        if(pointingPairRowHelper(row3, column, row3Possibilities, row1Possibilities, row2Possibilities)){
            flag = true;
        }

        return flag;
    }

    private boolean pointingPairsColumn(int row, int column){
        boolean flag = false;

        //create lists to hold rows
        ArrayList<Integer> column1Possibilities = new ArrayList<>();
        ArrayList<Integer> column2Possibilities = new ArrayList<>();
        ArrayList<Integer> column3Possibilities = new ArrayList<>();

        int column2 = column + 1;
        int column3 = column + 2;

        for (int i = row; i < row + 3; i++) {
            //get possibilities in column 1
            if (sudoku[i][column].getSolution() == 0) {
                for (Integer possible: sudoku[i][column].getPossibilities()) {
                    if (!column1Possibilities.contains(possible)) {
                        column1Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in column 2
            if (sudoku[i][column2].getSolution() == 0) {
                for (Integer possible: sudoku[i][column2].getPossibilities()) {
                    if (!column2Possibilities.contains(possible)) {
                        column2Possibilities.add(possible);
                    }
                }
            }
            //get possibilities in column 3
            if (sudoku[i][column3].getSolution() == 0) {
                for (Integer possible: sudoku[i][column3].getPossibilities()) {
                    if (!column3Possibilities.contains(possible)) {
                        column3Possibilities.add(possible);
                    }
                }
            }
        }

        if(pointingPairColumnHelper(row, column, column1Possibilities, column2Possibilities, column3Possibilities)){
            flag = true;
        }
        if(pointingPairColumnHelper(row, column2, column2Possibilities, column1Possibilities, column3Possibilities)){
            flag = true;
        }
        if(pointingPairColumnHelper(row, column3, column3Possibilities, column1Possibilities, column2Possibilities)){
            flag = true;
        }

        return flag;
    }

    private boolean pointingPairRowHelper(int row, int column, ArrayList<Integer> currentUnit, ArrayList<Integer> otherUnit1, ArrayList<Integer> otherUnit2){
        boolean flag = false;

        for (Integer possibility : currentUnit) {
            //if unit contains unique solution, remove from other cells in other box rows
            if (!(otherUnit1.contains(possibility) || otherUnit2.contains(possibility))) {
                if (column < 3) {
                    for (int i = 3; i < 9; i++) {
                        if (sudoku[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else if (column < 6) {
                    for (int i = 0; i < 3; i++) {
                        if (sudoku[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }

                    for (int i = 6; i < 9; i++) {
                        if (sudoku[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        if (sudoku[row][i].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
    private boolean pointingPairColumnHelper(int row, int column, ArrayList<Integer> currentUnit, ArrayList<Integer> otherUnit1, ArrayList<Integer> otherUnit2){
        boolean flag = false;

        for (Integer possibility : currentUnit) {
            //if unit contains unique solution, remove from other cells in other box rows
            if (!(otherUnit1.contains(possibility) || otherUnit2.contains(possibility))) {
                if (row < 3) {
                    for (int i = 3; i < 9; i++) {
                        if (sudoku[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else if (row < 6) {
                    for (int i = 0; i < 3; i++) {
                        if (sudoku[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }

                    for (int i = 6; i < 9; i++) {
                        if (sudoku[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        if (sudoku[i][column].getPossibilities().remove(possibility)) {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
        public boolean boxLineReduction(){
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(boxLineReductionHelper(i * 3, j * 3)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    private boolean boxLineReductionHelper(int row, int column){
        boolean flag = false;

        int row2 = row + 1;
        int row3 = row + 2;

        int column2 = column + 1;
        int column3 = column + 2;
        ArrayList<Integer> possiblesInRow1 = new ArrayList<>();
        ArrayList<Integer> possiblesInRow2 = new ArrayList<>();
        ArrayList<Integer> possiblesInRow3 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn1 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn2 = new ArrayList<>();
        ArrayList<Integer> possiblesInColumn3 = new ArrayList<>();
        ArrayList<Integer> otherPossiblesInRow1 = boxLineReductionRowHelper(row, column);
        ArrayList<Integer> otherPossiblesInRow2 = boxLineReductionRowHelper(row2, column);
        ArrayList<Integer> otherPossiblesInRow3 = boxLineReductionRowHelper(row3, column);
        ArrayList<Integer> otherPossiblesInColumn1 = boxLineReductionColumnHelper(row, column);
        ArrayList<Integer> otherPossiblesInColumn2 = boxLineReductionColumnHelper(row, column2);
        ArrayList<Integer> otherPossiblesInColumn3 = boxLineReductionColumnHelper(row, column3);

        //get all the possibilities for each row & column in the box
        for (int boxRows = row; boxRows < 3 + row; boxRows++) {
            for (Integer possible: sudoku[boxRows][column].getPossibilities()) {
                if(!possiblesInColumn1.contains(possible)){
                    possiblesInColumn1.add(possible);
                }
            }
            for (Integer possible: sudoku[boxRows][column2].getPossibilities()) {
                if(!possiblesInColumn2.contains(possible)){
                    possiblesInColumn2.add(possible);
                }
            }
            for (Integer possible: sudoku[boxRows][column3].getPossibilities()) {
                if(!possiblesInColumn3.contains(possible)){
                    possiblesInColumn3.add(possible);
                }
            }
        }

        for (int boxColumns = column; boxColumns < 3 + column; boxColumns++) {
            for (Integer possible: sudoku[row][boxColumns].getPossibilities()) {
                if(!possiblesInRow1.contains(possible)){
                    possiblesInRow1.add(possible);
                }
            }
            for (Integer possible: sudoku[row2][boxColumns].getPossibilities()) {
                if(!possiblesInRow2.contains(possible)){
                    possiblesInRow2.add(possible);
                }
            }
            for (Integer possible: sudoku[row3][boxColumns].getPossibilities()) {
                if(!possiblesInRow3.contains(possible)){
                    possiblesInRow3.add(possible);
                }
            }
        }

        if(boxLineReductionRowPossibilityRemover(possiblesInRow1,otherPossiblesInRow1, row2, row3,column)){
            flag = true;
        }
        if(boxLineReductionRowPossibilityRemover(possiblesInRow2,otherPossiblesInRow2, row, row3,column)){
            flag = true;
        }
        if(boxLineReductionRowPossibilityRemover(possiblesInRow3,otherPossiblesInRow3, row, row2,column)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn1,otherPossiblesInColumn1, column2, column3, row)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn2,otherPossiblesInColumn2, column, column3, row)){
            flag = true;
        }
        if(boxLineReductionColumnPossibilityRemover(possiblesInColumn3,otherPossiblesInColumn3, column, column2, row)){
            flag = true;
        }

        return flag;
    }

    //gets the possibles for the other cells in row not in the box
    private ArrayList<Integer> boxLineReductionRowHelper(int row, int column){
        ArrayList<Integer> possibilities = new ArrayList<>();

        if (column < 3) {
            for (int i = 3; i < 9; i++) {
              if(sudoku[row][i].getSolution() == 0){
                  for (Integer possible: sudoku[row][i].getPossibilities()) {
                      if(!possibilities.contains(possible)){
                          possibilities.add(possible);
                      }
                  }
              }
            }
        } else if (column < 6) {
            for (int i = 0; i < 3; i++) {
                if(sudoku[row][i].getSolution() == 0){
                    for (Integer possible: sudoku[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
            for (int i = 6; i < 9; i++) {
                if(sudoku[row][i].getSolution() == 0){
                    for (Integer possible: sudoku[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if(sudoku[row][i].getSolution() == 0){
                    for (Integer possible: sudoku[row][i].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        }

        return possibilities;
    }
    //gets the possibles for the other cells in column not in the box
    private ArrayList<Integer> boxLineReductionColumnHelper(int row, int column){
        ArrayList<Integer> possibilities = new ArrayList<>();

        if (row < 3) {
            for (int i = 3; i < 9; i++) {
                if(sudoku[i][column].getSolution() == 0){
                    for (Integer possible: sudoku[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else if (row < 6) {
            for (int i = 0; i < 3; i++) {
                if(sudoku[i][column].getSolution() == 0){
                    for (Integer possible: sudoku[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
            for (int i = 6; i < 9; i++) {
                if(sudoku[i][column].getSolution() == 0){
                    for (Integer possible: sudoku[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if(sudoku[i][column].getSolution() == 0){
                    for (Integer possible: sudoku[i][column].getPossibilities()) {
                        if(!possibilities.contains(possible)){
                            possibilities.add(possible);
                        }
                    }
                }
            }
        }

        return possibilities;
    }

    private boolean boxLineReductionRowPossibilityRemover(ArrayList<Integer> possiblesInBoxRow, ArrayList<Integer> otherPossiblesInRow, int otherRow1, int otherRow2, int boxColumnTop){
        boolean flag = false;
        for (Integer possible: possiblesInBoxRow) {
            if(!otherPossiblesInRow.contains(possible)){
                for (int boxColumns = boxColumnTop; boxColumns < 3 + boxColumnTop; boxColumns++) {
                    if(sudoku[otherRow1][boxColumns].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                    if(sudoku[otherRow2][boxColumns].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                }

            }
        }

        return flag;
    }
    private boolean boxLineReductionColumnPossibilityRemover(ArrayList<Integer> possiblesInBoxColumn, ArrayList<Integer> otherPossiblesInColumn, int otherColumn1, int otherColumn2, int boxRowTop){
        boolean flag = false;
        for (Integer possible: possiblesInBoxColumn) {
            if(!otherPossiblesInColumn.contains(possible)){
                for (int boxRows = boxRowTop; boxRows < 3 + boxRowTop; boxRows++) {
                    if(sudoku[boxRows][otherColumn1].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                    if(sudoku[boxRows][otherColumn2].getPossibilities().removeIf(p -> Objects.equals(p, possible))){
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }


    private boolean xWingStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            if(xWingRowStrategy(possible)){
                flag = true;
            }
            if (xWingColumnStrategy(possible)){
                flag = true;
            }

        }

        return flag;
    }


    private boolean xWingRowStrategy (int possible){
        boolean flag = false;
        ArrayList<LinkedList<Cell>> xWingPossibles = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            Cell cell1 = new Cell();
            Cell cell2 = new Cell();
            LinkedList<Cell> xWingCandidates = new LinkedList<>();
            int count = 0;
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible)){
                    count++;
                    if(count == 1){
                        cell1 = sudoku[row][column];
                    }else if(count == 2){
                        cell2=sudoku[row][column];
                    }else{
                        break;
                    }
                }
            }
            if(count == 2){
                xWingCandidates.add(cell1);
                xWingCandidates.add(cell2);
                xWingPossibles.add(xWingCandidates);
            }
        }

        if(xWingPossibles.size() < 2){
            return false;
        }

        for (int i = 0; i < xWingPossibles.size(); i++) {
            for (int j = i + 1; j < xWingPossibles.size(); j++) {
                //this means we have an X-wing and can remove the possible from the columns in cells not in the X-wing
                if(xWingPossibles.get(i).getFirst().getColumn() == xWingPossibles.get(j).getFirst().getColumn() &&
                        xWingPossibles.get(i).getLast().getColumn() == xWingPossibles.get(j).getLast().getColumn()){

                    int row1 = xWingPossibles.get(i).getFirst().getRow();
                    int row2 = xWingPossibles.get(j).getFirst().getRow();
                    int column1 = xWingPossibles.get(i).getFirst().getColumn();
                    int column2 = xWingPossibles.get(j).getLast().getColumn();

                    for (int row = 0; row < 9; row++) {
                        if(!(row == row1 || row == row2)){
                            if(sudoku[row][column1].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                            if(sudoku[row][column2].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }

    private boolean xWingColumnStrategy (int possible){

        boolean flag = false;
        ArrayList<LinkedList<Cell>> xWingPossibles = new ArrayList<>();

        for (int column = 0; column < 9; column++) {
            Cell cell1 = new Cell();
            Cell cell2 = new Cell();
            LinkedList<Cell> xWingCandidates = new LinkedList<>();
            int count = 0;
            for (int row = 0; row < 9; row++) {
                if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible)){
                    count++;
                    if(count == 1){
                        cell1 = sudoku[row][column];
                    }else if(count == 2){
                        cell2=sudoku[row][column];
                    }else{
                        break;
                    }
                }
            }
            if(count == 2){
                xWingCandidates.add(cell1);
                xWingCandidates.add(cell2);
                xWingPossibles.add(xWingCandidates);
            }
        }

        if(xWingPossibles.size() < 2){
            return false;
        }

        for (int i = 0; i < xWingPossibles.size(); i++) {
            for (int j = i + 1; j < xWingPossibles.size(); j++) {
                //this means we have an X-wing and can remove the possible from the rows in cells not in the X-wing
                if(xWingPossibles.get(i).getFirst().getRow() == xWingPossibles.get(j).getFirst().getRow() &&
                        xWingPossibles.get(i).getLast().getRow() == xWingPossibles.get(j).getLast().getRow()){

                    int row1 = xWingPossibles.get(i).getFirst().getRow();
                    int row2 = xWingPossibles.get(j).getLast().getRow();
                    int column1 = xWingPossibles.get(i).getFirst().getColumn();
                    int column2 = xWingPossibles.get(j).getFirst().getColumn();

                    for (int column = 0; column < 9; column++) {
                        if(!(column == column1 || column == column2)){
                            if(sudoku[row1][column].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                            if(sudoku[row2][column].getPossibilities().removeIf(p -> p == possible)){
                                flag = true;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }


    private ArrayList<int[]> getCellsWithStrongLinksOld(int possible){
        ArrayList<int[]> cellsWithStrongLinks = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            int rowPossibleCount = 0;
            int columnPossibleCount = 0;
            int[] rowCell1 = new int[2];
            int[] rowCell2 = new int[2];
            int[] columnCell1 = new int[2];
            int[] columnCell2 = new int[2];
            for (int y = 0; y < 9; y++) {
                if (sudoku[x][y].getSolution() == 0 && sudoku[x][y].getPossibilities().contains(possible)) {
                    rowPossibleCount++;
                    if (rowPossibleCount == 1) {
                        rowCell1[0] = x;
                        rowCell1[1] = y;
                    } else if (rowPossibleCount == 2) {
                        rowCell2[0] = x;
                        rowCell2[1] = y;
                    }
                }
                if (sudoku[y][x].getSolution() == 0 && sudoku[y][x].getPossibilities().contains(possible)) {
                    columnPossibleCount++;
                    if (columnPossibleCount == 1) {
                        columnCell1[0] = y;
                        columnCell1[1] = x;
                    } else if (columnPossibleCount == 2) {
                        columnCell2[0] = y;
                        columnCell2[1] = x;
                    }
                }
            }
            if(rowPossibleCount == 2){
                cellsWithStrongLinks.add(rowCell1);
                cellsWithStrongLinks.add(rowCell2);
            }
            if(columnPossibleCount == 2){
                cellsWithStrongLinks.add(columnCell1);
                cellsWithStrongLinks.add(columnCell2);

            } 
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int boxPossibleCount = 0;
                int[] boxCell1 = new int[2];
                int[] boxCell2 = new int[2];
                for (int boxRow = i * 3; boxRow < i * 3 + 3; boxRow++) {
                    for (int boxColumn = j * 3; boxColumn < j * 3 + 3; boxColumn++) {
                        if(sudoku[boxRow][boxColumn].getSolution() == 0 && sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                            boxPossibleCount++;
                            if (boxPossibleCount == 1) {
                                boxCell1[0] = boxRow;
                                boxCell1[1] = boxColumn;
                            } else if (boxPossibleCount == 2) {
                                boxCell2[0] = boxRow;
                                boxCell2[1] = boxColumn;
                            }
                        }
                    }
                }
                if(boxPossibleCount == 2){
                    cellsWithStrongLinks.add(boxCell1);
                    cellsWithStrongLinks.add(boxCell2);
                }
            }
        }
        ArrayList<int[]> newList = new ArrayList<>();

        for (int[] cell: cellsWithStrongLinks) {
            boolean inList = false;
            for (int[] newCell: newList ) {
                if(newCell[0] == cell[0] && newCell[1] == cell[1]){
                    inList = true;
                    break;
                }
            }
            if(!inList){
                newList.add(cell);
            }
        }

        return newList;
    }

    private boolean doCellsHaveStrongLink(int[] cell1, int[] cell2, int possible){
        boolean flag = false;
        int possibleCount = 0;
        if(cell1[0] == cell2[0]){
            for (int column = 0; column < 9; column++) {
                if(sudoku[cell1[0]][column].getPossibilities().contains(possible)){
                    possibleCount++;
                }
            }
        }else if( cell1[1] == cell2[1]){
            for (int row = 0; row < 9; row++) {
                if(sudoku[row][cell1[1]].getPossibilities().contains(possible)){
                    possibleCount++;
                }
            }
        }else if(areCellsInSameBox(cell1, cell2)){
            int[] boxTops = getBox(cell1[0], cell1[1]);
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

    private boolean simpleColoringStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<LinkedList<Cell>> cellsWithStrongLink = getCellsWithStrongLinks(possible);
            if(cellsWithStrongLink.size() < 4){
                continue;
            }
            if (simpleColoringHelper(cellsWithStrongLink, possible)){
                flag = true;
            }
        }

        return flag;
    }

    private boolean simpleColoringHelper(ArrayList<LinkedList<Cell>> cellsWithStrongLink, int possible){
        boolean flag = false;
        ArrayList<Cell> cellsWithOneLink = new ArrayList<>();
        LinkedList<ArrayList<Cell>> coloringChain = null;

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
                if(onCell2 != onCell1 && (onCell1.getRow() == onCell2.getRow() || onCell1.getColumn() == onCell2.getColumn() || areCellsInSameBox(onCell1, onCell2))){
                    for (Cell onCell: onCells) {
                        onCell.getPossibilities().remove(Integer.valueOf(possible));
                    }
                    return true;
                }
            }
        }

        for (Cell offCell1: offCells) {
            for (Cell offCell2: offCells) {
                if(offCell2 != offCell1 && (offCell1.getRow() == offCell2.getRow() || offCell1.getColumn() == offCell2.getColumn() || areCellsInSameBox(offCell1, offCell2))){
                    for (Cell offCell: offCells) {
                        offCell.getPossibilities().remove(Integer.valueOf(possible));
                    }
                    return true;
                }
            }
        }

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(colorChainBoard[row][column] == null && sudoku[row][column].getPossibilities().contains(possible)){
                    int onCellsCount = 0;
                    int offCellsCount = 0;
                    for (Cell cell: onCells) {
                        if(cell.getRow() == row || cell.getColumn() == column || areCellsInSameBox(cell, sudoku[row][column])){
                            onCellsCount++;
                        }
                    }
                    for (Cell cell: offCells) {
                        if(cell.getRow() == row || cell.getColumn() == column || areCellsInSameBox(cell, sudoku[row][column])){
                            offCellsCount++;
                        }
                    }
                    if(onCellsCount >= 1 && offCellsCount >= 1) {
                        sudoku[row][column].getPossibilities().remove(Integer.valueOf(possible));
                        flag = true;
                    }
                }
            }
        }


        return flag;
    }




    private ArrayList<LinkedList<Cell>> getCellsWithStrongLinks(int possible){
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

    private LinkedList<Cell> strongLinksWithCell(Cell cell, int possible){
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

    private boolean doCellsHaveStrongLink(Cell cell1, Cell cell2, int possible){
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

    private boolean yWingStrategy(){

        boolean flag = false;
        ArrayList<Cell> cellsWithTwoPossibles = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getPossibilities().size() == 2){
                    cellsWithTwoPossibles.add(sudoku[row][column]);
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
                if(row == possibleWing.getRow() || column == possibleWing.getColumn() || areCellsInSameBox(possibleHinge, possibleWing)) {
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

    private boolean yWingFinderHelper(LinkedList<Cell> possibleYWing){
        int hingeCellPossible1 = possibleYWing.getFirst().getPossibilities().get(0);
        int hingeCellPossible2 = possibleYWing.getFirst().getPossibilities().get(1);
        int wing1CellPossible1 = possibleYWing.get(1).getPossibilities().get(0);
        int wing1CellPossible2 = possibleYWing.get(1).getPossibilities().get(1);
        int wing2CellPossible1 = possibleYWing.getLast().getPossibilities().get(0);
        int wing2CellPossible2 = possibleYWing.getLast().getPossibilities().get(1);

         if (hingeCellPossible1 == wing1CellPossible1 || hingeCellPossible1 == wing1CellPossible2) {
             if (hingeCellPossible2 == wing2CellPossible1 || hingeCellPossible2 == wing2CellPossible2) {
                 if (wing1CellPossible1 == wing2CellPossible1 ^ wing1CellPossible1 == wing2CellPossible2 ^ wing1CellPossible2 == wing2CellPossible1 ^ wing1CellPossible2 == wing2CellPossible2) {
                     return true;
                 }
             }
         } else if (hingeCellPossible2 == wing1CellPossible1 || hingeCellPossible2 == wing1CellPossible2) {
             if (hingeCellPossible1 == wing2CellPossible1 || hingeCellPossible1 == wing2CellPossible2) {
                 if (wing1CellPossible1 == wing2CellPossible1 ^ wing1CellPossible1 == wing2CellPossible2 ^ wing1CellPossible2 == wing2CellPossible1 ^ wing1CellPossible2 == wing2CellPossible2) {
                     return true;
                 }
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

            ArrayList<Cell> list1 = cellsSeenHelper(yWingEdge1);
            ArrayList<Cell> list2 = cellsSeenHelper(yWingEdge2);
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

    private ArrayList<Cell> cellsSeenHelper(Cell cell){

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
    //TODO: remove this method after other methods that use it switch to using Cell objects instead of int[]s
    private ArrayList<int[]> cellsSeenHelper(int[] cell){

        ArrayList<int[]> cellsSeen = new ArrayList<>();

        int rowTop;
        int columnTop;

        int row = cell[0];
        int column = cell[1];

        if (row < 3) {
            rowTop = 0;
        } else if (row < 6) {
            rowTop = 3;
        } else {
            rowTop = 6;
        }

        if (column < 3) {
            columnTop = 0;
        } else if (column < 6) {
            columnTop = 3;
        } else {
            columnTop = 6;
        }

        //box
        for (int boxRow = rowTop; boxRow < 3 + rowTop; boxRow++) {
            for (int boxColumn = columnTop; boxColumn < 3 + columnTop; boxColumn++) {
                if(sudoku[boxRow][boxColumn].getSolution() == 0 && boxRow != row && boxColumn != column){
                    cellsSeen.add(new int[]{boxRow, boxColumn});
                }
            }
        }
        //row
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(new int[]{row, i}) && sudoku[row][i].getSolution() == 0 && i != column){
                cellsSeen.add(new int[]{row, i});
            }
        }

        //column
        for (int i = 0; i < 9; i++) {
            if(!cellsSeen.contains(new int[]{i, column}) && sudoku[i][column].getSolution() == 0 && i != row){
                cellsSeen.add(new int[]{i, column});
            }
        }

        return cellsSeen;
    }


    private boolean emptyRectangleStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<LinkedList<Cell>> cellsInRowWithStrongLink = new ArrayList<>();
            ArrayList<LinkedList<Cell>> cellsInColumnWithStrongLink = new ArrayList<>();
            int rowCount;
            int columnCount;

            for (int row = 0; row < 9; row++) {
                LinkedList<Cell> rowStrongLink = new LinkedList<>();
                LinkedList<Cell> columnStrongLink = new LinkedList<>();
                Cell rowCell1 = null;
                Cell rowCell2 = null;
                Cell columnCell1 = null;
                Cell columnCell2 = null;
                rowCount = 0;
                columnCount = 0;
                for (int column = 0; column < 9; column++) {
                    if (sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible)) {
                        rowCount++;
                        if (rowCount == 1) {
                            rowCell1 = sudoku[row][column];
                        } else if (rowCount == 2) {
                            rowCell2 = sudoku[row][column];

                        }
                    }

                    if (sudoku[column][row].getSolution() == 0 && sudoku[column][row].getPossibilities().contains(possible)) {
                        columnCount++;
                        if (columnCount == 1) {
                            columnCell1 = sudoku[column][row];
                        } else if (columnCount == 2) {
                            columnCell2 = sudoku[column][row];
                        }
                    }
                }
                if (rowCount == 2 && !areCellsInSameBox(rowCell1, rowCell2)) {
                    rowStrongLink.add(rowCell1);
                    rowStrongLink.add((rowCell2));
                    cellsInRowWithStrongLink.add(rowStrongLink);
                }
                if (columnCount == 2 && !areCellsInSameBox(columnCell1, columnCell2)) {
                    columnStrongLink.add(columnCell1);
                    columnStrongLink.add(columnCell2);
                    cellsInColumnWithStrongLink.add(columnStrongLink);
                }
            }

            if(emptyRectangleStrategyRowHelper(cellsInRowWithStrongLink, possible)){
                flag = true;
            }
            if(emptyRectangleStrategyColumnHelper(cellsInColumnWithStrongLink, possible)){
                flag = true;
            }

        }
        return flag;
    }

    private boolean emptyRectangleStrategyRowHelper(ArrayList<LinkedList<Cell>> cellsInRowWithStrongLink, int possible){
        boolean flag = false;

        if(cellsInRowWithStrongLink.size() < 1){
            return false;
        }
        for (LinkedList<Cell> strongLinkPair: cellsInRowWithStrongLink) {
            if((strongLinkPair.getFirst().getPossibilities().size() < 2 || strongLinkPair.getLast().getPossibilities().size() < 2) ||
                    (!strongLinkPair.getFirst().getPossibilities().contains(possible)  || !strongLinkPair.getLast().getPossibilities().contains(possible))){
                continue;
            }
            boolean strongLinkEmptyRectangle = false;
            for (int index = 0 ; index < strongLinkPair.size(); index++) {

                ArrayList<Cell> possibleWingCells = new ArrayList<>();
                Cell hingeCell = strongLinkPair.get(index);
                Cell wingCell1;
                if(index == 0){
                     wingCell1 = strongLinkPair.getLast();
                }else{
                     wingCell1 = strongLinkPair.getFirst();
                }

                int column = hingeCell.getColumn();
                int count = 0;
                for (int row = 0; row < 9; row++) {
                    if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible)){
                        count++;
                        if(sudoku[row][column] != hingeCell && !areCellsInSameBox(sudoku[row][column], hingeCell)){
                            possibleWingCells.add(sudoku[row][column]);
                        }
                    }
                }
                //this ensures there isn't a strong link between the hinge cell and wing cell. That is an edge case with separate logic
                // try to find an empty rectangle with the possible wing cells
                for (Cell possibleWingCell: possibleWingCells) {
                    ArrayList<Cell>  intersectingBoxCellsWithPossible = getBoxCellsWithPossibility(getBox(possibleWingCell.getRow(), wingCell1.getColumn()), possible);
                    if(intersectingBoxCellsWithPossible.size() <= 1){
                        continue;
                    }
                    boolean isEmptyRectangle = true;
                    for (Cell cell : intersectingBoxCellsWithPossible) {
                        if (!(cell.getColumn() == wingCell1.getColumn() || cell.getRow() == possibleWingCell.getRow())) {
                            isEmptyRectangle = false;
                            break;
                        }
                    }
                    if(isEmptyRectangle){
                        if(count > 2) {
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            break;
                        }else if(count == 2){
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            wingCell1.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            strongLinkEmptyRectangle = true;
                            break;
                        }
                    }
                }
                if (strongLinkEmptyRectangle){
                    break;
                }
            }
        }

        return flag;
    }
    private boolean emptyRectangleStrategyColumnHelper(ArrayList<LinkedList<Cell>> cellsInColumnWithStrongLink, int possible){
        boolean flag = false;

        if(cellsInColumnWithStrongLink.size() < 1){
            return false;
        }

        for (LinkedList<Cell> strongLinkPair: cellsInColumnWithStrongLink) {
            if((strongLinkPair.getFirst().getPossibilities().size() < 2 || strongLinkPair.getLast().getPossibilities().size() < 2) ||
                    (!strongLinkPair.getFirst().getPossibilities().contains(possible)  || !strongLinkPair.getLast().getPossibilities().contains(possible))){
                continue;
            }
            boolean strongLinkEmptyRectangle = false;
            for (int index = 0 ; index < strongLinkPair.size(); index++) {
                ArrayList<Cell> possibleWingCells = new ArrayList<>();
                Cell hingeCell = strongLinkPair.get(index);
                Cell wingCell1;
                if(index == 0){
                    wingCell1 = strongLinkPair.getLast();
                }else{
                    wingCell1 = strongLinkPair.getFirst();
                }
                int row = hingeCell.getRow();
                int count = 0;
                for (int column = 0; column < 9; column++) {
                    if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible)){
                        count++;
                        if(sudoku[row][column] != hingeCell && !areCellsInSameBox(sudoku[row][column], hingeCell)){
                            possibleWingCells.add(sudoku[row][column]);
                        }
                    }
                }
                for (Cell possibleWingCell: possibleWingCells) {
                        ArrayList<Cell>  intersectingBoxCellsWithPossible = getBoxCellsWithPossibility(getBox(possibleWingCell.getColumn(), wingCell1.getRow()), possible);
                        if(intersectingBoxCellsWithPossible.size() <= 1){
                            continue;
                        }
                        boolean isEmptyRectangle = true;
                        for (Cell cell : intersectingBoxCellsWithPossible) {
                            if (!(cell.getRow() == wingCell1.getRow() || cell.getColumn() == possibleWingCell.getColumn())) {
                                isEmptyRectangle = false;
                                break;
                            }
                        }
                    if(isEmptyRectangle){
                        if(count > 2) {
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                        }else if(count == 2){
                            possibleWingCell.getPossibilities().removeIf(p -> p == possible);
                            wingCell1.getPossibilities().removeIf(p -> p == possible);
                            flag = true;
                            strongLinkEmptyRectangle = true;
                            break;
                        }
                    }
                }
                if (strongLinkEmptyRectangle){
                    break;
                }
            }
        }
        return flag;
    }

    private boolean areCellsInSameBox(Cell cell1, Cell cell2){
       int[] box1 = getBox(cell1.getRow(), cell1.getColumn());
       int[] box2 = getBox(cell2.getRow(), cell2.getColumn());

       return (box1[0] == box2[0] && box1[1] == box2[1]);
    }

        private boolean areCellsInSameBox(int[] cell1, int[]cell2){
        int[] box1 = getBox(cell1[0], cell1[1]);
        int[] box2 = getBox(cell2[0], cell2[1]);

        return (box1[0] == box2[0] && box1[1] == box2[1]);
    }

    private ArrayList<Cell> getBoxCellsWithPossibility(int[] boxTop, int possible){
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

    private ArrayList<int[]> getBoxCellsWithPossibility(int boxRowTop, int boxColumnTop, int possible){
        ArrayList<int[]> cellsWithPossibleInBox = new ArrayList<>();
        for (int boxRow = boxRowTop; boxRow < boxRowTop + 3; boxRow++) {
            for (int boxColumn = boxColumnTop; boxColumn < boxColumnTop + 3; boxColumn++) {
                if(sudoku[boxRow][boxColumn].getSolution() == 0 && sudoku[boxRow][boxColumn].getPossibilities().contains(possible)){
                    cellsWithPossibleInBox.add(new int[]{boxRow, boxColumn});
                }
            }
        }
        return  cellsWithPossibleInBox;
    }

    private boolean xyzWingStrategy(){
        boolean flag = false;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getPossibilities().size() == 3){
                    if(xyzWingFinder(sudoku[row][column])){
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean xyzWingFinder(Cell hingeCell){
        boolean flag = false;
        int hingeCellRow = hingeCell.getRow();
        int hingeCellColumn = hingeCell.getColumn();
        int[] boxTop = getBox(hingeCellRow, hingeCellColumn);
        ArrayList<Cell> possibleWingInRow = new ArrayList<>();
        ArrayList<Cell> possibleWingInColumn = new ArrayList<>();
        ArrayList<Cell> possibleWingInBox = new ArrayList<>();
        ArrayList<Integer> xyzWingPossibles = hingeCell.getPossibilities();
        LinkedList<Cell> xyzWing = new LinkedList<>();
        xyzWing.add(hingeCell);
        //find  possible wings
        for (int i = 0; i < 9; i++) {
            //row
            if(sudoku[hingeCellRow][i].getPossibilities().size() == 2 && sudoku[hingeCellRow][i] != hingeCell &&
                    !areCellsInSameBox(sudoku[hingeCellRow][i], hingeCell) && xyzWingPossibles.containsAll(sudoku[hingeCellRow][i].getPossibilities())){
                possibleWingInRow.add(sudoku[hingeCellRow][i]);
            }
            //column
            if(sudoku[i][hingeCellColumn].getPossibilities().size() == 2 && sudoku[i][hingeCellColumn] != hingeCell &&
             !areCellsInSameBox(sudoku[i][hingeCellColumn], hingeCell) && xyzWingPossibles.containsAll(sudoku[i][hingeCellColumn].getPossibilities())){
                possibleWingInColumn.add(sudoku[i][hingeCellColumn]);
            }
        }
        //box
        for (int boxRow = boxTop[0]; boxRow < 3 + boxTop[0]; boxRow++) {
            for (int boxColumn = boxTop[1]; boxColumn < 3 + boxTop[1]; boxColumn++) {
                if ( sudoku[boxRow][boxColumn] != hingeCell && sudoku[boxRow][boxColumn].getPossibilities().size() == 2 &&
                        xyzWingPossibles.containsAll(sudoku[boxRow][boxColumn].getPossibilities())){
                    possibleWingInBox.add(sudoku[boxRow][boxColumn]);
                }
            }
        }

        //Find xyzWings and remove possibles seen by all three cells
        for (Cell boxCell: possibleWingInBox) {
            xyzWing.add(boxCell);
            for (Cell rowCell: possibleWingInRow) {
                if(rowCell.getPossibilities().contains(boxCell.getPossibilities().get(0)) ^ rowCell.getPossibilities().contains(boxCell.getPossibilities().get(1))){
                    xyzWing.add(rowCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }
        for (Cell boxCell: possibleWingInBox) {
            xyzWing.add(boxCell);
            for (Cell columnCell: possibleWingInColumn) {
                if(columnCell.getPossibilities().contains(boxCell.getPossibilities().get(0)) ^ columnCell.getPossibilities().contains(boxCell.getPossibilities().get(1))){
                    xyzWing.add(columnCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }

        for (Cell rowCell: possibleWingInRow) {
            xyzWing.add(rowCell);
            for (Cell columnCell: possibleWingInColumn) {
                if(columnCell.getPossibilities().contains(rowCell.getPossibilities().get(0)) ^ columnCell.getPossibilities().contains(rowCell.getPossibilities().get(1))){
                    xyzWing.add(columnCell);
                    if(xyzWingPossibleRemover(xyzWing)){
                        flag = true;
                    }
                    xyzWing.removeLast();
                }
            }
            xyzWing.removeLast();
        }

/*
        if(xyzWingCells.size() == 3){
            int possibleInAllCells = 0;
            //ensure only one number is in all 3 cells
            int possibleIn3Cells = 0;
            for (Integer possible: xyzWingPossibles) {
                int possibleCount = 0;
                for (int[] cell: xyzWingCells) {
                    if(sudoku[cell[0]][cell[1]].getPossibilities().contains(possible)){
                        possibleCount++;
                    }
                }
                if (possibleCount == 3){
                    possibleIn3Cells++;
                    possibleInAllCells = possible;
                }
            }
            if(possibleIn3Cells == 1){
                //ensure edges only have 1 matching possible
                if((Objects.equals(sudoku[xyzWingCells.get(1)[0]][xyzWingCells.get(1)[1]].getPossibilities().get(0), sudoku[xyzWingCells.get(2)[0]][xyzWingCells.get(2)[1]].getPossibilities().get(0)) ^
                        Objects.equals(sudoku[xyzWingCells.get(1)[0]][xyzWingCells.get(1)[1]].getPossibilities().get(0), sudoku[xyzWingCells.get(2)[0]][xyzWingCells.get(2)[1]].getPossibilities().get(1))) ^
                        (Objects.equals(sudoku[xyzWingCells.get(1)[0]][xyzWingCells.get(1)[1]].getPossibilities().get(1), sudoku[xyzWingCells.get(2)[0]][xyzWingCells.get(2)[1]].getPossibilities().get(0)) ^
                                Objects.equals(sudoku[xyzWingCells.get(1)[0]][xyzWingCells.get(1)[1]].getPossibilities().get(1), sudoku[xyzWingCells.get(2)[0]][xyzWingCells.get(2)[1]].getPossibilities().get(1)))){
                    //find cell visible to all xyzWingCells

                    ArrayList<int[]> list1 = cellsSeenHelper(xyzWingCells.get(0));
                    ArrayList<int[]> list2 = cellsSeenHelper(xyzWingCells.get(1));
                    ArrayList<int[]> list3 = cellsSeenHelper(xyzWingCells.get(2));
                    ArrayList<int[]> xyzWingCellsSeen = new ArrayList<>();

                    for (int[] cell: list1) {
                        for (int[] cell2: list2) {
                            for (int[] cell3: list3) {
                                if (cell[0] == cell2[0] && cell[0] == cell3[0] &&
                                        cell[1] == cell2[1] && cell[1] == cell3[1]) {
                                    xyzWingCellsSeen.add(cell);
                                }
                            }
                        }
                    }

                    //remove the shared possible of all 3 xyzWing  cells from the cells visible to all three cells in the xyzWing
                    for (int[] cell: xyzWingCellsSeen) {
                        if(sudoku[cell[0]][cell[1]].getPossibilities().remove(Integer.valueOf(possibleInAllCells))){
                            flag = true;
                        }
                    }
                }
            }
        }*/

        return flag;
    }

    private boolean xyzWingPossibleRemover(LinkedList<Cell> xyzWing){
        boolean flag = false;

        int possibleToRemove;

        if(xyzWing.get(1).getPossibilities().contains(xyzWing.getLast().getPossibilities().get(0))){
            possibleToRemove = xyzWing.getLast().getPossibilities().get(0);
        }else{
            possibleToRemove = xyzWing.getLast().getPossibilities().get(1);
        }

        ArrayList<Cell> cellsSeenByHinge = cellsSeenHelper(xyzWing.getFirst());
        ArrayList<Cell> cellsSeenByWing1 = cellsSeenHelper(xyzWing.get(1));
        ArrayList<Cell> cellsSeenByWing2 = cellsSeenHelper(xyzWing.getLast());
        ArrayList<Cell> cellsSeenByAll = new ArrayList<>();

        for (Cell cell : cellsSeenByHinge) {
            if(cellsSeenByWing1.contains(cell) && cellsSeenByWing2.contains(cell)){
                cellsSeenByAll.add(cell);
            }
        }

        for (Cell cell: cellsSeenByAll) {
            if(cell.getPossibilities().removeIf(p -> p == possibleToRemove)){
                flag = true;
            }
        }

        return flag;
    }

private boolean swordfishStrategy(){
    boolean flag = false;

    for (int possible = 1; possible <= 9; possible++) {
        if(swordfishRow(possible)){
            flag = true;
        }
        if(swordfishColumn(possible)){
            flag = true;
        }
    }

    return flag;
}

    private boolean swordfishRow(int possible){
        boolean flag = false;
        int count;
        Cell cell1;
        Cell cell2;
        Cell cell3;
        ArrayList<LinkedList<Cell>> possibleSwordfishCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            count = 0;
            cell1 = null;
            cell2 = null;
            cell3 = null;
            LinkedList<Cell> cellsWithPossibleInRow = new LinkedList<>();
            for (int column = 0; column < 9; column++) {
                if (sudoku[row][column].getSolution() == 0 && sudoku [row][column].getPossibilities().contains(possible)) {
                    count++;
                    switch (count) {
                        case 1 -> cell1 = sudoku[row][column];
                        case 2 -> cell2 = sudoku[row][column];
                        case 3 -> cell3 = sudoku[row][column];
                        default -> {
                        }
                    }
                    if(count > 3){
                        break;
                    }
                }
            }
            if(count <= 3 && count >= 2){
                cellsWithPossibleInRow.add(cell1);
                cellsWithPossibleInRow.add(cell2);
                cellsWithPossibleInRow.add(cell3);
                possibleSwordfishCells.add(cellsWithPossibleInRow);
            }
        }

        if(possibleSwordfishCells.size() < 3){
            return false;
        }
        ArrayList<Integer> columnsRow1 = new ArrayList<>();
        ArrayList<Integer> columnsRow2 = new ArrayList<>();
        ArrayList<Integer> columnsRow3 = new ArrayList<>();
        ArrayList<Integer> totalColumns = new ArrayList<>();
        LinkedList<ArrayList<Integer>> columnsInRows = new LinkedList<>();

        for (int row1 = 0; row1 < possibleSwordfishCells.size() - 2; row1++) {
            for (Cell row1Cell: possibleSwordfishCells.get(row1)) {
                if(row1Cell != null) {
                    int column = row1Cell.getColumn();
                    if (!columnsRow1.contains(column)) {
                        columnsRow1.add(column);
                    }
                }
            }
            columnsInRows.add(columnsRow1);
            for (int row2 = row1 + 1; row2 < possibleSwordfishCells.size() - 1; row2++) {
                for (Cell row2Cell: possibleSwordfishCells.get(row2)) {
                    if(row2Cell != null) {
                        int column = row2Cell.getColumn();
                        if (!columnsRow2.contains(column)) {
                            columnsRow2.add(column);
                        }
                    }
                }
                columnsInRows.add(columnsRow2);
                for (int row3 = row2 + 1; row3 < possibleSwordfishCells.size(); row3++) {
                    for (Cell row3Cell: possibleSwordfishCells.get(row3)) {
                        if(row3Cell != null) {
                            int column = row3Cell.getColumn();
                            if (!columnsRow3.contains(column)) {
                                columnsRow3.add(column);
                            }
                        }
                    }
                    columnsInRows.add(columnsRow3);
                    for (ArrayList<Integer> cellColumns: columnsInRows) {
                        for (Integer column: cellColumns) {
                            if(!totalColumns.contains(column)){
                                totalColumns.add(column);
                            }
                        }
                    }
                    if(totalColumns.size() == 3){
                        for (Integer column: totalColumns) {
                            for (int row = 0; row < 9; row++) {
                                if(sudoku[row][column].getPossibilities().contains(possible) &&
                                   !possibleSwordfishCells.get(row1).contains(sudoku[row][column]) &&
                                   !possibleSwordfishCells.get(row2).contains(sudoku[row][column]) &&
                                   !possibleSwordfishCells.get(row3).contains(sudoku[row][column])){
                                    if(sudoku[row][column].getPossibilities().removeIf(p -> p == possible)){
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                    columnsRow3.clear();
                    totalColumns.clear();
                    columnsInRows.removeLast();
                }
                columnsRow2.clear();
                columnsInRows.removeLast();
            }
            columnsRow1.clear();
            columnsInRows.clear();
        }

        return flag;
    }
    private boolean swordfishColumn(int possible){
        boolean flag = false;
        int count;
        Cell cell1;
        Cell cell2;
        Cell cell3;
        ArrayList<LinkedList<Cell>> possibleSwordfishCells = new ArrayList<>();

        for (int column = 0; column < 9; column++) {
            count = 0;
            cell1 = null;
            cell2 = null;
            cell3 = null;
            LinkedList<Cell> cellsWithPossibleInColumn = new LinkedList<>();
            for (int row = 0; row < 9; row++) {
                if (sudoku[row][column].getSolution() == 0 && sudoku [row][column].getPossibilities().contains(possible)) {
                    count++;
                    switch (count) {
                        case 1 -> cell1 = sudoku[row][column];
                        case 2 -> cell2 = sudoku[row][column];
                        case 3 -> cell3 = sudoku[row][column];
                        default -> {
                        }
                    }
                    if(count > 3){
                        break;
                    }
                }
            }
            if(count <= 3 && count >= 2){
                cellsWithPossibleInColumn.add(cell1);
                cellsWithPossibleInColumn.add(cell2);
                cellsWithPossibleInColumn.add(cell3);
                possibleSwordfishCells.add(cellsWithPossibleInColumn);
            }
        }

        if(possibleSwordfishCells.size() < 3){
            return false;
        }
        ArrayList<Integer> rowsColumn1 = new ArrayList<>();
        ArrayList<Integer> rowsColumn2 = new ArrayList<>();
        ArrayList<Integer> rowsColumn3 = new ArrayList<>();
        ArrayList<Integer> totalRows = new ArrayList<>();
        LinkedList<ArrayList<Integer>> rowsInColumns = new LinkedList<>();

        for (int column1 = 0; column1 < possibleSwordfishCells.size() - 2; column1++) {
            for (Cell column1Cell: possibleSwordfishCells.get(column1)) {
                if(column1Cell != null) {
                    int row = column1Cell.getRow();
                    if (!rowsColumn1.contains(row)) {
                        rowsColumn1.add(row);
                    }
                }
            }
            rowsInColumns.add(rowsColumn1);
            for (int column2 = column1 + 1; column2 < possibleSwordfishCells.size() - 1; column2++) {
                for (Cell column2Cell: possibleSwordfishCells.get(column2)) {
                    if(column2Cell != null) {
                        int row = column2Cell.getRow();
                        if (!rowsColumn2.contains(row)) {
                            rowsColumn2.add(row);
                        }
                    }
                }
                rowsInColumns.add(rowsColumn2);
                for (int column3 = column2 + 1; column3 < possibleSwordfishCells.size(); column3++) {
                    for (Cell column3Cell: possibleSwordfishCells.get(column3)) {
                        if(column3Cell != null) {
                            int row = column3Cell.getRow();
                            if (!rowsColumn3.contains(row)) {
                                rowsColumn3.add(row);
                            }
                        }
                    }
                    rowsInColumns.add(rowsColumn3);
                    for (ArrayList<Integer> cellRows: rowsInColumns) {
                        for (Integer row: cellRows) {
                            if(!totalRows.contains(row)){
                                totalRows.add(row);
                            }
                        }
                    }
                    if(totalRows.size() == 3){
                        for (Integer row: totalRows) {
                            for (int column = 0; column < 9; column++) {
                                if(sudoku[row][column].getPossibilities().contains(possible) &&
                                        !possibleSwordfishCells.get(column1).contains(sudoku[row][column]) &&
                                        !possibleSwordfishCells.get(column2).contains(sudoku[row][column]) &&
                                        !possibleSwordfishCells.get(column3).contains(sudoku[row][column])){
                                    if(sudoku[row][column].getPossibilities().removeIf(p -> p == possible)){
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                    rowsColumn3.clear();
                    totalRows.clear();
                    rowsInColumns.removeLast();
                }
                rowsColumn2.clear();
                rowsInColumns.removeLast();
            }
            rowsColumn1.clear();
            rowsInColumns.removeFirst();
        }
        return flag;
    }


    //bi-value universal grave strategy
    private boolean bugStrategy(){
        boolean flag = false;
        int count = 0;
        Cell bugCell = null;
        int possibleThatKillsBug = 0;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().size() == 2){
                    continue;
                }else if (sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().size() == 3){
                    bugCell = sudoku[row][column];
                    count++;
                }

                if(count > 1 || sudoku[row][column].getPossibilities().size() > 3){
                    return false;
                }
            }
        }
        if(count == 0){
            return false;
        }

       // ArrayList<Integer> bugCellPossibilities = new ArrayList<>(sudoku[bugCell[0]][bugCell[1]].getPossibilities());
        int[] boxTop = getBox(bugCell.getRow(), bugCell.getColumn());

        for (Integer possible: bugCell.getPossibilities()) {
            int boxCount = getBoxCellsWithPossibility(boxTop[0],boxTop[1], possible).size();
            int rowCount = 0;
            int columnCount = 0;

            int bugRow = bugCell.getRow();
            int bugColumn = bugCell.getColumn();
            for (int i = 0; i < 9; i++) {
                if(sudoku[bugRow][i].getSolution() == 0 && sudoku[bugRow][i].getPossibilities().contains(possible)){
                    rowCount++;
                }
                if(sudoku[i][bugColumn].getSolution() == 0 && sudoku[i][bugColumn].getPossibilities().contains(possible)){
                    columnCount++;
                }
            }

            if(boxCount == 3 || columnCount == 3 || rowCount == 3){
                possibleThatKillsBug = possible;
                break;
            }
        }

        int finalPossibleThatKillsBug = possibleThatKillsBug;
        if(bugCell.getPossibilities().removeIf(p -> p != finalPossibleThatKillsBug)){
            flag = true;
        }

        return flag;
    }

    private boolean xChainStrategy(){
        boolean flag = false;

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<int[]> cellsWithStrongLink = getCellsWithStrongLinksOld(possible);
            if(cellsWithStrongLink.size() == 0){
                continue;
            }
            if (findXChain(cellsWithStrongLink, possible)){
                flag = true;
            }
        }

        return flag;
    }

    private boolean findXChain(ArrayList<int[]> cellsWithStrongLink, int possible){
        boolean flag = false;

        ArrayList<LinkedList<int[]>> xChains = xChainFinderHelper(cellsWithStrongLink, possible);

        for (LinkedList<int[]> chain: xChains ) {
            if(chain.size() < 4){
                continue;
            }

            for (int chainLength = 3; chainLength < chain.size() - 2; chainLength += 2) {
                for (int currentChain = 0; currentChain <= chain.size() - 3; currentChain += 2) {
                    int endOfChain = currentChain + chainLength;
                    if(endOfChain >= chain.size()){
                        continue;
                    }

                    int[] chainStart = chain.get(currentChain);
                    int[] chainEnd = chain.get(endOfChain);


                    LinkedList<int[]> tempChain = new LinkedList<>();
                    for (int i = currentChain; i <= endOfChain; i++) {
                        tempChain.add(chain.get(i));
                    }

                    //this chain forms a loop/cycle, allows for more eliminations
                    if(chainStart[0] == chainEnd[0] || chainStart[1] == chainEnd[1] || areCellsInSameBox(chainStart, chainEnd)){
                        if(xCycleStrategy(tempChain, possible)){
                            flag = true;
                        }
                        continue;
                    }

                    //remove possible in cells that can see both on and off cells of the chain
                    ArrayList<int[]> startCellSees = cellsSeenHelper(chainStart);
                    ArrayList<int[]> startAndEndSees = new ArrayList<>();

                    for (int[] cell1: startCellSees) {
                        if(cell1[0] == chainEnd[0] || cell1[1] == chainEnd[1] || areCellsInSameBox(cell1, chainEnd)){
                            startAndEndSees.add(cell1);
                        }
                    }

                    ArrayList<int[]> temp = new ArrayList<>();
                    for (int[] cellSeen: startAndEndSees) {
                        boolean inChain = false;
                        for (int[] link: tempChain) {
                            if(cellSeen[0] == link[0] && cellSeen[1] == link[1]){
                                inChain = true;
                                break;
                            }
                        }
                        if(!inChain){
                            temp.add(cellSeen);
                        }
                    }
                    startAndEndSees = temp;
                    for (int[] cell: startAndEndSees) {
                        if(sudoku[cell[0]][cell[1]].getPossibilities().removeIf(p -> p == possible)){
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }

    //TODO: find a better way to find all the chains
    private ArrayList<LinkedList<int[]>> xChainFinderHelper(ArrayList<int[]> cellsWithStrongLink, int possible) {
        ArrayList<LinkedList<int[]>> xChains = new ArrayList<>();
        for (int[] cell: cellsWithStrongLink){
            xChains.addAll(xChainBuilder(cell, cellsWithStrongLink, possible));
        }

        return xChains;
    }

    private ArrayList<LinkedList<int[]>> xChainBuilder (int[] cell, ArrayList<int[]> cellsWithStrongLink, int possible){
        ArrayList<LinkedList<int[]>> xChains = new ArrayList<>();
        LinkedList<int[]> xChain = new LinkedList<>();
        xChain.add(cell);

        ArrayList<int[]> strongLinks = getNextStrongLink(xChain, cellsWithStrongLink, possible);
        ArrayList<LinkedList<int[]>> tempChains = new ArrayList<>();
        for (int[] strongLink: strongLinks) {
            LinkedList<int[]> newChain = new LinkedList<>(xChain);
            newChain.add(strongLink);
            tempChains.add(newChain);

        }
        for (LinkedList<int[]> chain: tempChains) {
            xChains.addAll(getNextWeakLink(chain, cellsWithStrongLink, possible));
        }



        return xChains;
    }


    private ArrayList<LinkedList<int[]>> getNextWeakLink(LinkedList<int[]> links, ArrayList<int[]> cellsWithStrongLink, int possible){
        ArrayList<LinkedList<int[]>> xChains = new ArrayList<>();
        xChains.add(links);
        boolean flag = true;
        while(flag) {
            flag = false;
            ArrayList<LinkedList<int[]>> tempChains = new ArrayList<>(xChains);

            for (LinkedList<int[]> xChain: xChains) {
                int[] lastCellInChain = xChain.getLast();
                ArrayList<int[]> weakLink = new ArrayList<>();
                for (int[] cell : cellsWithStrongLink) {
                    if (!xChain.contains(cell) && (cell[0] == lastCellInChain[0] || cell[1] == lastCellInChain[1] || areCellsInSameBox(cell, lastCellInChain))) {
                        weakLink.add(cell);
                    }
                }
                for (int[] cell : weakLink) {
                    LinkedList<int[]> chain = new LinkedList<>(xChain);
                    chain.add(cell);
                    ArrayList<int[]> strongLinks = getNextStrongLink(chain, cellsWithStrongLink, possible);
                    flag = true;
                    tempChains.remove(xChain);
                    for (int[] link: strongLinks) {
                        LinkedList<int[]> newChain = new LinkedList<>(chain);
                        newChain.add(link);
                        tempChains.add(newChain);
                    }
                }
            }
            xChains = new ArrayList<>(tempChains);
        }

        return xChains;
    }

    private ArrayList<int[]> getNextStrongLink(LinkedList<int[]> xChain, ArrayList<int[]> cellsWithStrongLink, int possible){
        int[] lastCellInChain = xChain.getLast();
        ArrayList<int[]> strongLinks = new ArrayList<>();
        for (int[] cell: cellsWithStrongLink) {
            if(!xChain.contains(cell) && doCellsHaveStrongLink(cell, lastCellInChain, possible)){
                strongLinks.add(cell);
            }
        }
        return strongLinks;

    }

    private boolean xCycleStrategy(LinkedList<int[]> xChain, int possible){
        boolean flag = false;
        Boolean[][] chainBoard= new Boolean[9][9];
        ArrayList<int[]> cellsAdded = new ArrayList<>();
        ArrayList<int[]> onCells = new ArrayList<>();
        ArrayList<int[]> offCells = new ArrayList<>();

        chainBoard[xChain.getFirst()[0]][xChain.getFirst()[1]] = true;
        cellsAdded.add(xChain.getFirst());

        boolean onCell = true;

        for (int[] cell: xChain) {
            if(onCell){
                onCells.add(cell);
                onCell = false;
            }else{
                offCells.add(cell);
                onCell = true;
            }
        }

        for (int[] cell: onCells) {
            chainBoard[cell[0]][cell[1]] = true;
        }
        for (int[] cell: offCells) {
            chainBoard[cell[0]][cell[1]] = false;
        }

        //remove possible in cells that can see both on and off cells of the chain
        Boolean[][] cellsWithPossibleNotInChain = new Boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().contains(possible) && chainBoard[row][column] == null){
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
                    for (int[] cell: onCells) {
                        if(cell[0] == row || cell[1] == column || areCellsInSameBox(cell, new int[]{row,column})){
                            onCellsCount++;
                        }
                    }
                    for (int[] cell: offCells) {
                        if(cell[0] == row || cell[1] == column || areCellsInSameBox(cell, new int[]{row,column})){
                            offCellsCount++;
                        }
                    }

                    if(onCellsCount >= 1 && offCellsCount >= 1){
                        if(sudoku[row][column].getPossibilities().removeIf(p -> p == possible)){
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }

    private boolean xyChainStrategy(){
        boolean flag = false;

        ArrayList<int[]> xyChainCandidates = new ArrayList<>();
        ArrayList<LinkedList<int[]>> xyChains = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().size() == 2) {
                    xyChainCandidates.add(new int[]{row, column});
                }
            }
        }

        for (int[] cell : xyChainCandidates) {

            for (int possible : sudoku[cell[0]][cell[1]].getPossibilities()) {
                xyChains.addAll(xyChainBuilder(cell, xyChainCandidates, possible));

                for (LinkedList<int[]> chain : xyChains) {
                    int[] chainStart = chain.getFirst();
                    int[] chainEnd = chain.getLast();
                    ArrayList<int[]> startCellSees = cellsSeenHelper(chainStart);
                    ArrayList<int[]> startAndEndSees = new ArrayList<>();

                    for (int[] seenCell : startCellSees) {
                        if (seenCell[0] == chainEnd[0] || seenCell[1] == chainEnd[1] || areCellsInSameBox(seenCell, chainEnd)) {
                            startAndEndSees.add(seenCell);
                        }
                    }

                    ArrayList<int[]> temp = new ArrayList<>();
                    for (int[] cellSeen : startAndEndSees) {

                        if( !(cellSeen[0] == chainStart[0] && cellSeen[1] == chainStart[1]) &&
                            !(cellSeen[0] == chainEnd[0] && cellSeen[1] == chainEnd[1]) ){
                            temp.add(cellSeen);
                        }
                    }

                    startAndEndSees = temp;
                    for (int[] seenCell : startAndEndSees) {
                        if (sudoku[seenCell[0]][seenCell[1]].getPossibilities().removeIf(p -> p == possible)) {
                            flag = true;
                        }
                    }
                }

                if(flag){
                    return true;
                }else {
                    xyChains.clear();
                }
            }
        }

        return flag;
    }

    private ArrayList<LinkedList<int[]>> xyChainBuilder (int[] cell, ArrayList<int[]> xyChainCandidates, int startPossible){
        ArrayList<LinkedList<int[]>> xyChains = new ArrayList<>();

        int[] possibles = sudoku[cell[0]][cell[1]].getPossibilities().stream().mapToInt(i -> i).toArray();
        LinkedList<int[]> xyChain = new LinkedList<>();
        xyChain.add(cell);
        int possible;

        if(startPossible == possibles[0]){
            possible = possibles[1];
        }else{
            possible = possibles[0];
        }

        xyChains.addAll(xyBuildChain(xyChain, xyChainCandidates, startPossible, possible));

        return xyChains;
    }

    private ArrayList<LinkedList<int[]>> xyBuildChain(LinkedList<int[]> xyChain, ArrayList<int[]> xyChainCandidates, int startPossible, int possible){
        ArrayList<LinkedList<int[]>> xyChains = new ArrayList<>();
        ArrayList<LinkedList<int[]>> tempChains = new ArrayList<>(xyChains);
        ArrayList<int[]> possibleLinks = new ArrayList<>();

        for (int[] possibleLink : xyChainCandidates) {
            if (!xyChain.contains(possibleLink) && sudoku[possibleLink[0]][possibleLink[1]].getPossibilities().contains(possible) &&
                    (xyChain.getLast()[0] == possibleLink[0] || xyChain.getLast()[1] == possibleLink[1] || areCellsInSameBox(xyChain.getLast(), possibleLink))) {
                possibleLinks.add(possibleLink);
            }
        }

        if(possibleLinks.size() == 0){
             return xyChains;
        }

        for (int[] link : possibleLinks) {
            LinkedList<int[]> chain = new LinkedList<>(xyChain);
            chain.add(link);
            tempChains.add(chain);
        }
        for (LinkedList<int[]> chain: tempChains) {
            int[] cell= chain.getLast();
            if(sudoku[cell[0]][cell[1]].getPossibilities().get(0) == possible){
                if(sudoku[cell[0]][cell[1]].getPossibilities().get(1) == startPossible){
                    xyChains.add(chain);
                }
                    xyChains.addAll(xyBuildChain(chain, xyChainCandidates, startPossible, sudoku[cell[0]][cell[1]].getPossibilities().get(1)));

            }else{
                if(sudoku[cell[0]][cell[1]].getPossibilities().get(0) == startPossible){
                    xyChains.add(chain);
                }
                    xyChains.addAll(xyBuildChain(chain, xyChainCandidates, startPossible, sudoku[cell[0]][cell[1]].getPossibilities().get(0)));
            }
        }

        return xyChains;
    }

  /*  private boolean aicChainStrategy(){
        boolean flag = false;
        ArrayList<int[]> aicPossibleCells = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().size() == 2) {
                    aicPossibleCells.add(new int[]{row, column});
                }
            }
        }

        for (int possible = 1; possible <= 9; possible++) {
            ArrayList<int[]> temp = getCellsWithStrongLinks(possible);
            for (int[] cell: temp) {
                boolean alreadyAdded = false;
                for (int[] aicPossible: aicPossibleCells) {
                    if(cell[0] == aicPossible[0] && cell[1] == aicPossible[1]){
                        alreadyAdded = true;
                        break;
                    }
                }
                if(!alreadyAdded){
                    aicPossibleCells.add(cell);
                }
            }
        }

        for (int[] aicPossible: aicPossibleCells) {
            for (int possible: sudoku[aicPossible[0]][aicPossible[1]].getPossibilities()) {
                aicChainBuilder(aicPossible, aicPossibleCells, possible);
            }
        }

        return flag;
    }

    private ArrayList<LinkedList<int[]>> aicChainBuilder (int[] cell, ArrayList<int[]> aicChainCandidates, int startPossible){
        ArrayList<LinkedList<int[]>> aicChains = new ArrayList<>();
        LinkedList<int[]> aicChain = new LinkedList<>();
        aicChain.add(cell);

        aicChains.addAll(aicBuildChain(aicChain, aicChainCandidates, startPossible));

        return aicChains;

    }

    private ArrayList<LinkedList<int[]>> aicBuildChain(LinkedList<int[]> aicChain, ArrayList<int[]> aicChainCandidates, int startPossible){
        ArrayList<LinkedList<int[]>> aicChains = new ArrayList<>();
        int[] possibles = sudoku[aicChain.getLast()[0]][aicChain.getLast()[1]].getPossibilities().stream().mapToInt(i -> i).toArray();
        int numPossibles = sudoku[aicChain.getLast()[0]][aicChain.getLast()[1]].getPossibilities().size();

    *//*    if (numPossibles == 2) {
            if (startPossible == possibles[0]) {
                possible = possibles[1];
            } else {
                possible = possibles[0];
            }
        } else {
            possible = startPossible;
        }*//*
        return aicChains;
    }

    private ArrayList<int[]> aicStrongLink(){
        ArrayList<int[]> strongLinks = new ArrayList<>();

        return strongLinks;
    }
    private ArrayList<int[]>  weakLink(){
        ArrayList<int[]> weakLinks = new ArrayList<>();

        return weakLink();
    }*/



    private boolean guess(){

       int[][] currentBoard = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                currentBoard[row][column] = sudoku[row][column].getSolution();
            }
        }
        int[] cell = new int[2];
        boolean cellFound = false;
        for (int i = 2; i < 9; i++) {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    if (sudoku[row][column].getSolution() == 0 && sudoku[row][column].getPossibilities().size() == i) {
                        cell[0] = row;
                        cell[1] = column;
                        cellFound = true;
                        break;
                    }

                }
                if (cellFound) {
                    break;
                }
            }
            if (cellFound) {
                break;
            }
        }
        ArrayList<Integer> possibles = new ArrayList<>(sudoku[cell[0]][cell[1]].getPossibilities());

        for (Integer possible : possibles) {
            updateCellSolution(cell[0], cell[1], possible);
            if (!solve()) {
                Sudoku currentState = new Sudoku(currentBoard);
                sudoku = currentState.sudoku;
                invalidCell = currentState.invalidCell;
            } else {
                return true;
            }
        }
        return !isUnsolved();
    }

        public void display() {
        // We have got the solution, just display it
        for (int i = 0; i < 9; i++) {
            for (int d = 0; d < 9; d++) {
                System.out.print(sudoku[i][d].getSolution() + " ");
            }
            System.out.print("\n");
        }
    }

    @Override
    public String toString(){

        String sudokuString = "";

        for (int i = 0; i < 9; i++) {
            for (int d = 0; d < 9; d++) {
                //noinspection StringConcatenationInLoop
                sudokuString += Integer.toString(sudoku[i][d].getSolution());
            }
        }

        return  sudokuString;
    }

    public boolean containsInvalidCell() {
        return invalidCell;
    }
}
