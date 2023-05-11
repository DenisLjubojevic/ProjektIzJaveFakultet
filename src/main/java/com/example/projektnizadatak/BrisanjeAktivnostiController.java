package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrisanjeAktivnostiController {
    @FXML
    private TableView<Aktivnost> aktivnostiTableView;

    @FXML
    private TableColumn<Aktivnost, String> nazivTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> cijenaTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> trajanjeTableColumn;

    List<Aktivnost> aktivnosti = new ArrayList<>();
    public void initialize(){
        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje aktivnosti!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaziv()));
        cijenaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijena().toString()));
        trajanjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTrajanje().toString()));

        aktivnostiTableView.setItems(FXCollections.observableList(aktivnosti));
    }

    public void obrisiAktivnost(){
        Aktivnost aktivnost = aktivnostiTableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda brisanja");
            alert.setContentText("Jeste li sigurni da želite obrisati odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiAktivnost(aktivnost);
                AzurirajZivotinjuController.spremiPromjenu(aktivnost.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Brisanje aktivnosti");
                alert2.setHeaderText("Uspješno brisanje!");
                alert2.setContentText("Aktivnost " + aktivnost.getNaziv() + " je uspješno obrisana!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
