package gui.sudokuteacher.controllers;

import gui.sudokuteacher.views.CellView;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import solver.sudokuteacher.SudokuCompenents.Cell;

public class CellController {

    Cell cellModel;
    CellView cellView;
    int cellValue;
    int cellRow;
    int cellColumn;
    boolean editPencilMarks;
    boolean isSolutionHint;

    public CellController(){}

    public CellController(Cell cell) {
        this.cellValue = cell.getSolution();
        this.cellRow = cell.getRow();
        this.cellColumn = cell.getColumn();
        cellModel = cell;
        cellModel.setCellController(this);
        if(cellValue == 0){
            isSolutionHint = false;
        }else{
            isSolutionHint = true;
            }
        cellView = new CellView(cell.getSolution(), this);
        updateCellPossibilities();
    }

    public void setCellBoarder(Border boarder){
        cellView.setBorder(boarder);
    }
    public int getCellValue(){return this.cellValue;}
    public boolean isEditPencilMarks() {return editPencilMarks;}
    public void enableEditPencilMarks(){
        editPencilMarks = true;
    }
    public void disableEditPencilMarks(){
        editPencilMarks = false;
    }
    public CellView getCellView() {return cellView;}
    public Cell getCellModel() {return cellModel;}
    public int getCellRow() {return cellRow;}
    public int getCellColumn() {return cellColumn;}
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
    public boolean isSolutionHint() {return isSolutionHint;}
    public void updatePencilMarks(int possible){
        String result = cellView.updatePencilMarks(possible);
        if(result.equals("remove")){
         cellModel.getPossibilities().remove(Integer.valueOf(possible));
        }else if(result.equals("add")){
            cellModel.getPossibilities().add(possible);
        }

    }
    public void setSelected(boolean b){
        if(b){
            cellView.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            cellView.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public void highlightPossible(int possible, Color color){
        cellView.highlightPossible(possible, color);
    }
    public void unhighlightPossible(int possible){
        cellView.unhighlightPossible(possible);
    }

    public void removePencilMark(int possible){
        if(!isSolutionHint) {
            cellView.hidePencilMark(possible);
            cellModel.getPossibilities().remove(Integer.valueOf(possible));
        }
    }
    public void addPencilMark(int possible){
        if(!isSolutionHint) {
            cellView.showPencilMark(possible);
            cellModel.getPossibilities().add(possible);
        }
    }
    public void updateCellSolution(int solution){
        if(!isSolutionHint ) {
            this.cellValue = solution;
            if (cellValue == 0) {
                cellView.updateSolution(0);
/*                cellModel.setSolution(solution);
                cellModel.getPossibilities().clear();*/
              // cellView.getCellPossibilityGridPane().setVisible(true);
            }else{
                cellView.updateSolution(cellValue);
                cellView.clearPencilMarks();

/*                cellModel.setSolution(solution);
                cellModel.getPossibilities().clear();*/
            }
            //updateCellPossibilities();
        }
    }

    public void updateCellPossibilities(){
        cellView.clearPencilMarks();
        for (Integer possible : cellModel.getPossibilities()) {
            cellView.showPencilMark(possible);
        }
    }

}
