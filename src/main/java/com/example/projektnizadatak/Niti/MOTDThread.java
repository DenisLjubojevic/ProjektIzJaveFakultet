package com.example.projektnizadatak.Niti;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Korisnici.Role;
import com.example.projektnizadatak.MainApplication;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class MOTDThread implements Runnable{
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try{
                NotificationLogin.waitForLogin();
                Platform.runLater(() -> {
                    if (loginScreenController.roleKorisnika.equals(Role.KORISNIK)){
                        MainApplication.showAlertDialog(
                                Alert.AlertType.INFORMATION,
                                "WELCOME",
                                "DOBRODOŠLI!",
                                "Dobrodošli u naš zoološki vrt.\n" +
                                        "Želimo vam ugodano iskustvo!"
                        );
                    }else{
                        MainApplication.showAlertDialog(
                                Alert.AlertType.INFORMATION,
                                "MOTD",
                                "MESSAGE OF THE DAY!",
                                "Nema neočekivanih događanja!"
                        );
                    }
                });
            }catch (InterruptedException ex){
                Thread.currentThread().interrupt();
                System.out.println("MOTD interrupted!");
            }
        }
    }
}
