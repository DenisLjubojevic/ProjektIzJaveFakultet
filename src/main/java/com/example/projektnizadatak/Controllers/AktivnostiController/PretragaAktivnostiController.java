package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Aktivnosti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
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
    private Button dodajButton;
    @FXML
    private Button urediButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private HBox hBox;
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
    }

    public void dohvatiAktivnosti(){
        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();

        aktivnostiTableView.setItems(FXCollections.observableList(aktivnosti));
        List<Aktivnost> filtriraneAktivnosti = new ArrayList<>();
        if(!naziv.isEmpty()){
            filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getNaziv().contains(naziv)).toList();
            aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
        }

        if (!cijena.isEmpty()) {
            if(!naziv.isEmpty()){
                filtriraneAktivnosti = filtriraneAktivnosti.stream().filter(a -> a.getCijena().equals(Integer.parseInt(cijena))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }else {
                filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getCijena().equals(Integer.parseInt(cijena))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }
        }

        if (!trajanje.isEmpty()) {
            if(!naziv.isEmpty() || !cijena.isEmpty()){
                filtriraneAktivnosti = filtriraneAktivnosti.stream().filter(a -> a.getTrajanje().equals(Integer.parseInt(trajanje))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            } else {
                filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getTrajanje().equals(Integer.parseInt(trajanje))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }

        }
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
                    BazaPodataka.obrisiAktivnost(aktivnost);
                    AzurirajZivotinjuController.spremiPromjenu(aktivnost.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());

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
