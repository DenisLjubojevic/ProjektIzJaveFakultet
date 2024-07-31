package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Entiteti.Stanista.RasporedHranjenja;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class RasporedHranjenjaController {
    @FXML
    private Label naslovLabel;

    @FXML
    private ListView<String> rasporedListView;

    private RasporedHranjenja rasporedHranjenja;
    private List<Staniste> stanista;

    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        rasporedHranjenja = new RasporedHranjenja();

        MainApplication.setupNaslov(naslovLabel);

        try{
            stanista = BazaPodataka.dohvatiSvaStanista();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje staništa!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Staniste s: stanista){
            rasporedHranjenja.dodajHranjenje(s, s.getVrijemeHranjenja());
        }

        ispisiRaspored();
    }

    private void ispisiRaspored(){
        rasporedListView.getItems().clear();
        List<Map.Entry<Staniste, LocalTime>> sortiraniRaspored = rasporedHranjenja.dohvatiSortiraniRaspored();

        for (Map.Entry<Staniste, LocalTime> entry: sortiraniRaspored){
            rasporedListView.getItems().add(entry.getValue() + " - Hranjenje staništa " + entry.getKey().getSistematika().vrsta());
        }
    }
}
