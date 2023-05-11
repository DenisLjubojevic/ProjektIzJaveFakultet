package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class nahraniNit implements Runnable{
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

        if(!stanista.get(0).getImaHrane()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hranjenje");
            alert.setHeaderText("Potrebno je opskrbiti slijedeća staništa hranom i vodom:");
            for (Staniste s:stanista) {
                if(!s.getImaHrane()){
                    alert.setContentText("Razred: " + s.getSistematika().razred() +
                            " s " + s.getBrojJedinki() + " jednike");
                }
            }

            alert.showAndWait();
        }
    }
}
