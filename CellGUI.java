package gui.sudokuteacher;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;


public class CellGUI extends StackPane  {
    private GridPane cellPossibilityGridPane;
    private Text solution;
    private boolean isSolutionHint;
    private boolean selected;
    private boolean editPencilMarks;
    int row;
    int column;


    public CellGUI() {
        super();
        createPencilMarks();
        this.getChildren().add(cellPossibilityGridPane);

    }

    public CellGUI(char solution, int row, int column) {
        super();
        selected = false;
        this.row = row;
        this.column = column;
        String solutionChar = String.valueOf(solution);
        if(Character.isDigit(solution) && Integer.parseInt(solutionChar) > 0){
            isSolutionHint = true;
            this.solution = new Text(solutionChar);
            super.getChildren().addAll(this.solution);
        }else{
            this.solution = new Text("");
            createPencilMarks();
            super.getChildren().addAll(this.solution, cellPossibilityGridPane);
        }

        this.solution.setFont(Font.font("Comic Sans MS", FontWeight.BLACK, 35));
    }

    public boolean isSelected(){
        return selected;
    }

    public int getSolution(){
        if(this.solution.getText().equals("")){
            return 0;
        }else{
            return  Integer.parseInt(this.solution.getText());
        }
    }


    public void setSelected(boolean b){
        if(b){
            super.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            super.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public boolean handleNumberInput(int number){
        boolean numberAdded = false;
        if(!isSolutionHint) {
            if (editPencilMarks) {
                if (solution.getText().equals("") && number > 0) {
                    updatePencilMarks(number);
                }
            } else {
                updateSolution(number);
                numberAdded = true;
            }
        }
        return numberAdded;
    }

    public void updateEditPencilMarks(){
        editPencilMarks = !editPencilMarks;
    }

    private void updateSolution(int solution){
        if(!editPencilMarks && solution > 0) {
            this.solution.setText(Integer.toString(solution));
            this.solution.setFill(Color.DARKBLUE);
            for (Node node: cellPossibilityGridPane.getChildren()) {
                ((Text) node).setVisible(false);
            }
        }else if(solution == 0){
            this.solution.setText("");

        }
    }

    private void updatePencilMarks(int possible){

        if(cellPossibilityGridPane.getChildren().get(possible - 1).isVisible()){
            cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(false);
        }else{
            cellPossibilityGridPane.getChildren().get(possible - 1).setVisible(true);
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
                Text text = new Text(Integer.toString(possible));
                text.setVisible(false);
                cellPossibilityGridPane.add(text, column, row);
                GridPane.setHalignment(text, HPos.CENTER);
                possible++;
            }
        }

        cellPossibilityGridPane.alignmentProperty().set(Pos.CENTER);

    }

    public void highlightRowAndColumn() {
        // Highlight row
        for (Node node : getParent().getChildrenUnmodifiable()) {
            if (node instanceof CellGUI) {
                CellGUI cell = (CellGUI) node;
                if (cell.row == this.row) {
                    if (cell.column == this.column) {
                        cell.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE.darker(), CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        cell.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
        }

        // Highlight column
        for (Node node : getParent().getChildrenUnmodifiable()) {
            if (node instanceof CellGUI) {
                CellGUI cell = (CellGUI) node;
                if (cell.column == this.column) {
                    if (cell.row != this.row) {
                        cell.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
        }
    }

    public void unhighlightRowAndColumn() {
        // Check if the parent is not null
        if (getParent() != null) {
            // Unhighlight row
            for (Node node : getParent().getChildrenUnmodifiable()) {
                if (node instanceof CellGUI) {
                    CellGUI cell = (CellGUI) node;
                    if (cell.row == this.row) {
                        cell.setBackground(Background.EMPTY);
                    }
                }
            }

            // Unhighlight column
            for (Node node : getParent().getChildrenUnmodifiable()) {
                if (node instanceof CellGUI) {
                    CellGUI cell = (CellGUI) node;
                    if (cell.column == this.column) {
                        cell.setBackground(Background.EMPTY);
                    }
                }
            }
        }
    }

}