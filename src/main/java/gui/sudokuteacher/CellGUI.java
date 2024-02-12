package gui.sudokuteacher;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;



public class CellGUI extends Pane  {
    private GridPane cellPossibilityGridPane;
    private boolean selected;
    int row;
    int column;


    public CellGUI() {
        super();

        createPencilMarks();
        this.getChildren().add(cellPossibilityGridPane);
        super.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("(Row: " + row + " Column: " + column + ") clicked");
        });

    }
    public CellGUI(int row, int column) {
        super();
        selected = false;
        this.row = row;
        this.column = column;

        createPencilMarks();
        super.getChildren().add(cellPossibilityGridPane);

        super.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("(Row: " + row + " Column: " + column + ") clicked");
        });

    }

    public CellGUI(String solution, int row, int column) {
        super();
        super.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }

    public boolean isSelected(){
        return selected;
    }


    public void setSelected(boolean b){
        if(b){
            super.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            super.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                    CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void createPencilMarks() {
        cellPossibilityGridPane = new GridPane();
        for (int i = 0; i < 3; i++) {
            cellPossibilityGridPane.getColumnConstraints().add(new ColumnConstraints(15));
            cellPossibilityGridPane.getRowConstraints().add(new RowConstraints(15));
        }

        Text text1 = new Text("1");
        Text text2 = new Text("2");
        Text text3 = new Text("3");
        Text text4 = new Text("4");
        Text text5 = new Text("5");
        Text text6 = new Text("6");
        Text text7 = new Text("7");
        Text text8 = new Text("8");
        Text text9 = new Text("9");
        cellPossibilityGridPane.add(text1, 0, 0);
        cellPossibilityGridPane.add(text2, 1, 0);
        cellPossibilityGridPane.add(text3, 2, 0);
        cellPossibilityGridPane.add(text4, 0, 1);
        cellPossibilityGridPane.add(text5, 1, 1);
        cellPossibilityGridPane.add(text6, 2, 1);
        cellPossibilityGridPane.add(text7, 0, 2);
        cellPossibilityGridPane.add(text8, 1, 2);
        cellPossibilityGridPane.add(text9, 2, 2);
        GridPane.setHalignment(text1, HPos.CENTER);
        GridPane.setHalignment(text2, HPos.CENTER);
        GridPane.setHalignment(text3, HPos.CENTER);
        GridPane.setHalignment(text4, HPos.CENTER);
        GridPane.setHalignment(text5, HPos.CENTER);
        GridPane.setHalignment(text6, HPos.CENTER);
        GridPane.setHalignment(text7, HPos.CENTER);
        GridPane.setHalignment(text8, HPos.CENTER);
        GridPane.setHalignment(text9, HPos.CENTER);

        cellPossibilityGridPane.alignmentProperty().set(Pos.CENTER);

    }
}