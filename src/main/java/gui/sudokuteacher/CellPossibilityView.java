package gui.sudokuteacher;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CellPossibilityView extends StackPane {

    Text text;
    Rectangle rectangle;

    public CellPossibilityView(int possible) {
        super();
        text = new Text(Integer.toString(possible));
        rectangle = new Rectangle(15,15);
        rectangle.setFill(Color.TRANSPARENT);
        text.setVisible(false);
        super.getChildren().addAll(rectangle, text);
    }

    public void highlightPossible(Color color){
        this.rectangle.setFill(color);
    }
    public void undoHighlight(){
        rectangle.setFill(Color.TRANSPARENT);
    }
    public void hidePencilMark(){
        text.setVisible(false);
        undoHighlight();

    }

    public void showPencilMark(){
        text.setVisible(true);
    }

    public boolean isShown(){
        return this.text.isVisible();
    }

}
