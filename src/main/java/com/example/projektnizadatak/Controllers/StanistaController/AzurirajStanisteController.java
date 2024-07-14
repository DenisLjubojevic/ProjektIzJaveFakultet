package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AzurirajStanisteController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Obrok> obrokChoiceBox;

    private String stariBrojJedinki;
    private String staraVrsta;
    private Obrok stariObrok;
    List<Staniste> stanista = new ArrayList<>();
    List<Zivotinja> zivotinje = new ArrayList<>();
    List<Obrok> obroci = new ArrayList<>();
    Staniste trazenoStaniste;

    private boolean popravljenLayout = false;

    public synchronized void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            stanista = BazaPodataka.dohvatiSvaStanista();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje staništa!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        try{
            obroci = BazaPodataka.dohvatiSveObroke();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje obroka!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        for (Obrok o: obroci) {
            if (obrokChoiceBox.getItems().contains(o)){
                continue;
            }
            obrokChoiceBox.getItems().add(o);
        }

        obrokChoiceBox.getSelectionModel().selectFirst();
    }

    public synchronized void dohvatiStaniste(Staniste staniste){
        vrstaZivotinjaTextField.setText(staniste.getZivotinja().get(0).getSistematika().vrsta());
        brojZivotinjaTextField.setText(String.valueOf(staniste.getZivotinja().size()));
        obrokChoiceBox.getSelectionModel().select(staniste.getObrok());

        stariBrojJedinki = String.valueOf(staniste.getZivotinja().size());
        staraVrsta = staniste.getZivotinja().get(0).getSistematika().vrsta();
        stariObrok = staniste.getObrok();
        trazenoStaniste = staniste;
    }

    public synchronized void izmjeniStaniste(){
        String vrsta = vrstaZivotinjaTextField.getText();
        String brojJedinki = brojZivotinjaTextField.getText();
        Obrok obrok = obrokChoiceBox.getValue();

        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        boolean imaVrste = false;
        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().vrsta().equals(vrsta)){
                odabraneZivotinje.add(z);
                imaVrste = true;
            }
        }
        if (imaVrste){
            trazenoStaniste.setBrojJedinki(Integer.valueOf(brojJedinki));
            trazenoStaniste.setZivotinja(odabraneZivotinje);
            trazenoStaniste.setSistematika(new Sistematika(odabraneZivotinje.get(0).getSistematika().vrsta(), odabraneZivotinje.get(0).getSistematika().razred()));
            trazenoStaniste.setObrok(obrok);

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Potvrda izmjene");
                alert.setContentText("Jeste li sigurni da želite urediti odabrano stanište?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    if(!stariBrojJedinki.equals(brojJedinki)){
                        AzurirajZivotinjuController.spremiPromjenu(stariBrojJedinki, brojJedinki, "admin", LocalDateTime.now());
                    }

                    if(!staraVrsta.equals(vrsta)){
                        AzurirajZivotinjuController.spremiPromjenu(staraVrsta, vrsta, "admin", LocalDateTime.now());
                    }

                    BazaPodataka.azurirajStaniste(trazenoStaniste);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Izmjena staništa");
                    alert2.setHeaderText("Uspješna izmjena!");
                    alert2.setContentText("Stanište " + vrsta + " je uspješno izmjenjeno!");

                    alert2.showAndWait();
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }

            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problem");
            alert.setHeaderText("Neuspješna izmjena!");
            alert.setContentText(vrsta + " ne postoji u bazi podataka!");

            alert.showAndWait();
        }



    }

}
