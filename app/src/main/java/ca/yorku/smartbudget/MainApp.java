package ca.yorku.smartbudget;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Label("SmartBudget - Phase 0 OK"), 400, 200);
        stage.setTitle("SmartBudget");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
