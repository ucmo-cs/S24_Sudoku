module solver.sudokuteacher {
    requires javafx.controls;
    requires javafx.fxml;


    opens solver.sudokuteacher to javafx.fxml;
    exports solver.sudokuteacher;
    exports gui.sudokuteacher;
    opens gui.sudokuteacher to javafx.fxml;
}