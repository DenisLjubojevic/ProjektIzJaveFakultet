package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    @FXML
    private TableView<Aktivnost> aktivnostiTableView;

    @FXML
    private TableColumn<Aktivnost, String> nazivTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> cijenaTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> trajanjeTableColumn;

    private String stariNaziv;
    private String staraCijena;
    private String staroTrajanje;

    List<Aktivnost> aktivnosti = new ArrayList<>();
    Aktivnost trazenaAktivnost;

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

    public void dohvatiAktivnosti(){
        Aktivnost aktivnost = aktivnostiTableView.getSelectionModel().getSelectedItem();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda izmjene");
            alert.setContentText("Jeste li sigurni da želite promjeniti odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
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
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Izmjena aktivnosti");
                alert2.setHeaderText("Uspješna izmjena!");
                alert2.setContentText("Aktivnost " + naziv + " je uspješno izmjenjena!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
