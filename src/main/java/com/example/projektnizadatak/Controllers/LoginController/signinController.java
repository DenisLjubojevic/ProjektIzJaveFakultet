package com.example.projektnizadatak.Controllers.LoginController;

import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Korisnici.Korisnik;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class signinController {
    @FXML
    private TextField korisnickoImeTextField;
    @FXML
    private PasswordField lozinkaPasswordField;
    @FXML
    private Label korisnickoImeLabel;
    @FXML
    private Label lozinkaLabel;
    @FXML
    private ImageView image;
    @FXML
    private GridPane gridPane;
    List<Korisnik> korisnici = new ArrayList<>();

    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        image.fitWidthProperty().bind(gridPane.widthProperty().divide(2.5));
        image.fitHeightProperty().bind(gridPane.heightProperty().divide(1.5));

        korisnickoImeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(43).asString(), "px"));
        lozinkaLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(43).asString(), "px"));
    }

    public void napraviAccount(ActionEvent event) throws BazaPodatakaException {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = lozinkaPasswordField.getText();
        boolean zauzetoKorisnickoIme = false;

        korisnici = BazaPodataka.dohvatiKorisnike();

        for (Korisnik k: korisnici) {
            if(k.getKorisnickoIme().equals(korisnickoIme)){
                zauzetoKorisnickoIme = true;
            }
        }

        if(!korisnickoIme.isBlank() && !lozinka.isBlank()){
            if(zauzetoKorisnickoIme){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Stvaranje računa!",
                        "Pogreška stvaranja!",
                        "Korisničko ime je već zauzeto!");
            }else{
                BazaPodataka.stovoriKorisnika(new Korisnik(korisnickoIme, lozinka, 0));
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Stvaranje računa!",
                        "Uspješno stvaranja!",
                        "Korisnik " + korisnickoIme + " je uspješno stvoren!");
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Stvaranje računa!",
                    "Pogreška stvaranja!",
                    "Potrebno je unjeti sve podatke!");
        }
    }

    public void goBack() throws IOException {
        IzbornikController.promjeniEkran(
                "login/loginScreen.fxml",
                "Zoološki vrt");
    }
}
