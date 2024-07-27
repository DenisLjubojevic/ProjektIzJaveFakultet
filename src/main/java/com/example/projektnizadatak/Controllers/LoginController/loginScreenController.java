package com.example.projektnizadatak.Controllers.LoginController;

import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Korisnici.Korisnik;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Hashiranje;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    @FXML
    private Label naslovLabel;
    @FXML
    private Label korisnickoImeLabel;
    @FXML
    private Label lozinkaLabel;

    List<Korisnik> korisnici = new ArrayList<>();
    Hashiranje hashiranje = new Hashiranje();

    public static String roleKorisnika;

    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

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

        naslovLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(20).asString(), "px"));
        korisnickoImeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(31).asString(), "px"));
        lozinkaLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(31).asString(), "px"));
        korisnickoImeTextField.styleProperty().bind(Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(6).asString(), "px"));
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

    public void napraviAccount() throws IOException {
        IzbornikController.promjeniEkran(
                "login/signin.fxml",
                "Stvaranje računa");
    }
    public void ucitajKorisnika(ActionEvent event) throws IOException, NoSuchAlgorithmException, BazaPodatakaException {
        try{
            korisnici = BazaPodataka.dohvatiKorisnike();

            String korisnickoIme = korisnickoImeTextField.getText();
            String lozinka = getLozinka();
            String hashiranaLozinka = hashiranje.hashiranaLozinka(lozinka);
            boolean uspjesnaPrijava = false;

            for (Korisnik korisnik : korisnici) {
                if (korisnik.getKorisnickoIme().equals(korisnickoIme) &&
                        korisnik.getLozinka().equals(hashiranaLozinka)) {
                    uspjesnaPrijava = true;

                    if (korisnik.getRole().equals(1)){
                        roleKorisnika = "admin";
                    }else{
                        roleKorisnika = "user";
                    }

                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Spajanje korisnika!",
                            "Uspjenšno spajanje!",
                            "Prijavljeni ste kao " + roleKorisnika);

                    IzbornikController.promjeniEkran(
                            "zivotinje/pretragaZivotinja.fxml",
                            "Pretraga životinja");
                }
            }

            if (!uspjesnaPrijava){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spajanje korisnika!",
                        "Neuspješno spajanje!",
                        "Pogrešno korisničko ime ili lozinka!");
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