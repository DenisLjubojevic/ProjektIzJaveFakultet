package com.example.projektnizadatak.Entiteti;

public class Korisnik{
    private String korisnickoIme;
    private String lozinka;

    public Korisnik(String korisnickoIme, String lozinka) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }
}
