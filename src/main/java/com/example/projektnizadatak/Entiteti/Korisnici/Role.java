package com.example.projektnizadatak.Entiteti.Korisnici;

public enum Role {
    ADMIN("ADMIN"),
    VODITELJ("VODITELJ"),
    VETERINAR("VETERINAR"),
    TIMARITELJ("TIMARITELJ"),
    KORISNIK("KORISNIK");

    private String rolaKorisnika;

    Role(String rola){
        this.rolaKorisnika = rola;
    }

    public String getRolaKorisnika() {
        return rolaKorisnika;
    }

    public void setRolaKorisnika(String rolaKorisnika) {
        this.rolaKorisnika = rolaKorisnika;
    }
}
