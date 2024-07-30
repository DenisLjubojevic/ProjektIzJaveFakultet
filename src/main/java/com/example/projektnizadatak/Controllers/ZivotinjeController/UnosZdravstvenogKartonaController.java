package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Entiteti.Zivotinje.ZdravstveniKarton;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UnosZdravstvenogKartonaController {
    @FXML
    private DatePicker datumPregledaDatePicker;
    @FXML
    private TextField dijagnozaTextField;
    @FXML
    private TextField terapijaTextField;
    @FXML
    private TextArea napomeneTextArea;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label datumPregledaLabel;
    @FXML
    private Label dijagnozaLabel;
    @FXML
    private Label terapijaLabel;
    @FXML
    private Label napomenaLabel;
    @FXML
    private Button spremiButton;

    private Zivotinja odabranaZivotinja;

    private boolean popravljenLayout = false;
    public void initialize() {
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(datumPregledaLabel);
        MainApplication.setupText(dijagnozaLabel);
        MainApplication.setupText(terapijaLabel);
        MainApplication.setupText(napomenaLabel);

        MainApplication.setupButton(spremiButton);
    }

    public void setOdabranaZivotinja(Zivotinja zivotinja){
        this.odabranaZivotinja = zivotinja;
    }

    public void dodajZdravstveniKarton(){
        LocalDate datumPregleda = datumPregledaDatePicker.getValue();
        String dijagnoza = dijagnozaTextField.getText();
        String terapija = terapijaTextField.getText();
        String napomena = napomeneTextArea.getText();

        if (datumPregleda == null || dijagnoza.isEmpty() || terapija.isEmpty() || napomena.isEmpty()){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje zdravstvenog kartona!",
                    "Pogreška prilikom spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        }else{
            ZdravstveniKarton zdravstveniKarton = new ZdravstveniKarton(1, datumPregleda, dijagnoza, terapija, napomena);
            odabranaZivotinja.dodajZdravstveniKarton(zdravstveniKarton);

            try{
                BazaPodataka.spremiZdravstveniKarton(zdravstveniKarton, odabranaZivotinja.getId());

                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje zdravstvenog kartona!",
                        "Uspješno spremljen novi zdravstveni karton!",
                        "Zdravstveni karton za datum " + datumPregleda + " je uspješno spremljen!"
                );
            } catch (BazaPodatakaException ex) {
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje zdravstvenog kartona!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }

}
