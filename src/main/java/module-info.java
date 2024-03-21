module solver.sudokuteacher {
    requires javafx.controls;
    requires javafx.fxml;


    opens solver.sudokuteacher to javafx.fxml;
    exports solver.sudokuteacher;
    exports gui.sudokuteacher;
    opens gui.sudokuteacher to javafx.fxml;
    exports gui.sudokuteacher.controllers;
    opens gui.sudokuteacher.controllers to javafx.fxml;
    exports gui.sudokuteacher.views;
    opens gui.sudokuteacher.views to javafx.fxml;
    exports gui.sudokuteacher.scenes;
    opens gui.sudokuteacher.scenes to javafx.fxml;
}