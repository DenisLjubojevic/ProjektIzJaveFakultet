package com.example.projektnizadatak.Entiteti.Stanista;

import com.example.projektnizadatak.Entiteti.Entitet;

import java.time.LocalTime;

public class Hrana extends Entitet {
    private String vrstaHrane;
    private double kolicina;
    private LocalTime vrijemeHranjenja;
    private String napomena;

    public Hrana(Integer id, String vrstaHrane, double kolicina, LocalTime vrijemeHranjenja, String napomena) {
        super(id);
        this.vrstaHrane = vrstaHrane;
        this.kolicina = kolicina;
        this.vrijemeHranjenja = vrijemeHranjenja;
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

    public LocalTime getVrijemeHranjenja() {
        return vrijemeHranjenja;
    }

    public void setVrijemeHranjenja(LocalTime vrijemeHranjenja) {
        this.vrijemeHranjenja = vrijemeHranjenja;
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
