package gui.sudokuteacher.views;

import gui.sudokuteacher.controllers.CellController;
import javafx.geometry.HPos;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CellView extends StackPane  {

    private GridPane cellPossibilityGridPane;
    private Text solution;
    private boolean isSolutionHint;
    private CellController cellController;


    //TODO: have font/color and other cosmetic options to the controller
    public CellView(int solution, CellController cellController) {
        super();
        this.cellController = cellController;
        if(solution > 0){
            isSolutionHint = true;
            this.solution = new Text(Integer.toString(solution));
            super.getChildren().addAll(this.solution);
        }else{
            this.solution = new Text("");
            this.solution.setFill(Color.DARKBLUE);
            createPencilMarks();
            super.getChildren().addAll(this.solution, cellPossibilityGridPane);
        }

        this.solution.setFont(Font.font("Comic Sans MS", FontWeight.BLACK, 35));
        this.setMaxSize(50,50);
        this.setMinSize(50,50);
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
                ((CellPossibilityView) possible).hidePencilMark();
            }
        }
    }

    public String updatePencilMarks(int possible){
        if(!isSolutionHint) {
            CellPossibilityView currentPossible = ((CellPossibilityView) cellPossibilityGridPane.getChildren().get(possible - 1));
            if(currentPossible.isShown()){
                currentPossible.hidePencilMark();
                return "remove";
            }else{
                currentPossible.showPencilMark();
                return "add";
            }

        }
        return "";
    }

    public void highlightPossible(int possible, Color color){
        CellPossibilityView currentPossible = (CellPossibilityView) cellPossibilityGridPane.getChildren().get(possible - 1);
        currentPossible.highlightPossible(color);
    }
    public void unhighlightPossible(int possible){
        CellPossibilityView currentPossible = (CellPossibilityView) cellPossibilityGridPane.getChildren().get(possible - 1);
        currentPossible.undoHighlight();
    }

    public void hidePencilMark(int possible){
        if(!isSolutionHint) {
            CellPossibilityView currentPossible = (CellPossibilityView) cellPossibilityGridPane.getChildren().get(possible - 1);
            currentPossible.hidePencilMark();
        }
        }
    public void showPencilMark(int possible){
        if(!isSolutionHint) {
         CellPossibilityView currentPossible = (CellPossibilityView) cellPossibilityGridPane.getChildren().get(possible - 1);
         currentPossible.showPencilMark();
        }
        }

    private void createPencilMarks() {
        cellPossibilityGridPane = new GridPane();
        for (int i = 0; i < 3; i++) {
            cellPossibilityGridPane.getColumnConstraints().add(new ColumnConstraints(15));
            cellPossibilityGridPane.getRowConstraints().add(new RowConstraints(15));
        }

        int possible = 1;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                CellPossibilityView cellPossible = new CellPossibilityView(possible);
                cellPossibilityGridPane.add(cellPossible, column, row);
                GridPane.setHalignment(cellPossible, HPos.CENTER);
                possible++;
            }
        }

        cellPossibilityGridPane.alignmentProperty().set(Pos.CENTER);

    }
}