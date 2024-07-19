package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AzurirajAktivnostiController {
    @FXML
    private TextField nazivTextField;

    @FXML
    private TextField cijenaTextField;

    @FXML
    private TextField trajanjeTextField;
    private String stariNaziv;
    private String staraCijena;
    private String staroTrajanje;

    List<Aktivnost> aktivnosti = new ArrayList<>();
    Aktivnost trazenaAktivnost;
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje aktivnosti!",
                    "Pogreška učitavanja!",
                    ex.getMessage());
        }
    }

    public void dohvatiAktivnosti(Aktivnost aktivnost){
        nazivTextField.setText(aktivnost.getNaziv());
        cijenaTextField.setText(aktivnost.getCijena().toString());
        trajanjeTextField.setText(aktivnost.getTrajanje().toString());

        stariNaziv = aktivnost.getNaziv();
        staraCijena = aktivnost.getCijena().toString();
        staroTrajanje = aktivnost.getTrajanje().toString();

        trazenaAktivnost = aktivnost;
    }

    public void izmjeniAktivnost(){
        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();

        trazenaAktivnost.setNaziv(naziv);
        trazenaAktivnost.setCijena(Integer.valueOf(cijena));
        trazenaAktivnost.setTrajanje(Integer.valueOf(trajanje));

        try {
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda izmjene",
                    "Jeste li sigurni da želite promjeniti odabranu aktivnost?");

            if(result.get() == ButtonType.OK){
                if(!naziv.equals(stariNaziv)){
                    AzurirajZivotinjuController.spremiPromjenu(stariNaziv, naziv, "admin", LocalDateTime.now());
                }

                if(!cijena.equals(staraCijena)){
                    AzurirajZivotinjuController.spremiPromjenu(staraCijena, cijena, "admin", LocalDateTime.now());
                }

                if(!trajanje.equals(staroTrajanje)){
                    AzurirajZivotinjuController.spremiPromjenu(staroTrajanje, trajanje, "admin", LocalDateTime.now());
                }

                BazaPodataka.azurirajAktivnost(trazenaAktivnost);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Izmjena aktivnosti",
                        "Uspješna izmjena!",
                        "Aktivnost " + naziv + " je uspješno izmjenjena!");
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
