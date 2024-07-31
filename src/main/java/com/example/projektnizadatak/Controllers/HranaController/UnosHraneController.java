package com.example.projektnizadatak.Controllers.HranaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UnosHraneController {
    @FXML
    private TextField vrstaHraneTextField;
    @FXML
    private TextField kolicinaTextField;
    @FXML
    private TextField napomenaTextField;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label vrstaHraneLabel;
    @FXML
    private Label kolicinaLabel;
    @FXML
    private Label napomenaLabel;
    @FXML
    private Button spremiButton;


    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaHraneLabel);
        MainApplication.setupText(kolicinaLabel);
        MainApplication.setupText(napomenaLabel);

        MainApplication.setupButton(spremiButton);
    }

    public void dodajHranu(){
        List<Hrana> hrane = new ArrayList<>();

        try{
            hrane = BazaPodataka.dohvatiSvuHranu();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje hrane!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }
        Integer id = hrane.size();
        String vrstaHrane = vrstaHraneTextField.getText();
        String kolicina = kolicinaTextField.getText();
        String napomena = napomenaTextField.getText();

        if (vrstaHrane.isEmpty() || kolicina.isEmpty() || napomena.isEmpty()){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje hrane!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve apodatke!"
            );
        }else{

            Hrana novaHrana = new Hrana(id + 1, vrstaHrane, Double.parseDouble(kolicina), napomena);

            try{
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Dodana hrana",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){
                    System.out.println("Greška: " + ex);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.ERROR,
                            "Spremanje promjene!",
                            "Pogreška spremanja!",
                            ex.getMessage()
                    );
                }

                BazaPodataka.spremiHranu(novaHrana);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje hrane!",
                        "Uspješno spremljena nova vrsta hrane!",
                        "Hrana " + vrstaHrane + " je uspješno spremljena!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje hrane!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }


}
