package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class AzurirajZivotinjuController {
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
    private Button izmjeniButton;

    final ToggleGroup spolToggleGroup = new ToggleGroup();

    private String stariRazred;

    private Zivotinja trazenaZivotinja;
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        muskoRadioButton.setToggleGroup(spolToggleGroup);
        zenskoRadioButton.setToggleGroup(spolToggleGroup);

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(razredLabel);
        MainApplication.setupText(starostLabel);

        MainApplication.setupRadioButton(muskoRadioButton);
        MainApplication.setupRadioButton(zenskoRadioButton);

        MainApplication.setupButton(izmjeniButton);
    }

    public void dohvatiZivotinju(Zivotinja zivotinja){
        vrstaZivotinjeTextField.setText(zivotinja.getSistematika().vrsta());
        razredZivotinjeTextField.setText(zivotinja.getSistematika().razred());
        starostZivotinjeTextField.setText(zivotinja.getStarost().toString());

        if(zivotinja.getSpol().equals("Muško")){
            spolToggleGroup.selectToggle(muskoRadioButton);
        }else if (zivotinja.getSpol().equals("Žensko")){
            spolToggleGroup.selectToggle(zenskoRadioButton);
        }

        stariRazred = razredZivotinjeTextField.getText();
        trazenaZivotinja = zivotinja;
    }

    public void izmjeniZivotinju(){
        String vrsta = vrstaZivotinjeTextField.getText();
        String razred = razredZivotinjeTextField.getText();
        String starost = starostZivotinjeTextField.getText();

        String spol = "";
        if(muskoRadioButton.isSelected()){
            spol = "Muško";
        }else if(zenskoRadioButton.isSelected()){
            spol = "Žensko";
        }

        trazenaZivotinja.setSistematika(new Sistematika(vrsta, razred));
        trazenaZivotinja.setStarost(Integer.valueOf(starost));
        trazenaZivotinja.setSpol(spol);

        try {
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda izmjene",
                    "Jeste li sigurni da želite promjeniti odabranu životinju?"
            );

            if(result.get() == ButtonType.OK){
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Ažurirana životinja",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){

                }

                BazaPodataka.azurirajZivotinju(trazenaZivotinja);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Izmjena životinje",
                        "Uspješna izmjena!",
                        "Životinja razreda " + stariRazred + " je uspješno izmjenjena!"
                );
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
