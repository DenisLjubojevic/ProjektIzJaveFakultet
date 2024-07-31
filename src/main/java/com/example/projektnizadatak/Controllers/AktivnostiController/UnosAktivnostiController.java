package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnosti.Aktivnost;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosAktivnostiController {
    @FXML
    private TextField nazivTextField;

    @FXML
    private TextField cijenaTextField;

    @FXML
    private TextField trajanjeTextField;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label cijenaLabel;
    @FXML
    private Label trajanjeLabel;
    @FXML
    private Button spremiButton;


    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(nazivLabel);
        MainApplication.setupText(cijenaLabel);
        MainApplication.setupText(trajanjeLabel);

        MainApplication.setupButton(spremiButton);
    }

    public void dodajAktivnost(){
        List<Aktivnost> aktivnosti = new ArrayList<>();

        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje aktivnosti!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();
        Integer id = aktivnosti.size();

        if(naziv.isBlank() || cijena.isBlank() || trajanje.isBlank()){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje aktivnosti!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        } else {
            Aktivnost novaAktivnost = new Aktivnost.Builder(id + 1, naziv, Integer.valueOf(cijena)).saTrajanjem(Integer.valueOf(trajanje)).build();
            try{
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Dodana aktivnost",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){

                }

                BazaPodataka.spremiAktivnost(novaAktivnost);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje aktivnosti!",
                        "Uspješno spremljena aktivnost!",
                        "Aktivnost " + naziv + " je uspješno spremljena!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje aktivnosti!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }
}
