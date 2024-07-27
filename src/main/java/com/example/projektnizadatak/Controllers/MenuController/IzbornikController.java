package com.example.projektnizadatak.Controllers.MenuController;

import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.projektnizadatak.MainApplication.mainStage;

public class IzbornikController {
    @FXML
    private ImageView signOutImage;

    @FXML
    private ImageView pawsImage;

    @FXML
    private ImageView employeeImage;

    @FXML
    private ImageView habitatImage;

    @FXML
    private ImageView notebookImage;

    @FXML
    private ImageView changeImage;

    @FXML
    private MenuBar izbornikAdmin;

    public void initialize(){
        izbornikAdmin.prefWidthProperty().bind(mainStage.widthProperty());

        pawsImage.setImage(loadImage("/com/example/projektniZadatak/Images/paws.png"));
        employeeImage.setImage(loadImage("/com/example/projektniZadatak/Images/employee.png"));
        habitatImage.setImage(loadImage("/com/example/projektniZadatak/Images/habitat.png"));
        notebookImage.setImage(loadImage("/com/example/projektniZadatak/Images/notebook.png"));
        changeImage.setImage(loadImage("/com/example/projektniZadatak/Images/changes.png"));
        signOutImage.setImage(loadImage("/com/example/projektniZadatak/Images/changes.png"));
    }

    private Image loadImage(String relativnaPutanja){
        return new Image(getClass().getResource(relativnaPutanja).toExternalForm());
    }

    public static void promjeniEkran(String naziv, String naslov) throws IOException {
        Stage stage = MainApplication.getMainStage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(naziv));
        double sirina = MainApplication.getMainStage().getWidth();
        double visina = MainApplication.getMainStage().getHeight();
        Scene scene = new Scene(fxmlLoader.load(), sirina, visina);
        stage.setTitle(naslov);
        stage.setScene(scene);

        try {
            Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/logo.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scene.getStylesheets().add(MainApplication.class.getResource("/com/example/projektnizadatak/css/style.css").toExternalForm());

        Platform.runLater(() -> {
            scene.getRoot().layout();
            adjustLayout(scene);
        });

        stage.show();

        stage.widthProperty().addListener((obs, oldVal, newVal) -> adjustLayout(scene));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> adjustLayout(scene));
    }

    private static void adjustLayout(Scene scene) {
        Platform.runLater(scene.getRoot()::requestLayout);
    }

    public void logOut() throws IOException{
        promjeniEkran("login/loginScreen.fxml", "Zoološki vrt");
    }

    public void showZivotinjeSearchScreen() throws IOException {
        promjeniEkran("zivotinje/pretragaZivotinja.fxml", "Pretraga životinja");
    }

    public void showZaposleniciSearchScreen() throws IOException {
        promjeniEkran("zaposlenici/pretragaZaposlenika.fxml", "Pretraga zaposlenika");
    }

    public void showStanistaSearchScreen() throws IOException {
        promjeniEkran("stanista/pretragaStanista.fxml", "Pretraga staništa");
    }

    public void showAktivnostiSearchScreen() throws IOException {
        promjeniEkran("aktivnosti/pretragaAktivnosti.fxml", "Pretraga aktivnosti");
    }

    public void showInteraktivnaMapaScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/karta/karta.fxml"));
        Parent root = loader.load();

        Stage stage = MainApplication.getMainStage();
        stage.setScene(new Scene(root));
        stage.setTitle("KARTA");
        stage.show();
    }

    public void showPromjeneScreen() throws IOException {
        promjeniEkran("promjene/promjene.fxml", "Promjene");
    }
}
