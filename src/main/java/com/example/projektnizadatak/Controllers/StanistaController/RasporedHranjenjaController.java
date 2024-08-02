package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Korisnici.Role;
import com.example.projektnizadatak.Entiteti.Stanista.RasporedHranjenja;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RasporedHranjenjaController {
    @FXML
    private Label naslovLabel;

    @FXML
    private ListView<String> rasporedListView;

    private RasporedHranjenja rasporedHranjenja;
    private List<Staniste> stanista;

    private boolean popravljenLayout = false;

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        rasporedHranjenja = new RasporedHranjenja();

        MainApplication.setupNaslov(naslovLabel);

        try{
            stanista = BazaPodataka.dohvatiSvaStanista();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje staništa!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Staniste s: stanista){
            rasporedHranjenja.dodajHranjenje(s, s.getVrijemeHranjenja());
        }

        ispisiRaspored();

        if (loginScreenController.roleKorisnika.equals(Role.TIMARITELJ)){
            startFeedingAlertTask();
        }
    }

    private void startFeedingAlertTask(){
        scheduler.scheduleAtFixedRate(this::cheeckFeedingTime, 0, 1, TimeUnit.MINUTES);
    }

    private void cheeckFeedingTime(){
        LocalTime now = LocalTime.now();
        List<Map.Entry<Staniste, LocalTime>> schedule = rasporedHranjenja.dohvatiSortiraniRaspored();
        for (Map.Entry<Staniste, LocalTime> entry: schedule){
            LocalTime feedingTime = entry.getValue();
            Duration duration = Duration.between(now, feedingTime);

            if (duration.isNegative() && duration.compareTo(Duration.ofMinutes(30)) <= 0){
                Platform.runLater(() -> {
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Hranjenje",
                            "Vrijeme hranjenja",
                            "Za manje od 30 minuta je vrijeme hranjenja\n"
                                + " za stanište " + entry.getKey().getSistematika().vrsta()
                    );
                });
            }
        }
    }

    private void ispisiRaspored(){
        rasporedListView.getItems().clear();
        List<Map.Entry<Staniste, LocalTime>> sortiraniRaspored = rasporedHranjenja.dohvatiSortiraniRaspored();

        for (Map.Entry<Staniste, LocalTime> entry: sortiraniRaspored){
            rasporedListView.getItems().add(entry.getValue() + " - Hranjenje staništa " + entry.getKey().getSistematika().vrsta());
        }
    }
}
