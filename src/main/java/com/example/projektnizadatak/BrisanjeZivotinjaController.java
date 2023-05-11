package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Zivotinja;
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
import java.util.List;
import java.util.Optional;

public class BrisanjeZivotinjaController {
    @FXML
    private TableView<Zivotinja> zivotinjaTableView;

    @FXML
    private TableColumn<Zivotinja, String> vrstaZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> razredZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> starostZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> spolZivotinjeTableColumn;

    private List<Zivotinja> zivotinje;

    public void initialize(){
        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        vrstaZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().vrsta()));
        razredZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().razred()));
        starostZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStarost().toString()));
        spolZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpol()));

        zivotinjaTableView.setItems(FXCollections.observableList(zivotinje));
    }

    public void obrisiZivotinju(){
        Zivotinja zivotinja = zivotinjaTableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda brisanja");
            alert.setContentText("Jeste li sigurni da želite obrisati odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiZivotinju(zivotinja);
                AzurirajZivotinjuController.spremiPromjenu(zivotinja.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Brisanje životinje");
                alert2.setHeaderText("Uspješno brisanje!");
                alert2.setContentText("Životinja razreda " + zivotinja.getSistematika().razred() + " je uspješno obrisana!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
