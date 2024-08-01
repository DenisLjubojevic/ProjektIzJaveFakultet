package com.example.projektnizadatak.Controllers.ZaposleniciController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private boolean popravljenLayout = false;
    @FXML
    private Button pretraziButton;
    @FXML
    private Button dodajButton;
    @FXML
    private Button urediButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private HBox hBox;

    @FXML
    private Label naslovLabel;
    @FXML
    private Label imeLabel;
    @FXML
    private Label prezimeLabel;
    @FXML
    private Label cijenaLabel;
    @FXML
    private Label satnicaLabel;
    @FXML
    private Label posaoLabel;


    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "Admin")){
            hBox.getChildren().remove(dodajButton);
            hBox.getChildren().remove(urediButton);
            hBox.getChildren().remove(obrisiButton);
        }

        try{
            zaposlenici = BazaPodataka.dohvatiSveZaposlenike();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
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

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(imeLabel);
        MainApplication.setupText(prezimeLabel);
        MainApplication.setupText(cijenaLabel);
        MainApplication.setupText(satnicaLabel);
        MainApplication.setupText(posaoLabel);

        MainApplication.setupButton(pretraziButton);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(urediButton);
        MainApplication.setupButton(obrisiButton);
    }

    public void dohvatiZaposlenike(){
        String ime = imeZaposlenikaTextField.getText();
        String prezime = prezimeZaposlenikaTextField.getText();
        String cijenapPoSatu = cijenaPoSatuZaposlenikaTextField.getText();
        String satnica = satnicaZaposlenikaTextField.getText();
        String posao = posaoZaposlenikaChoiceBox.getValue();

        List<Zaposlenici> filtriraniZaposlenici = zaposlenici;

        if(!ime.isEmpty()){
            filtriraniZaposlenici = filtriraniZaposlenici.stream()
                    .filter(z -> z.getIme().contains(ime))
                    .toList();
        }

        if (!prezime.isEmpty()) {
            filtriraniZaposlenici = filtriraniZaposlenici.stream()
                    .filter(z -> z.getPrezime().contains(prezime))
                    .toList();
        }

        if (!cijenapPoSatu.isEmpty()) {
            filtriraniZaposlenici = filtriraniZaposlenici.stream()
                    .filter(z -> z.getCijenaPoSatu().equals(Integer.parseInt(cijenapPoSatu)))
                    .toList();
        }

        if (!satnica.isEmpty()) {
            filtriraniZaposlenici = filtriraniZaposlenici.stream()
                    .filter(z -> z.getMjesecnaSatnica().equals(Integer.parseInt(satnica)))
                    .toList();
        }

        if (!posao.equals("Odabir")) {
            filtriraniZaposlenici = filtriraniZaposlenici.stream()
                    .filter(z -> z.getZanimanje().contains(posao))
                    .toList();
        }

        zaposleniciTableView.setItems(FXCollections.observableList(filtriraniZaposlenici));
    }

    public void dodajZaposlenika() throws IOException {
        IzbornikController.promjeniEkran(
                "zaposlenici/unosZaposlenika.fxml",
                "Dodaj zaposlenika");
    }

    public void azurirajZaposlenika(){
        Zaposlenici zaposlenik = zaposleniciTableView.getSelectionModel().getSelectedItem();
        if (zaposlenik != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/zaposlenici/azurirajZaposlenika.fxml"));
                Parent root = loader.load();

                AzurirajZaposlenikaController azurirajZaposlenika = loader.getController();
                azurirajZaposlenika.dohvatiZaposlenika(zaposlenik);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Uredi zaposlenika");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednog zaposlenika iz tablice."
            );
        }
    }

    public void obrisiZaposlenika(){
        Zaposlenici zaposlenik = zaposleniciTableView.getSelectionModel().getSelectedItem();
        if (zaposlenik != null){
            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda brisanja",
                        "Jeste li sigurni da želite obrisati odabranog zaposlenika?"
                );

                if(result.get() == ButtonType.OK){
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Obrisan zaposlenik",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){

                    }

                    BazaPodataka.obrisiZaposlenika(zaposlenik);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje zaposlenika",
                            "Uspješno brisanje!",
                            "Zaposlenik" + zaposlenik.getIme() + " " + zaposlenik.getPrezime() + " je uspješno obrisan!"
                    );
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednog zaposlenika iz tablice."
            );
        }

        initialize();
    }
}
