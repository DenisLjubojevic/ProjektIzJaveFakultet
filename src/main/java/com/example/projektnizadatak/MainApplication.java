package com.example.projektnizadatak;

import com.example.projektnizadatak.Util.nahraniNit;
import com.example.projektnizadatak.Util.simulacijaJedenjaNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainApplication extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        /*Timeline simulacijaJedenja = new Timeline(
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

        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Zoološki vrt");
        scene.getStylesheets().add(MainApplication.class.getResource("/com/example/projektnizadatak/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage(){
        return mainStage;
    }
}