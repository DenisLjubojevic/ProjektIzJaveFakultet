package com.example.projektnizadatak.Controllers.MenuController;

import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

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
    private ImageView pawsImage1;

    @FXML
    private ImageView employeeImage1;

    @FXML
    private ImageView habitatImage1;

    @FXML
    private ImageView notebookImage1;

    @FXML
    private MenuBar izbornik;
    @FXML
    private MenuBar izbornikAdmin;

    public void initialize(){
        if(loginScreenController.roleKorisnika.equals("admin")){
            izbornikAdmin.setVisible(true);
            izbornik.setVisible(false);
        }else{
            izbornikAdmin.setVisible(false);
            izbornik.setVisible(true);
        }

        izbornik.prefWidthProperty().bind(mainStage.widthProperty());
        izbornikAdmin.prefWidthProperty().bind(mainStage.widthProperty());

        pawsImage.setImage(loadImage("/com/example/projektniZadatak/Images/paws.png"));
        employeeImage.setImage(loadImage("/com/example/projektniZadatak/Images/employee.png"));
        habitatImage.setImage(loadImage("/com/example/projektniZadatak/Images/habitat.png"));
        notebookImage.setImage(loadImage("/com/example/projektniZadatak/Images/notebook.png"));
        changeImage.setImage(loadImage("/com/example/projektniZadatak/Images/changes.png"));
        pawsImage1.setImage(loadImage("/com/example/projektniZadatak/Images/paws.png"));
        employeeImage1.setImage(loadImage("/com/example/projektniZadatak/Images/employee.png"));
        habitatImage1.setImage(loadImage("/com/example/projektniZadatak/Images/habitat.png"));
        notebookImage1.setImage(loadImage("/com/example/projektniZadatak/Images/notebook.png"));
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

    public void showAktivnostUnosScreen() throws IOException {
        promjeniEkran("aktivnosti/unosAktivnosti.fxml", "Unos aktivnosti");
    }

    public void showAktivnostIzmjenaScreen() throws IOException {
        promjeniEkran("aktivnosti/azurirajAktivnosti.fxml", "Izmjena aktivnosti");
    }

    public void showBrisanjeAktivnostiScreen()throws IOException{
        promjeniEkran("aktivnosti/brisanjeAktivnosti.fxml", "Brisanje aktivnosti");
    }

    public void showPromjeneScreen() throws IOException {
        promjeniEkran("promjene/promjene.fxml", "Promjene");
    }
}
