package com.example.projektnizadatak.Entiteti.Stanista;

import com.example.projektnizadatak.Entiteti.Entitet;

import java.time.LocalDate;
import java.time.LocalTime;

public class Obrok extends Entitet {
    private String vrstaHrane;
    private double kolicina;
    private LocalTime vrijemeObroka;
    private String napomena;

    public Obrok(Integer id, String vrstaHrane, double kolicina, LocalTime vrijemeObroka, String napomena) {
        super(id);
        this.vrstaHrane = vrstaHrane;
        this.kolicina = kolicina;
        this.vrijemeObroka = vrijemeObroka;
        this.napomena = napomena;
    }

    public String getVrstaHrane() {
        return vrstaHrane;
    }

    public void setVrstaHrane(String vrstaHrane) {
        this.vrstaHrane = vrstaHrane;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public LocalTime getVrijemeObroka() {
        return vrijemeObroka;
    }

    public void setVrijemeObroka(LocalTime vrijemeObroka) {
        this.vrijemeObroka = vrijemeObroka;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    @Override
    public String toString() {
        return vrstaHrane;
    }
}
