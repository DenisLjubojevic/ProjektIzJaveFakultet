package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosZivotinjaController {
    @FXML
    private TextField vrstaZivotinjeTextField;

    @FXML
    private TextField razredZivotinjeTextField;

    @FXML
    private TextField starostZivotinjeTextField;

    @FXML
    private RadioButton muskoRadioButton;

    @FXML
    private RadioButton zenskoRadioButton;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label vrstaLabel;
    @FXML
    private Label razredLabel;
    @FXML
    private Label starostLabel;
    @FXML
    private Button spremiButton;
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(razredLabel);
        MainApplication.setupText(starostLabel);

        MainApplication.setupRadioButton(muskoRadioButton);
        MainApplication.setupRadioButton(zenskoRadioButton);

        MainApplication.setupButton(spremiButton);
    }

    public void dodajZivotinju(){
        List<Zivotinja> zivotinje = new ArrayList<>();

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
        String vrsta = vrstaZivotinjeTextField.getText();
        String razred = razredZivotinjeTextField.getText();
        String starost = starostZivotinjeTextField.getText();
        Integer id = zivotinje.size();

        String spol;
        if(muskoRadioButton.isSelected()){
            spol = "Muško";
        }else if(zenskoRadioButton.isSelected()){
            spol = "Žensko";
        } else {
            spol = "";
        }

        if(vrsta.isBlank() || razred.isBlank() || starost.isBlank() || spol.equals("")){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje životinje!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        } else {

            Zivotinja novaZivotinja = new Zivotinja(id + 1, new Sistematika(vrsta, razred), Integer.valueOf(starost), spol);

            try{
                BazaPodataka.spremiZivotinju(novaZivotinja);
                AzurirajZivotinjuController.spremiPromjenu( "-", novaZivotinja.getClass().getSimpleName(), "admin", LocalDateTime.now());

                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje životinje!",
                        "Uspješno spremljen životinje!",
                        "Životinje razreda " + razred + " je uspješno spremljena!"
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
