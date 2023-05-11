package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Korisnik;
import com.example.projektnizadatak.Util.Datoteke;
import com.example.projektnizadatak.Util.Hashiranje;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class loginScreenController {
    @FXML
    private ImageView background;
    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private TextField lozinkaTextField;

    @FXML
    private PasswordField skrivenaLozinkaTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    File file = new File("Datoteke/korisnici.txt");
    HashMap<String, String> korisnici = new HashMap<>();
    Hashiranje hashiranje = new Hashiranje();

    public static String roleKorisnika;

    public void initialize(){
        changeVisibility(new ActionEvent());
        background.setImage(new Image("C:\\Users\\Dean\\Desktop\\Skola\\TVZ\\III. Semestar\\Java\\Projektni Zadatak\\ProjektniZadatak\\src\\main\\java\\com\\example\\projektnizadatak\\Images\\loginBackground.jpg"));
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

    public void napraviAccount(ActionEvent event){
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = getLozinka();
        try(BufferedWriter bufferKorisnici = new BufferedWriter(new FileWriter(file, true))){
            bufferKorisnici.write(korisnickoIme + "," + hashiranje.hashiranaLozinka(lozinka) + ",korisnik\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stvaranje računa!");
            alert.setHeaderText("Uspješno napravljen račun!");
            alert.setContentText("Uspješno je stvoreni račun pod imenom: " + korisnickoIme);

            alert.showAndWait();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void ucitajKorisnika(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = getLozinka();
        korisnici = Datoteke.dohvatiKorisnike();

        String hashiranaLozinka = korisnici.get(korisnickoIme);
        if (hashiranje.hashiranaLozinka(lozinka).equals(hashiranaLozinka)){
            for (String s: Datoteke.roles.keySet()) {
                if(s.equals(korisnickoIme) && Datoteke.roles.get(s).equals("admin")){
                    roleKorisnika = "admin";
                }else{
                    roleKorisnika = "korisnik";
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spajanje korisnika!");
            alert.setHeaderText("Uspjenšno spajanje!");
            alert.setContentText("Prijavljeni ste kao " + roleKorisnika);

            alert.showAndWait();

            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("pretragaZivotinja.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            MainApplication.getMainStage().setTitle("Pretraga životinja");
            MainApplication.getMainStage().setScene(scene);
            MainApplication.getMainStage().show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spajanje korisnika!");
            alert.setHeaderText("Neuspješno spajanje!");
            alert.setContentText("Pogrešno korisničko ime ili lozinka!");

            alert.showAndWait();
        }
    }
}