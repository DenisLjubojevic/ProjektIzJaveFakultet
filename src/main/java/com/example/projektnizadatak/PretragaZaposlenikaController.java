package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Zaposlenici;
import com.example.projektnizadatak.Entiteti.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PretragaZaposlenikaController {

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
        cijenaPoSatuZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijenaPoSatu().toString()));
        satnicaZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMjesecnaSatnica().toString()));
        posaoZaposlenikaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZanimanje()));

        zaposleniciTableView.setItems(FXCollections.observableList(zaposlenici));
    }

    public void dohvatiZaposlenike(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenapPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();

        List<Zaposlenici> filtriraniZaposlenici = new ArrayList<>();
        zaposleniciTableView.setItems(FXCollections.observableList(zaposlenici));

        if(ime.length() != 0 && posao.equals("Odabir")){
            filtriraniZaposlenici = zaposlenici.stream().filter(z -> z.getIme().contains(ime)).toList();
            zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
        }

        if (prezime.length() != 0 && posao.equals("Odabir")) {
            if(ime.length() != 0){
                filtriraniZaposlenici = filtriraniZaposlenici.stream().filter(z -> z.getPrezime().contains(prezime)).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            }else {
                filtriraniZaposlenici = zaposlenici.stream().filter(z -> z.getPrezime().contains(prezime)).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            }
        }

        if (cijenapPoSatu.length() != 0 && posao.equals("Odabir")) {
            if(ime.length() != 0 || prezime.length() != 0){
                filtriraniZaposlenici = filtriraniZaposlenici.stream().filter(z -> z.getCijenaPoSatu().equals(Integer.parseInt(cijenapPoSatu))).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            } else {
                filtriraniZaposlenici = zaposlenici.stream().filter(z -> z.getCijenaPoSatu().equals(Integer.parseInt(cijenapPoSatu))).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            }
        }

        if (satnica.length() != 0 && posao.equals("Odabir")) {
            if(ime.length() != 0 || prezime.length() != 0 || cijenapPoSatu.length() != 0){
                filtriraniZaposlenici = filtriraniZaposlenici.stream().filter(z -> z.getMjesecnaSatnica().equals(Integer.parseInt(satnica))).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            } else {
                filtriraniZaposlenici = zaposlenici.stream().filter(z -> z.getMjesecnaSatnica().equals(Integer.parseInt(satnica))).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            }
        }

        if (!posao.isBlank() && !posao.equals("Odabir")) {
            if(ime.length() != 0 || prezime.length() != 0 || cijenapPoSatu.length() != 0 || satnica.length() != 0){
                filtriraniZaposlenici = filtriraniZaposlenici.stream().filter(z -> z.getZanimanje().contains(posao)).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            } else {
                filtriraniZaposlenici = zaposlenici.stream().filter(z -> z.getZanimanje().contains(posao)).toList();
                zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
            }
        }
    }
}
