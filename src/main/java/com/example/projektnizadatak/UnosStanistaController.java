package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Sistematika;
import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField razredZivotinjaTextField;

    public synchronized void dodajStaniste(){
        List<Staniste> staniste = new ArrayList<>();
        List<Zivotinja> zivotinje = new ArrayList<>();

        try{
            staniste = BazaPodataka.dohvatiSvaStanista();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
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

        String brojZivotinja = brojZivotinjaTextField.getText();
        String razred = razredZivotinjaTextField.getText();
        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        Integer id = staniste.size();

        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().razred().equals(razred)){
                odabraneZivotinje.add(z);
            }
        }

        if(razred.isBlank() || brojZivotinja.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje staništa!");
            alert.setHeaderText("Pogreška spremanja!");
            alert.setContentText("Potrebno je unjeti sve podatke!");

            alert.showAndWait();
        } else if (odabraneZivotinje.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje staništa!");
            alert.setHeaderText("Pogreška spremanja!");
            alert.setContentText("Potrebno je unjeti razred životinja koji već postoji!");

            alert.showAndWait();
        } else if (!razred.isBlank() && !brojZivotinja.isBlank() && odabraneZivotinje.size() != 0) {

            Staniste novoStaniste = new Staniste(id + 1, odabraneZivotinje, Integer.valueOf(brojZivotinja), false, false);
            try{
                BazaPodataka.spremiStaniste(novoStaniste);
                AzurirajZivotinjuController.spremiPromjenu( "-", novoStaniste.getClass().getSimpleName(), "admin", LocalDateTime.now());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje staništa!");
                alert.setHeaderText("Uspješno spremljeno stanište!");
                alert.setContentText("Stanište razreda " + razred + " je uspješno spremljeno!");

                alert.showAndWait();
            } catch (BazaPodatakaException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje životinje!");
                alert.setHeaderText("Pogreška prilikom spremanja!");
                alert.setContentText(ex.getMessage());

                alert.showAndWait();
            }
        }
    }
}
