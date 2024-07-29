package com.example.projektnizadatak.Entiteti.Stanista;

import com.example.projektnizadatak.Entiteti.Entitet;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;

import java.util.List;

public class Staniste extends Entitet {
    private List<Zivotinja> zivotinja;

    private Sistematika sistematika;

    private Integer brojJedinki;
    private Hrana hrana;
    private byte[] slikaStanista;

    public Staniste(Integer id, List<Zivotinja> zivotinja, Integer brojJedniki, Hrana hrana, byte[] slikaStanista) {
        super(id);
        this.zivotinja = zivotinja;
        this.brojJedinki = brojJedniki;
        this.sistematika = new Sistematika(zivotinja.get(0).getSistematika().vrsta(), zivotinja.get(0).getSistematika().razred());
        this.hrana = hrana;
        this.slikaStanista = slikaStanista;
    }

    public Staniste(Integer id, List<Zivotinja> zivotinja, Integer brojJedniki, Hrana hrana) {
        super(id);
        this.zivotinja = zivotinja;
        this.brojJedinki = brojJedniki;
        this.sistematika = new Sistematika(zivotinja.get(0).getSistematika().vrsta(), zivotinja.get(0).getSistematika().razred());
        this.hrana = hrana;
    }

    public List<Zivotinja> getZivotinja() {
        return zivotinja;
    }

    public void setZivotinja(List<Zivotinja> zivotinja) {
        this.zivotinja = zivotinja;
    }

    public Sistematika getSistematika() {
        return sistematika;
    }

    public void setSistematika(Sistematika sistematika) {
        this.sistematika = sistematika;
    }

    public Integer getBrojJedinki() {
        return brojJedinki;
    }

    public void setBrojJedinki(Integer brojJedinki) {
        this.brojJedinki = brojJedinki;
    }

    public Hrana getHrana() {
        return hrana;
    }

    public void setHrana(Hrana hrana) {
        this.hrana = hrana;
    }

    public byte[] getSlikaStanista() {
        return slikaStanista;
    }

    public void setSlikaStanista(byte[] slikaStanista) {
        this.slikaStanista = slikaStanista;
    }
}
