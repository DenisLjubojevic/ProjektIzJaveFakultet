package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnosti.Aktivnost;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
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

    @FXML
    private Label naslovLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label cijenaLabel;
    @FXML
    private Label trajanjeLabel;
    @FXML
    private Button promjeniButton;
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

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(nazivLabel);
        MainApplication.setupText(cijenaLabel);
        MainApplication.setupText(trajanjeLabel);

        MainApplication.setupButton(promjeniButton);
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
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Ažurirana aktivnost",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){

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
