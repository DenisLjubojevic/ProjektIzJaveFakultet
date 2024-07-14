package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetaljiObrokaController {
    @FXML
    private Label vrstaHraneLabel;

    @FXML
    private Label kolicinaLabel;

    @FXML
    private Label vrijemeObrokaLabel;

    @FXML
    private Label napomenaLabel;

    public void prikaziDetalje(Obrok obrok) {
        MainApplication.popraviLayout();
        vrstaHraneLabel.setText(obrok.getVrstaHrane());
        kolicinaLabel.setText(String.valueOf(obrok.getKolicina()) + "kg");
        vrijemeObrokaLabel.setText(obrok.getVrijemeObroka().toString() + "h");
        napomenaLabel.setText(obrok.getNapomena());
    }

}
