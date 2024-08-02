package com.example.projektnizadatak.Controllers.RasporedRadaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zaposlenici.RasporedRada;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Smjena;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DodajRasporedController {
    @FXML
    private Label naslovLabel;
    @FXML
    private Label zaposlenikLabel;
    @FXML
    private Label ponLabel;
    @FXML
    private Label utoLabel;
    @FXML
    private Label sriLabel;
    @FXML
    private Label cetLabel;
    @FXML
    private Label petLabel;
    @FXML
    private Button spremiButton;
    @FXML
    private ChoiceBox<Zaposlenici> zaposlenikChoiceBox;
    @FXML
    private ChoiceBox<Smjena> ponChoiceBox;
    @FXML
    private ChoiceBox<Smjena> utoChoiceBox;
    @FXML
    private ChoiceBox<Smjena> sriChoiceBox;
    @FXML
    private ChoiceBox<Smjena> cetChoiceBox;
    @FXML
    private ChoiceBox<Smjena> petChoiceBox;

    List<Zaposlenici> zaposlenici = new ArrayList<>();
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            zaposlenici = BazaPodataka.dohvatiSveZaposlenike();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Zaposlenici z: zaposlenici){
            zaposlenikChoiceBox.getItems().add(z);
        }
        zaposlenikChoiceBox.getSelectionModel().selectFirst();

        for (Smjena s: Smjena.values()){
            ponChoiceBox.getItems().add(s);
            utoChoiceBox.getItems().add(s);
            sriChoiceBox.getItems().add(s);
            cetChoiceBox.getItems().add(s);
            petChoiceBox.getItems().add(s);
        }

        ponChoiceBox.getSelectionModel().selectFirst();
        utoChoiceBox.getSelectionModel().selectFirst();
        sriChoiceBox.getSelectionModel().selectFirst();
        cetChoiceBox.getSelectionModel().selectFirst();
        petChoiceBox.getSelectionModel().selectFirst();

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(zaposlenikLabel);
        MainApplication.setupText(ponLabel);
        MainApplication.setupText(utoLabel);
        MainApplication.setupText(sriLabel);
        MainApplication.setupText(cetLabel);
        MainApplication.setupText(petLabel);
        MainApplication.setupButton(spremiButton);
    }

    public void dodajRaspored(){
        Zaposlenici zaposlenik = zaposlenikChoiceBox.getSelectionModel().getSelectedItem();
        Smjena ponSmjena = ponChoiceBox.getSelectionModel().getSelectedItem();
        Smjena utoSmjena = utoChoiceBox.getSelectionModel().getSelectedItem();
        Smjena sriSmjena = sriChoiceBox.getSelectionModel().getSelectedItem();
        Smjena cetSmjena = cetChoiceBox.getSelectionModel().getSelectedItem();
        Smjena petSmjena = petChoiceBox.getSelectionModel().getSelectedItem();

        Map<String, Smjena> smjenaPoDanima = new HashMap<>();
        smjenaPoDanima.put("PON", ponSmjena);
        smjenaPoDanima.put("UTO", utoSmjena);
        smjenaPoDanima.put("SRI", sriSmjena);
        smjenaPoDanima.put("ČET", cetSmjena);
        smjenaPoDanima.put("PET", petSmjena);

        RasporedRada noviRaspored = new RasporedRada(1, zaposlenik.getId(), smjenaPoDanima);

        try{
            Promjene promjena = new Promjene(
                    1,
                    loginScreenController.prijavljeniKorisnik.getId(),
                    "Dodan raspored",
                    new Timestamp(System.currentTimeMillis()));
            try{
                BazaPodataka.spremiPromjenu(promjena);
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Pogreška!",
                        "Pogreška spremanja promjene!",
                        ex.getMessage()
                );
            }

            BazaPodataka.spremiRasporedRada(noviRaspored);
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Spremanje rasporeda!",
                    "Uspješno spremljen raspored!",
                    "Raspored za " + zaposlenik.getIme() + " " + zaposlenik.getPrezime() + " je uspješno spremljen!"
            );
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje rasporeda!",
                    "Pogreška prilikom spremanja!",
                    ex.getMessage()
            );
        }
    }
}
