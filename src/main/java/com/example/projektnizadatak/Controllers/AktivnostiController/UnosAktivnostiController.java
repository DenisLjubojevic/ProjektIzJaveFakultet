package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnosAktivnostiController {
    @FXML
    private TextField nazivTextField;

    @FXML
    private TextField cijenaTextField;

    @FXML
    private TextField trajanjeTextField;

    public void dodajAktivnost(){
        List<Aktivnost> aktivnosti = new ArrayList<>();

        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje aktivnosti!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();
        Integer id = aktivnosti.size();

        if(naziv.isBlank() || cijena.isBlank() || trajanje.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje aktivnosti!");
            alert.setHeaderText("Pogreška spremanja!");
            alert.setContentText("Potrebno je unjeti sve podatke!");

            alert.showAndWait();
        } else if (!naziv.isBlank() && !cijena.isBlank() && !trajanje.isBlank()) {
            Aktivnost novaAktivnost = new Aktivnost.Builder(id + 1, naziv, Integer.valueOf(cijena)).saTrajanjem(Integer.valueOf(trajanje)).build();
            try{
                BazaPodataka.spremiAktivnost(novaAktivnost);
                AzurirajZivotinjuController.spremiPromjenu( "-", novaAktivnost.getClass().getSimpleName(), "admin", LocalDateTime.now());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje aktivnosti!");
                alert.setHeaderText("Uspješno spremljena aktivnost!");
                alert.setContentText("Aktivnost " + naziv + " je uspješno spremljena!");

                alert.showAndWait();
            } catch (BazaPodatakaException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje aktivnosti!");
                alert.setHeaderText("Pogreška prilikom spremanja!");
                alert.setContentText(ex.getMessage());

                alert.showAndWait();
            }
        }
    }
}
