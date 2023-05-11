package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AzurirajZaposlenikaController {
    @FXML
    private TextField imeZaposlenikaTextField;

    @FXML
    private TextField prezimeZaposlenikaTextField;

    @FXML
    private TextField cijenaPoSatuZaposlenikaTextField;

    @FXML
    private TextField satnicaZaposlenikaTextField;

    @FXML
    private ChoiceBox<String> posaoZaposlenikaChoiceBox;

    @FXML
    private TableView<Zaposlenici> zaposleniciTableView;

    @FXML
    private TableColumn<Zaposlenici, String> imeZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> prezimeZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> cijneaPoSatuZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> satnicaZaposlenikaTableColumn;

    @FXML
    private TableColumn<Zaposlenici, String> posaoZaposlenikaTableColumn;

    private String staroIme;
    private String staroPrezime;
    private String staraCijenaPoSatu;
    private String staraSatnica;
    private String stariPosao;


    private List<Zaposlenici> zaposlenici;
    private Zaposlenici trazeniZaposlenik;

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

        posaoZaposlenikaChoiceBox.getItems().add("Odabir");
        for (Zaposlenici z: zaposlenici) {
            if (posaoZaposlenikaChoiceBox.getItems().contains(z.getZanimanje())){
                continue;
            }
            posaoZaposlenikaChoiceBox.getItems().add(z.getZanimanje());
        }

        posaoZaposlenikaChoiceBox.getSelectionModel().selectFirst();

        imeZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));
        cijneaPoSatuZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijenaPoSatu().toString()));
        satnicaZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMjesecnaSatnica().toString()));
        posaoZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZanimanje()));

        zaposleniciTableView.setItems(FXCollections.observableList(zaposlenici));
    }

    public void dohvatiZaposlenika(){
        Zaposlenici zaposlenik = zaposleniciTableView.getSelectionModel().getSelectedItem();
        imeZaposlenikaTextField.setText(zaposlenik.getIme());
        prezimeZaposlenikaTextField.setText(zaposlenik.getPrezime());
        cijenaPoSatuZaposlenikaTextField.setText(zaposlenik.getCijenaPoSatu().toString());
        satnicaZaposlenikaTextField.setText(zaposlenik.getMjesecnaSatnica().toString());
        posaoZaposlenikaChoiceBox.getSelectionModel().select(zaposlenik.getZanimanje());

        staroIme = zaposlenik.getIme();
        staroPrezime = zaposlenik.getPrezime();
        staraCijenaPoSatu = zaposlenik.getCijenaPoSatu().toString();
        staraSatnica = zaposlenik.getMjesecnaSatnica().toString();
        stariPosao = posaoZaposlenikaChoiceBox.getValue();

        trazeniZaposlenik = zaposlenik;
    }

    public void izmjeniZaposlenika(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenaPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();

        trazeniZaposlenik.setIme(ime);
        trazeniZaposlenik.setPrezime(prezime);
        trazeniZaposlenik.setCijenaPoSatu(Integer.valueOf(cijenaPoSatu));
        trazeniZaposlenik.setMjesecnaSatnica(Integer.valueOf(satnica));
        trazeniZaposlenik.setZanimanje(posao);

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda izmjene");
            alert.setContentText("Jeste li sigurni da želite promjeniti odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                if(!staroIme.equals(ime)){
                    AzurirajZivotinjuController.spremiPromjenu(staroIme, ime, "admin", LocalDateTime.now());
                }

                if(!staroPrezime.equals(prezime)){
                    AzurirajZivotinjuController.spremiPromjenu(staroPrezime, prezime, "admin", LocalDateTime.now());
                }

                if(!staraCijenaPoSatu.equals(cijenaPoSatu)){
                    AzurirajZivotinjuController.spremiPromjenu(staraCijenaPoSatu, cijenaPoSatu, "admin", LocalDateTime.now());
                }

                if(!staraSatnica.equals(satnica)){
                    AzurirajZivotinjuController.spremiPromjenu(staraSatnica, satnica, "admin", LocalDateTime.now());
                }

                if(!stariPosao.equals(posao)){
                    AzurirajZivotinjuController.spremiPromjenu(stariPosao, posao, "admin", LocalDateTime.now());
                }

                BazaPodataka.azurirajZaposlenika(trazeniZaposlenik);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Izmjena zaposlenika");
                alert2.setHeaderText("Uspješna izmjena!");
                alert2.setContentText("Zaposlenik " + ime + " " + prezime + " je uspješno izmjenjen!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
