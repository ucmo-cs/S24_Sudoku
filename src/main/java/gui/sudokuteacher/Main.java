package gui.sudokuteacher;

import gui.sudokuteacher.scenes.MainMenuScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

//For some reason this class needs to be here for the build to work
//TODO: figure out why this needs to be here for the build
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene mainMenuScene = MainMenuScene.createMainMenu(stage);

        stage.setScene(mainMenuScene);
        stage.setTitle("Sudoku Teacher");
        stage.show();
    }
}
