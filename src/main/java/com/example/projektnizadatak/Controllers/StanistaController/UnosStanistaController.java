package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Obrok> obrokChoiceBox;

    List<Obrok> obroci = new ArrayList<>();
    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            obroci = BazaPodataka.dohvatiSveObroke();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Obrok o: obroci) {
            if (obrokChoiceBox.getItems().contains(o)){
                continue;
            }
            obrokChoiceBox.getItems().add(o);
        }

        obrokChoiceBox.getSelectionModel().selectFirst();
    }

    public synchronized void dodajStaniste() {
        List<Staniste> staniste = new ArrayList<>();
        List<Zivotinja> zivotinje = new ArrayList<>();

        try{
            staniste = BazaPodataka.dohvatiSvaStanista();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje staništa!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje životinja!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        String brojZivotinja = brojZivotinjaTextField.getText();
        String vrsta = vrstaZivotinjaTextField.getText();
        Obrok obrok = obrokChoiceBox.getValue();

        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        Integer id = staniste.size();

        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().vrsta().equals(vrsta)){
                odabraneZivotinje.add(z);
            }
        }

        if(vrsta.isBlank() || brojZivotinja.isBlank()){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje staništa!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        } else if (odabraneZivotinje.isEmpty()) {
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje staništa!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti vrstu životinja koja već postoji!"
            );
        } else {

            Staniste novoStaniste = new Staniste(id + 1, odabraneZivotinje, Integer.valueOf(brojZivotinja), obrok);
            try{
                BazaPodataka.spremiStaniste(novoStaniste);
                AzurirajZivotinjuController.spremiPromjenu( "-", novoStaniste.getClass().getSimpleName(), "admin", LocalDateTime.now());

                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje staništa!",
                        "Uspješno spremljeno stanište!",
                        "Stanište " + vrsta + " je uspješno spremljeno!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje životinje!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }
}
