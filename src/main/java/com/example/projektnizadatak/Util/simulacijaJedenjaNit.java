package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class simulacijaJedenjaNit implements Runnable{
    public List<Staniste> stanista = new ArrayList<>();

    @Override
    public synchronized void run() {
        try{
            stanista = BazaPodataka.dohvatiSvaStanista();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje staništa!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        for (Staniste s: stanista) {
            s.simulacijaJedenja();
            s.simulacijaPijenja();
            try {
                BazaPodataka.azurirajStaniste(s);
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Jedenje!");
            alert.setHeaderText("Nestašica hrane");
            alert.setContentText("Životinje su pojele svu hranu!");

            alert.showAndWait();
        }

    }
}
