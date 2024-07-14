package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }
    }

    public void dodajZivotinju(){
        List<Zivotinja> zivotinje = new ArrayList<>();

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje životinje!");
            alert.setHeaderText("Pogreška spremanja!");
            alert.setContentText("Potrebno je unjeti sve podatke!");

            alert.showAndWait();
        } else if (!vrsta.isBlank() && !razred.isBlank() && !spol.equals("") && !starost.isBlank()) {

            Zivotinja novaZivotinja = new Zivotinja(id + 1, new Sistematika(vrsta, razred), Integer.valueOf(starost), spol);

            try{
                BazaPodataka.spremiZivotinju(novaZivotinja);
                AzurirajZivotinjuController.spremiPromjenu( "-", novaZivotinja.getClass().getSimpleName(), "admin", LocalDateTime.now());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje profesora!");
                alert.setHeaderText("Uspješno spremljen životinje!");
                alert.setContentText("Životinje razreda " + razred + " je uspješno spremljena!");

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
