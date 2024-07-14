package com.example.projektnizadatak.Controllers.LoginController;

import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Korisnik;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import com.example.projektnizadatak.Util.Hashiranje;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class loginScreenController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private TextField lozinkaTextField;

    @FXML
    private PasswordField skrivenaLozinkaTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    List<Korisnik> korisnici = new ArrayList<>();
    Hashiranje hashiranje = new Hashiranje();

    public static String roleKorisnika;

    public void initialize(){
        MainApplication.popraviLayout();

        changeVisibility(new ActionEvent());

        String imagePath = "/com/example/projektniZadatak/Images/loginBackground.jpg";
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());

        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage myBgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        anchorPane.setBackground(new Background(myBgImage));
    }

    public void changeVisibility(ActionEvent event){
        if(showPasswordCheckBox.isSelected()){
            lozinkaTextField.setText(skrivenaLozinkaTextField.getText());
            lozinkaTextField.setVisible(true);
            skrivenaLozinkaTextField.setVisible(false);
            return;
        }
        skrivenaLozinkaTextField.setText(lozinkaTextField.getText());
        skrivenaLozinkaTextField.setVisible(true);
        lozinkaTextField.setVisible(false);
    }

    private String getLozinka(){
        if(lozinkaTextField.isVisible()){
            return lozinkaTextField.getText();
        } else {
            return skrivenaLozinkaTextField.getText();
        }
    }

    public void napraviAccount(ActionEvent event) throws BazaPodatakaException {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = getLozinka();
        boolean zauzetoKorisnickoIme = false;

        korisnici = BazaPodataka.dohvatiKorisnike();

        for (Korisnik k: korisnici) {
            if(k.getKorisnickoIme().equals(korisnickoIme)){
                zauzetoKorisnickoIme = true;
            }
        }

        if(!korisnickoIme.isBlank() && !lozinka.isBlank()){
            if(zauzetoKorisnickoIme){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Stvaranje računa!");
                alert.setHeaderText("Pogreška stvaranja!");
                alert.setContentText("Korisničko ime je već zauzeto!");

                alert.showAndWait();
            }else{
                BazaPodataka.stovoriKorisnika(new Korisnik(korisnickoIme, lozinka, 0));
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stvaranje računa!");
            alert.setHeaderText("Pogreška stvaranja!");
            alert.setContentText("Potrebno je unjeti sve podatke!");

            alert.showAndWait();
        }
    }
    public void ucitajKorisnika(ActionEvent event) throws IOException, NoSuchAlgorithmException, BazaPodatakaException {
        try{
            korisnici = BazaPodataka.dohvatiKorisnike();

            String korisnickoIme = korisnickoImeTextField.getText();
            String lozinka = getLozinka();
            String hashiranaLozinka = hashiranje.hashiranaLozinka(lozinka);
            boolean uspješnaPrijava = false;

            for (Korisnik korisnik : korisnici) {
                if (korisnik.getKorisnickoIme().equals(korisnickoIme) &&
                        korisnik.getLozinka().equals(hashiranaLozinka)) {
                    uspješnaPrijava = true;

                    if (korisnik.getRole().equals(1)){
                        roleKorisnika = "admin";
                    }else{
                        roleKorisnika = "user";
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Spajanje korisnika!");
                    alert.setHeaderText("Uspjenšno spajanje!");
                    alert.setContentText("Prijavljeni ste kao " + roleKorisnika);

                    alert.showAndWait();

                    IzbornikController.promjeniEkran(
                            "zivotinje/pretragaZivotinja.fxml",
                            "Pretraga životinja");
                }
            }

            if (!uspješnaPrijava){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spajanje korisnika!");
                alert.setHeaderText("Neuspješno spajanje!");
                alert.setContentText("Pogrešno korisničko ime ili lozinka!");

                alert.showAndWait();
            }
        }catch (BazaPodatakaException e) {
            e.printStackTrace();
            showAlert("Pogreška", "Pogreška prilikom povezivanja na bazu podataka!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Pogreška", "Pogreška prilikom učitavanja fxml datoteke!");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            showAlert("Pogreška", "Pogreška prilikom hashiranja lozinke!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}