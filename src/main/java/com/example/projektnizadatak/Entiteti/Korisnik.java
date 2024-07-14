package com.example.projektnizadatak.Entiteti;

public class Korisnik{
    private String korisnickoIme;
    private String lozinka;
    private Integer role;

    public Korisnik(String korisnickoIme, String lozinka, Integer role) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.role = role;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public Integer getRole(){ return role; }
}
