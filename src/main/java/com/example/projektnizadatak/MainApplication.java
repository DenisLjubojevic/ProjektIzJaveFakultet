package com.example.projektnizadatak;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        /*
        * Timeline simulacijaJedenja = new Timeline(
                new KeyFrame(Duration.seconds(60), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new simulacijaJedenjaNit());
                    }
                }));
        simulacijaJedenja.setCycleCount(3);
        simulacijaJedenja.play();

        Timeline nahrani = new Timeline(
                new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new nahraniNit());
                    }
                }));
        nahrani.setCycleCount(Timeline.INDEFINITE);
        nahrani.play();*/

        stage.setWidth(900);
        stage.setHeight(500);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        mainStage = stage;

        IzbornikController.promjeniEkran(
                "login/loginScreen.fxml",
                "Zoološki vrt");
    }

    public static void popraviLayout(){
        double visina = getMainStage().getHeight();
        getMainStage().setHeight(visina - 1);
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage(){
        return mainStage;
    }
}