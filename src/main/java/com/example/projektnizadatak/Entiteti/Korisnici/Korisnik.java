package com.example.projektnizadatak.Entiteti.Korisnici;

import com.example.projektnizadatak.Entiteti.Entitet;

public class Korisnik extends Entitet {
    private String korisnickoIme;
    private String lozinka;
    private Integer role;

    public Korisnik(Integer id, String korisnickoIme, String lozinka, Integer role) {
        super(id);
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
