package com.example.projektnizadatak;

import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.StanistaController.RasporedHranjenjaController;
import com.example.projektnizadatak.Niti.MOTDThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import javafx.embed.swing.SwingFXUtils;
import org.h2.tools.Server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class MainApplication extends Application {
    public static Stage mainStage;
    private static Server server;

    private static final Thread motdThread = new Thread(new MOTDThread());

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        server = Server.createTcpServer("-tcpAllowOthers").start();

        motdThread.start();

        stage.setWidth(900);
        stage.setHeight(500);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        mainStage = stage;

        IzbornikController.promjeniEkran(
                "login/loginScreen.fxml",
                "Zoološki vrt");

        mainStage.setOnCloseRequest(event -> {
            RasporedHranjenjaController.scheduler.shutdownNow();
            if (motdThread != null && motdThread.isAlive()) {
                motdThread.interrupt();
                try {
                    motdThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public static void setupButton(Button button){
        button.styleProperty().bind(
                Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(8.5).asString(), "px; ",
                        "-fx-pref-height: ", MainApplication.getMainStage().heightProperty().divide(13).asString(), "px; ",
                        "-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(50).asString(), "px"));
    }

    public static void setupNaslov(Label label){
        label.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(16).asString(), "px"));
    }

    public static void setupText(Label label){
        label.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(42).asString(), "px"));
    }

    public static void setupRadioButton(RadioButton radioButton){
        radioButton.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(42).asString(), "px"));
    }

    @Override
    public void stop() {
        if (server != null && server.isRunning(true)) {
            server.stop();
        }
    }
}