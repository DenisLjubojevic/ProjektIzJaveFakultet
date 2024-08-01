package com.example.projektnizadatak.Controllers.HranaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PretragaHraneController {
    @FXML
    private TextField vrstaHraneTextField;
    @FXML
    private TextField kolicinaTextField;
    @FXML
    private TableView<Hrana> hranaTableView;
    @FXML
    private TableColumn<Hrana, String> vrstaHraneTableColumn;
    @FXML
    private TableColumn<Hrana, String> kolicinaTableColumn;
    @FXML
    private TableColumn<Hrana, String> napomenaTableColumn;
    @FXML
    private Button pretraziButton;
    @FXML
    private javafx.scene.control.Button dodajButton;
    @FXML
    private javafx.scene.control.Button urediButton;
    @FXML
    private javafx.scene.control.Button obrisiButton;
    @FXML
    private HBox hBox;
    @FXML
    private Label naslovLabel;
    @FXML
    private Label vrstaLabel;
    @FXML
    private Label kolicinaLabel;

    List<Hrana> hrane = new ArrayList<>();
    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "Admin") &&
                !Objects.equals(loginScreenController.roleKorisnika, "Veterinar")){
            hBox.getChildren().remove(dodajButton);
            hBox.getChildren().remove(urediButton);
            hBox.getChildren().remove(obrisiButton);
        }

        try{
            hrane = BazaPodataka.dohvatiSvuHranu();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje hrane!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        vrstaHraneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVrstaHrane())));
        kolicinaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKolicina())));
        napomenaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNapomena())));

        hranaTableView.setItems(FXCollections.observableList(hrane));

        hranaTableView.setRowFactory(tv -> {
            TableRow<Hrana> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()){
                    Hrana rowData = row.getItem();
                    try {
                        prikaziDetaljeHrane(rowData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(kolicinaLabel);

        MainApplication.setupButton(pretraziButton);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(urediButton);
        MainApplication.setupButton(obrisiButton);
    }

    private void prikaziDetaljeHrane(Hrana hrana) throws IOException {
        if (loginScreenController.roleKorisnika.equals("admin")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/hrana/detaljiHrane.fxml"));
                Parent root = loader.load();

                DetaljiHraneController detaljiHrane = loader.getController();
                detaljiHrane.prikaziDetalje(hrana);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalji hrane");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.WARNING,
                    "Odbijeno",
                    "Pristup odbijen",
                    "Nemate pravo pristupa tim podacima!"
            );
        }
    }

    public void dohvatiHranu(){
        String vrstaHrane = vrstaHraneTextField.getText();
        String kolicina = kolicinaTextField.getText();

        List<Hrana> filtriranaHrana = hrane;

        if (!vrstaHrane.isEmpty()){
            filtriranaHrana = filtriranaHrana.stream()
                    .filter(h -> h.getVrstaHrane().contains(vrstaHrane))
                    .toList();
        }

        if (!kolicina.isEmpty()) {
            try {
                double kolicinaDouble = Double.parseDouble(kolicina);
                filtriranaHrana = filtriranaHrana.stream()
                        .filter(h -> h.getKolicina() == kolicinaDouble)
                        .toList();
            } catch (NumberFormatException e) {
                return;
            }
        }

        hranaTableView.setItems(FXCollections.observableList(filtriranaHrana));
    }

    public void dodajHranu() throws IOException {
        IzbornikController.promjeniEkran(
                "hrana/unosHrane.fxml",
                "Dodaj hranu");
    }

    public void azurirajHranu() throws IOException {
        Hrana hrana = hranaTableView.getSelectionModel().getSelectedItem();
        if (hrana != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/hrana/azurirajHranu.fxml"));
            Parent root = loader.load();

            AzurirajHranuController azurirajHranu = loader.getController();
            azurirajHranu.dohvatiHranu(hrana);

            Stage stage = MainApplication.getMainStage();
            stage.setScene(new Scene(root));
            stage.setTitle("Promijeni hranu");
            stage.show();
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu hranu iz tablice."
            );
        }
    }

    public synchronized void obrisiHranu() throws BazaPodatakaException {
        Hrana hrana = hranaTableView.getSelectionModel().getSelectedItem();

        if (hrana != null){
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda brisanja",
                    "Jeste li sigurni da želite obrisati odabranu hranu?"
            );

            if (result.get().equals(ButtonType.OK)){
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Obrisana hrana",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){
                    System.out.println("Greška: " + ex);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.ERROR,
                            "Spremanje promjene!",
                            "Pogreška spremanja!",
                            ex.getMessage()
                    );
                }

                BazaPodataka.obrisiHranu(hrana);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Brisanje hrane",
                        "Uspješno brisanje!",
                        "Hrana " + hrana.getVrstaHrane() + " je uspješno obrisana!"
                );
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu hranu iz tablice."
            );
        }
        initialize();
    }
}
