package com.example.projektnizadatak.Controllers.ZaposleniciController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AzurirajZaposlenikaController {
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

    private String staroIme;
    private String staroPrezime;
    private String staraCijenaPoSatu;
    private String staraSatnica;
    private String stariPosao;


    private List<Zaposlenici> zaposlenici;
    private Zaposlenici trazeniZaposlenik;
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
    }

    public void dohvatiZaposlenika(Zaposlenici zaposlenik){
        imeZaposlenikaTextField.setText(zaposlenik.getIme());
        prezimeZaposlenikaTextField.setText(zaposlenik.getPrezime());
        cijenaPoSatuZaposlenikaTextField.setText(zaposlenik.getCijenaPoSatu().toString());
        satnicaZaposlenikaTextField.setText(zaposlenik.getMjesecnaSatnica().toString());
        posaoZaposlenikaChoiceBox.getSelectionModel().select(zaposlenik.getZanimanje());

        staroIme = zaposlenik.getIme();
        staroPrezime = zaposlenik.getPrezime();
        staraCijenaPoSatu = zaposlenik.getCijenaPoSatu().toString();
        staraSatnica = zaposlenik.getMjesecnaSatnica().toString();
        stariPosao = posaoZaposlenikaChoiceBox.getValue();

        trazeniZaposlenik = zaposlenik;
    }

    public void izmjeniZaposlenika(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenaPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();

        trazeniZaposlenik.setIme(ime);
        trazeniZaposlenik.setPrezime(prezime);
        trazeniZaposlenik.setCijenaPoSatu(Integer.valueOf(cijenaPoSatu));
        trazeniZaposlenik.setMjesecnaSatnica(Integer.valueOf(satnica));
        trazeniZaposlenik.setZanimanje(posao);

        try {
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda izmjene",
                    "Jeste li sigurni da želite promjeniti odabranog zaposlenika?"
            );

            if(result.get() == ButtonType.OK){
                if(!staroIme.equals(ime)){
                    AzurirajZivotinjuController.spremiPromjenu(staroIme, ime, "admin", LocalDateTime.now());
                }

                if(!staroPrezime.equals(prezime)){
                    AzurirajZivotinjuController.spremiPromjenu(staroPrezime, prezime, "admin", LocalDateTime.now());
                }

                if(!staraCijenaPoSatu.equals(cijenaPoSatu)){
                    AzurirajZivotinjuController.spremiPromjenu(staraCijenaPoSatu, cijenaPoSatu, "admin", LocalDateTime.now());
                }

                if(!staraSatnica.equals(satnica)){
                    AzurirajZivotinjuController.spremiPromjenu(staraSatnica, satnica, "admin", LocalDateTime.now());
                }

                if(!stariPosao.equals(posao)){
                    AzurirajZivotinjuController.spremiPromjenu(stariPosao, posao, "admin", LocalDateTime.now());
                }

                BazaPodataka.azurirajZaposlenika(trazeniZaposlenik);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Izmjena zaposlenika",
                        "Uspješna izmjena!",
                        "Zaposlenik " + ime + " " + prezime + " je uspješno izmjenjen!"
                );
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
