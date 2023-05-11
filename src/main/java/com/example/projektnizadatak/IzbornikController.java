package com.example.projektnizadatak;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

        pawsImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\paws.png"));
        employeeImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\employee.png"));
        habitatImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\habitat.png"));
        notebookImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\notebook.png"));
        changeImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\changes.png"));
        pawsImage1.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\paws.png"));
        employeeImage1.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\employee.png"));
        habitatImage1.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\habitat.png"));
        notebookImage1.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\notebook.png"));
        signOutImage.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\signOut.png"));
    }

    public void promjeniEkran(String naziv, String naslov) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(naziv));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        MainApplication.getMainStage().setTitle(naslov);
        MainApplication.getMainStage().getScene().getStylesheets().add(MainApplication.class.getResource("/com/example/projektnizadatak/style.css").toExternalForm());
        MainApplication.getMainStage().setScene(scene);
        MainApplication.getMainStage().show();
    }

    public void logOut() throws IOException{
        promjeniEkran("loginScreen.fxml", "Zoološki vrt");
    }

    public void showZivotinjeSearchScreen() throws IOException {
        promjeniEkran("pretragaZivotinja.fxml", "Pretraga životinja");
    }

    public void showZaposleniciSearchScreen() throws IOException {
        promjeniEkran("pretragaZaposlenika.fxml", "Pretraga zaposlenika");
    }

    public void showStanistaSearchScreen() throws IOException {
        promjeniEkran("pretragaStanista.fxml", "Pretraga staništa");
    }

    public void showAktivnostiSearchScreen() throws IOException {
        promjeniEkran("pretragaAktivnosti.fxml", "Pretraga aktivnosti");
    }

    public void showZivotinjaUnosScreen() throws IOException {
        promjeniEkran("unosZivotinja.fxml", "Unos životinja");
    }

    public void showZaposlenikUnosScreen() throws IOException {
        promjeniEkran("unosZaposlenika.fxml", "Unos zaposlenika");
    }

    public void showStanisteUnosScreen() throws IOException {
        promjeniEkran("unosStanista.fxml", "Unos staništa");
    }

    public void showAktivnostUnosScreen() throws IOException {
        promjeniEkran("unosAktivnosti.fxml", "Unos aktivnosti");
    }

    public void showZivotinjaIzmjenaScreen() throws IOException {
        promjeniEkran("azurirajZivotinju.fxml", "Izmjena životinje");
    }

    public void showZaposlenikIzmjenaScreen() throws IOException {
        promjeniEkran("azurirajZaposlenika.fxml", "Izmjena zaposlenika");
    }

    public void showStanisteIzmjenaScreen() throws IOException {
        promjeniEkran("azurirajStaniste.fxml", "Izmjena staništa");
    }

    public void showAktivnostIzmjenaScreen() throws IOException {
        promjeniEkran("azurirajAktivnosti.fxml", "Izmjena aktivnosti");
    }


    public void showBrisanjeZivotinjaScreen()throws IOException{
        promjeniEkran("brisanjeZivotinja.fxml", "Brisanje životinja");
    }

    public void showBrisanjeZaposlenikaScreen()throws IOException{
        promjeniEkran("brisanjeZaposlenika.fxml", "Brisanje zaposlenika");
    }

    public void showBrisanjeStanistaScreen()throws IOException{
        promjeniEkran("brisanjeStanista.fxml", "Brisanje stanista");
    }

    public void showBrisanjeAktivnostiScreen()throws IOException{
        promjeniEkran("brisanjeAktivnosti.fxml", "Brisanje aktivnosti");
    }

    public void showPromjeneScreen() throws IOException {
        promjeniEkran("promjene.fxml", "Promjene");
    }
}
