package com.example.projektnizadatak.Entiteti.Zaposlenici;

import java.time.LocalTime;

public enum Smjena {
    JUTARNJA(LocalTime.of(6, 0), LocalTime.of(14, 0)),
    POPODNEVNA(LocalTime.of(14,0), LocalTime.of(22, 0));

    private final LocalTime pocetakRada;
    private final LocalTime krajRada;

    Smjena(LocalTime pocetakRada, LocalTime krajRada){
        this.pocetakRada = pocetakRada;
        this.krajRada = krajRada;
    }

    public LocalTime getPocetak() {
        return pocetakRada;
    }

    public LocalTime getKraj() {
        return krajRada;
    }

    public String ispisiSmjenu(){
        return pocetakRada.toString() + " - " + krajRada.toString();
    }
}
