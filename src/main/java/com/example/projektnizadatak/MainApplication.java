package com.example.projektnizadatak;

import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
        stage.setMinWidth(700);
        stage.setMinHeight(500);
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

    public static void showAlertDialog(Alert.AlertType alertType, String title, String header, String context){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("/com/example/projektnizadatak/css/AlertDialog.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/logo.png"))));

        alert.showAndWait();
    }

    public static Optional<ButtonType> showAlertDialogConfirmation(Alert.AlertType alertType, String title, String header, String context){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("/com/example/projektnizadatak/css/AlertDialog.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/logo.png"))));

        return alert.showAndWait();
    }

    public static Image byteArrayToImage(byte[] byteArray){
        if (byteArray != null && byteArray.length > 0){
            return new Image(new ByteArrayInputStream(byteArray));
        }
        return null;
    }

    public static  byte[] imageToByteArray(Image image){
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try{
            ImageIO.write(bufferedImage, "png", s);
            return s.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}