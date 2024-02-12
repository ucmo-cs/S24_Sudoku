package gui.sudokuteacher;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class SudokuBoard extends GridPane {
    CellGUI currentCell;

    public SudokuBoard() {
        super();
        currentCell = new CellGUI();
        super.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4, 4, 4, 4), null)));
        buildBoard();

        super.setOnMouseClicked((MouseEvent e) -> {
            CellGUI cell = new CellGUI();
            Node clickedNode = e.getPickResult().getIntersectedNode();
            if (clickedNode != this) {
                // click on descendant node
                Node parent = clickedNode.getParent();
                while (parent != this) {
                    clickedNode = parent;
                    parent = clickedNode.getParent();
                }
                Integer colIndex = GridPane.getColumnIndex(clickedNode);
                Integer rowIndex = GridPane.getRowIndex(clickedNode);
                System.out.println("Mouse clicked cell: " + rowIndex + " And: " + colIndex);
            }

            cell = (CellGUI) clickedNode;

            currentCell.setSelected(false);
            currentCell = cell;
            currentCell.setSelected(true);


        });

    }

    private void buildBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI(i, j);
                cell.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

                //following code is used to help visual the sudoku board better by making each quadrant lines thicker than each individual square
                //sets style for top row of sudoku
                if ((i == 0 )) {
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 1, 1), null)));
                    }
                }

                //sets style for row 2 & 5
                else if (i == 2 || i == 5){

                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 2, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 2, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 2, 1), null)));
                    }
                }

                //sets style for rows 3 & 6
                else if (i == 3 || i == 6) {

                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 0), null)));
                    } else if (j == 8) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 0, 1, 1), null)));

                    } else if (j == 2 || j == 5) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 2, 1, 1), null)));

                    } else if (j == 3 || j == 6) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 2), null)));

                    } else{
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2, 1, 1, 1), null)));
                    }
                }

                //sets style for rows 1, 4, 5
                else if (i == 1 || i == 4 || i == 7){
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 1, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 1, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1), null)));
                    }
                }
                //sets style for bottom row of board
                else  {
                    if (j == 0) {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 0), null)));
                    }else if(j == 8){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 1), null)));

                    }else if(j == 2 || j == 5){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 2, 0, 1), null)));

                    } else if(j == 3 || j == 6){
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 2), null)));

                    }else {
                        cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 0, 1), null)));
                    }
                }
                super.add(cell, j, i );
            }

        }
    }

}
