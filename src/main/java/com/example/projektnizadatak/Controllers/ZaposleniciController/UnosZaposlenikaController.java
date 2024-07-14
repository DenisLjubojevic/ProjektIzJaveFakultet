package com.example.projektnizadatak.Controllers.ZaposleniciController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosZaposlenikaController {
    @FXML
    private TextField imeZaposlenikaTextField;

    @FXML
    private TextField prezimeZaposlenikaTextField;

    @FXML
    private TextField cijenaPoSatuZaposlenikaTextField;

    @FXML
    private TextField satnicaZaposlenikaTextField;

    @FXML
    private ChoiceBox<String> posaoZaposlenikaChoiceBox;

    List<Zaposlenici> zaposlenici = new ArrayList<>();
    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            zaposlenici = BazaPodataka.dohvatiSveZaposlenike();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje zaposlenika!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        for (Zaposlenici z: zaposlenici) {
            if (posaoZaposlenikaChoiceBox.getItems().contains(z.getZanimanje())){
                continue;
            }
            posaoZaposlenikaChoiceBox.getItems().add(z.getZanimanje());
        }

        posaoZaposlenikaChoiceBox.getSelectionModel().selectFirst();
    }

    public void dodajZaposlenika(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenaPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();
        Integer id = zaposlenici.size();

        if(ime.isBlank() || prezime.isBlank() || cijenaPoSatu.isBlank() || satnica.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje zaposlenika!");
            alert.setHeaderText("Pogreška spremanja!");
            alert.setContentText("Potrebno je unjeti sve podatke!");

            alert.showAndWait();
        } else {

            Zaposlenici noviZaposlenik = new Zaposlenici(id + 1, posao, Integer.valueOf(cijenaPoSatu), Integer.valueOf(satnica), ime, prezime);

            try{
                BazaPodataka.spremiZaposlenika(noviZaposlenik);
                AzurirajZivotinjuController.spremiPromjenu( "-", noviZaposlenik.getClass().getSimpleName(), "admin", LocalDateTime.now());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje zaposlenika!");
                alert.setHeaderText("Uspješno spremljen zaposlenik!");
                alert.setContentText("Zaposlenik " + ime + " " + prezime + " je uspješno spremljen!");

                alert.showAndWait();
            } catch (BazaPodatakaException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje zaposlenika!");
                alert.setHeaderText("Pogreška prilikom spremanja!");
                alert.setContentText(ex.getMessage());

                alert.showAndWait();
            }
        }
    }
}
