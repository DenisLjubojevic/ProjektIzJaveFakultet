package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnosti.Aktivnost;
import com.example.projektnizadatak.Entiteti.Promjene;
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

public class PretragaAktivnostiController {
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

    List<Aktivnost> aktivnosti = new ArrayList<>();

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
    private Label nazivLabel;
    @FXML
    private Label cijenaLabel;
    @FXML
    private Label trajanjeLabel;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "admin")){
            hBox.getChildren().remove(dodajButton);
            hBox.getChildren().remove(urediButton);
            hBox.getChildren().remove(obrisiButton);
        }

        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje aktivnosti!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaziv()));
        cijenaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijena().toString()));
        trajanjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTrajanje().toString()));

        aktivnostiTableView.setItems(FXCollections.observableList(aktivnosti));

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(nazivLabel);
        MainApplication.setupText(cijenaLabel);
        MainApplication.setupText(trajanjeLabel);

        MainApplication.setupButton(pretraziButton);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(urediButton);
        MainApplication.setupButton(obrisiButton);
    }

    public void dohvatiAktivnosti(){
        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();

        List<Aktivnost> filtriraneAktivnosti = aktivnosti;
        if(!naziv.isEmpty()){
            filtriraneAktivnosti = filtriraneAktivnosti.stream()
                    .filter(a -> a.getNaziv().contains(naziv))
                    .toList();
        }

        if (!cijena.isEmpty()) {
            filtriraneAktivnosti = filtriraneAktivnosti.stream()
                    .filter(a -> a.getCijena().equals(Integer.parseInt(cijena)))
                    .toList();
        }

        if (!trajanje.isEmpty()) {
            filtriraneAktivnosti = filtriraneAktivnosti.stream()
                    .filter(a -> a.getTrajanje().equals(Integer.parseInt(trajanje)))
                    .toList();
        }

        aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
    }

    public void dodajAktivnost() throws IOException {
        IzbornikController.promjeniEkran(
                "aktivnosti/unosAktivnosti.fxml",
                "Dodaj aktivnost");
    }

    public void azurirajAktivnost(){
        Aktivnost aktivnost = aktivnostiTableView.getSelectionModel().getSelectedItem();
        if (aktivnost != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/aktivnosti/azurirajAktivnosti.fxml"));
                Parent root = loader.load();

                AzurirajAktivnostiController azurirajAktivnost = loader.getController();
                azurirajAktivnost.dohvatiAktivnosti(aktivnost);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Uredi aktivnost");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu aktivnost iz tablice."
            );
        }
    }

    public void obrisiAktivnost(){
        Aktivnost aktivnost = aktivnostiTableView.getSelectionModel().getSelectedItem();
        if (aktivnost != null){
            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda brisanja",
                        "Jeste li sigurni da želite obrisati odabranu aktivnost?"
                );

                if(result.get() == ButtonType.OK){
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Obrisana aktivnost",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){

                    }

                    BazaPodataka.obrisiAktivnost(aktivnost);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje aktivnosti",
                            "Uspješno brisanje!",
                            "Aktivnost " + aktivnost.getNaziv() + " je uspješno obrisana!"
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
                    "Trebate odabrati jednu aktivnost iz tablice."
            );
        }
        initialize();
    }
}
