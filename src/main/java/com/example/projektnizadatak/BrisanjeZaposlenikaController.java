package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Zaposlenici;
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

public class BrisanjeZaposlenikaController {
    @FXML
    private TableView<Zaposlenici> zaposleniciTableView;

    @FXML
    private TableColumn<Zaposlenici, String> imeZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> prezimeZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> cijenaPoSatuZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> satnicaZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> posaoZaposlenikaTableColumn;

    private List<Zaposlenici> zaposlenici;

    public void initialize(){
        try{
            zaposlenici = BazaPodataka.dohvatiSveZaposlenike();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje zaposlenika!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        imeZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));
        cijenaPoSatuZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijenaPoSatu().toString()));
        satnicaZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMjesecnaSatnica().toString()));
        posaoZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZanimanje()));

        zaposleniciTableView.setItems(FXCollections.observableList(zaposlenici));
    }

    public void obrisiZaposlenika(){
        Zaposlenici zaposlenik = zaposleniciTableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda brisanja");
            alert.setContentText("Jeste li sigurni da želite obrisati odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiZaposlenika(zaposlenik);
                AzurirajZivotinjuController.spremiPromjenu(zaposlenik.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Brisanje zaposlenika");
                alert2.setHeaderText("Uspješno brisanje!");
                alert2.setContentText("Zaposlenik" + zaposlenik.getIme() + " " + zaposlenik.getPrezime() + " je uspješno obrisan!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
