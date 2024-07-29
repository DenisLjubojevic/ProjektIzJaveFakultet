package com.example.projektnizadatak.Controllers.HranaController;

import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.MainApplication;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetaljiHraneController {
    @FXML
    private Label naslovLabel;

    @FXML
    private Label vrstaHraneLabel;

    @FXML
    private Label kolicinaLabel;

    @FXML
    private Label vrijemeObrokaLabel;

    @FXML
    private Label napomenaLabel;

    @FXML
    private Label vrstaHraneTextLabel;

    @FXML
    private Label kolicinaTextLabel;

    @FXML
    private Label vrijemeObrokaTextLabel;

    @FXML
    private Label napomenaTextLabel;

    public void prikaziDetalje(Hrana hrana) {
        MainApplication.popraviLayout();

        vrstaHraneLabel.setText(hrana.getVrstaHrane());
        kolicinaLabel.setText(String.valueOf(hrana.getKolicina()) + "kg");
        vrijemeObrokaLabel.setText(hrana.getVrijemeHranjenja().toString() + "h");
        napomenaLabel.setText(hrana.getNapomena());

        MainApplication.setupNaslov(naslovLabel);
        setupLabel(vrstaHraneLabel);
        setupLabel(vrstaHraneTextLabel);
        setupLabel(kolicinaLabel);
        setupLabel(kolicinaTextLabel);
        setupLabel(vrijemeObrokaLabel);
        setupLabel(vrijemeObrokaTextLabel);
        setupLabel(napomenaLabel);
        setupLabel(napomenaTextLabel);
    }

    private void setupLabel(Label label){
        label.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(35).asString(), "px"));
    }
}
