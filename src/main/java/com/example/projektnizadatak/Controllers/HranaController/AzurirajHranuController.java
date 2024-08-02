package com.example.projektnizadatak.Controllers.HranaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AzurirajHranuController {
    @FXML
    private TextField vrstaHraneTextField;
    @FXML
    private TextField kolicinaTextField;
    @FXML
    private TextField napomenaTextField;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label vrstaHraneLabel;
    @FXML
    private Label kolicinaLabel;
    @FXML
    private Label napomenaLabel;
    @FXML
    private Button promjeniButton;

    List<Hrana> hrane = new ArrayList<>();
    Hrana trazenaHrana;

    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            hrane = BazaPodataka.dohvatiSvuHranu();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje obroka!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaHraneLabel);
        MainApplication.setupText(kolicinaLabel);
        MainApplication.setupText(napomenaLabel);

        MainApplication.setupButton(promjeniButton);
    }

    public synchronized void dohvatiHranu(Hrana hrana){
        vrstaHraneTextField.setText(hrana.getVrstaHrane());
        kolicinaTextField.setText(String.valueOf(hrana.getKolicina()));
        napomenaTextField.setText(hrana.getNapomena());

        trazenaHrana = hrana;
    }

    public synchronized void izmjeniHranu(){
        String vrstaHrane = vrstaHraneTextField.getText();
        String kolicina = kolicinaTextField.getText();
        String napomena = napomenaTextField.getText();

        trazenaHrana.setVrstaHrane(vrstaHrane);
        trazenaHrana.setKolicina(Double.parseDouble(kolicina));
        trazenaHrana.setNapomena(napomena);

        try{
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda izmjene",
                    "Jeste li sigurni da želite urediti odabranu hranu?"
            );

            if (result.get() == ButtonType.OK){
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Ažurirana hrana",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){
                    MainApplication.showAlertDialog(
                            Alert.AlertType.ERROR,
                            "Pogreška!",
                            "Pogreška spremanja promjene!",
                            ex.getMessage()
                    );
                }

                BazaPodataka.azurirajHranu(trazenaHrana);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Izmjena hrane",
                        "Uspješna izmjena!",
                        "Hrana " + trazenaHrana.getVrstaHrane() + " je uspješno izmjenjena!"
                );
            }

        }catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
