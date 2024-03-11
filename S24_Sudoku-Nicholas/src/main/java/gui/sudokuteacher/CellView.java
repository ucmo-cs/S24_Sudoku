package gui.sudokuteacher;

import javafx.geometry.HPos;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import solver.sudokuteacher.SudokuCompenents.Cell;

public class CellView extends StackPane  {

    private GridPane cellPossibilityGridPane;
    private Text solution;
    private boolean isSolutionHint;
    private CellController cellController;


    //TODO: have font/color and other cosmetic options to the controller
    public CellView(Cell cell, CellController cellController) {
        super();
        this.cellController = cellController;
        int solution = cell.getSolution();
        if(solution > 0){
            isSolutionHint = true;
            this.solution = new Text(Integer.toString(solution));
            super.getChildren().addAll(this.solution);
        }else{
            this.solution = new Text("");
            this.solution.setFill(Color.DARKBLUE);
            createPencilMarks(cell);
            super.getChildren().addAll(this.solution, cellPossibilityGridPane);
        }

        this.solution.setFont(Font.font("Comic Sans MS", FontWeight.BLACK, 35));
    }

    public Text getSolution(){return this.solution;}

    public CellController getCellController() {
        return cellController;
    }
    public GridPane getCellPossibilityGridPane() {
        return cellPossibilityGridPane;
    }

    public void updateSolution(int solution){
        if(solution > 0){
            this.solution.setText(Integer.toString(solution));
        }else{
            this.solution.setText("");
        }
    }

    public void clearPencilMarks(){
        if(!isSolutionHint) {
            for (Node possible : cellPossibilityGridPane.getChildren()) {
                possible.setVisible(false);
            }
        }
    }

    public void updatePencilMarks(int possible){
        if(!isSolutionHint) {
            if(cellPossibilityGridPane.getChildren().get(possible -1).isVisible()){
                cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(false);
            }else{
                cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(true);
            }

        }
    }

    public void hidePencilMark(int possible){
        if(!isSolutionHint) {
            cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(false);
        }
        }
    public void showPencilMark(int possible){
        if(!isSolutionHint) {
            cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(true);
        }
        }

    private void createPencilMarks(Cell cell) {
        cellPossibilityGridPane = new GridPane();
        for (int i = 0; i < 3; i++) {
            cellPossibilityGridPane.getColumnConstraints().add(new ColumnConstraints(15));
            cellPossibilityGridPane.getRowConstraints().add(new RowConstraints(15));
        }

        int possible = 1;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                Text text = new Text(Integer.toString(possible));
                text.setVisible(false);
                cellPossibilityGridPane.add(text, column, row);
                GridPane.setHalignment(text, HPos.CENTER);
                possible++;
            }
        }
        for (Integer cellPossible: cell.getPossibilities()) {
            cellPossibilityGridPane.getChildren().get(cellPossible - 1).setVisible(true);
        }

        cellPossibilityGridPane.alignmentProperty().set(Pos.CENTER);

    }
}