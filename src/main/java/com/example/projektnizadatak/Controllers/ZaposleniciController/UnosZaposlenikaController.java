package com.example.projektnizadatak.Controllers.ZaposleniciController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    private Label naslovLabel;
    @FXML
    private Label imeLabel;
    @FXML
    private Label prezimeLabel;
    @FXML
    private Label cijenaLabel;
    @FXML
    private Label satnicaLabel;
    @FXML
    private Label posaoLabel;
    @FXML
    private Button spremiButton;

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
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Zaposlenici z: zaposlenici) {
            if (posaoZaposlenikaChoiceBox.getItems().contains(z.getZanimanje())){
                continue;
            }
            posaoZaposlenikaChoiceBox.getItems().add(z.getZanimanje());
        }

        posaoZaposlenikaChoiceBox.getSelectionModel().selectFirst();

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(imeLabel);
        MainApplication.setupText(prezimeLabel);
        MainApplication.setupText(cijenaLabel);
        MainApplication.setupText(satnicaLabel);
        MainApplication.setupText(posaoLabel);

        MainApplication.setupButton(spremiButton);
    }

    public void dodajZaposlenika(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenaPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();
        Integer id = zaposlenici.size();

        if(ime.isBlank() || prezime.isBlank() || cijenaPoSatu.isBlank() || satnica.isBlank()){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje zaposlenika!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        } else {

            Zaposlenici noviZaposlenik = new Zaposlenici(id + 1, posao, Integer.valueOf(cijenaPoSatu), Integer.valueOf(satnica), ime, prezime);

            try{
                BazaPodataka.spremiZaposlenika(noviZaposlenik);
                AzurirajZivotinjuController.spremiPromjenu( "-", noviZaposlenik.getClass().getSimpleName(), "admin", LocalDateTime.now());

                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje zaposlenika!",
                        "Uspješno spremljen zaposlenik!",
                        "Zaposlenik " + ime + " " + prezime + " je uspješno spremljen!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje zaposlenika!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }
}
