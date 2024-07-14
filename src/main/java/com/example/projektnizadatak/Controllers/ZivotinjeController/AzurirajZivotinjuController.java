package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private List<Zivotinja> zivotinje;
    final ToggleGroup spolToggleGroup = new ToggleGroup();

    private String staraVrsta;
    private String stariRazred;
    private String staraStarost;
    private String stariSpol;

    private Zivotinja trazenaZivotinja;
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        muskoRadioButton.setToggleGroup(spolToggleGroup);
        zenskoRadioButton.setToggleGroup(spolToggleGroup);
    }

    public void dohvatiZivotinju(Zivotinja zivotinja){
        vrstaZivotinjeTextField.setText(zivotinja.getSistematika().vrsta());
        razredZivotinjeTextField.setText(zivotinja.getSistematika().razred());
        starostZivotinjeTextField.setText(zivotinja.getStarost().toString());

        if(zivotinja.getSpol().equals("Muško")){
            spolToggleGroup.selectToggle(muskoRadioButton);
            stariSpol = "Muško";
        }else if (zivotinja.getSpol().equals("Žensko")){
            spolToggleGroup.selectToggle(zenskoRadioButton);
            stariSpol = "Žensko";
        }

        staraVrsta = vrstaZivotinjeTextField.getText();
        stariRazred = razredZivotinjeTextField.getText();
        staraStarost = starostZivotinjeTextField.getText();
        trazenaZivotinja = zivotinja;
    }

    public void izmjeniZivotinju(){
        String vrsta = vrstaZivotinjeTextField.getText();
        String razred = razredZivotinjeTextField.getText();
        String starost = starostZivotinjeTextField.getText();
        LocalDateTime sada = LocalDateTime.now();

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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda izmjene");
            alert.setContentText("Jeste li sigurni da želite promjeniti odabranu životinju?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                if(!staraVrsta.equals(vrsta)){
                    spremiPromjenu(staraVrsta, vrsta, "admin", sada);
                }

                if(!stariRazred.equals(razred)){
                    spremiPromjenu(stariRazred, razred, "admin", sada);
                }

                if(!staraStarost.equals(starost)){
                    spremiPromjenu(staraStarost, starost, "admin", sada);
                }

                if(!stariSpol.equals(spol)){
                    spremiPromjenu(stariSpol, spol, "admin", sada);
                }

                BazaPodataka.azurirajZivotinju(trazenaZivotinja);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Izmjena životinje");
                alert2.setHeaderText("Uspješna izmjena!");
                alert2.setContentText("Životinja razreda " + stariRazred + " je uspješno izmjenjena!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }

    public static void spremiPromjenu(String staraVrijednost, String novaVrijednost, String role, LocalDateTime vrijeme){
        Optional<Promjene> promjena = Datoteke.deserijalizirajPromjene();
        promjena.get().setStaraVrijednost(staraVrijednost);
        promjena.get().setNovaVrijednost(novaVrijednost);
        promjena.get().setRole(role);
        promjena.get().setDatumIVrijeme(vrijeme);
        Datoteke.serijalizirajPromjene(promjena.get());
    }
}
