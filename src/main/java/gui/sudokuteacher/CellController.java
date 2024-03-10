package gui.sudokuteacher;

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

        cellView = new CellView(cell, this);

    }

    public void setCellBoarder(Border boarder){
        cellView.setBorder(boarder);
    }
    public int getCellValue(){return this.cellValue;}

    public CellView getCellView() {return cellView;}
    public Cell getCellModel() {return cellModel;}

    public boolean isSolutionHint() {return isSolutionHint;}

    public void setSelected(boolean b){
        if(b){
            cellView.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            cellView.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public void updateCellSolution(int solution){
        if(!isSolutionHint ) {
            this.cellValue = solution;
            if (cellValue == 0) {
                cellView.updateSolution(0);
                cellView.getCellPossibilityGridPane().setVisible(true);
            }else{
                cellView.updateSolution(cellValue);
                cellView.getCellPossibilityGridPane().setVisible(false);
            }
            //updateCellPossibilities();
        }
    }

    public void updateCellPossibilities(){
        cellView.clearPencilMarks();
        for (Integer possible : cellModel.getPossibilities()) {
            cellView.updatePencilMarks(possible);
        }
    }

    public void enablePencilMarks(){
        cellView.enableEditPencilMarks();
    }
    public void disablePencilMarks(){cellView.disableEditPencilMarks();}



}
