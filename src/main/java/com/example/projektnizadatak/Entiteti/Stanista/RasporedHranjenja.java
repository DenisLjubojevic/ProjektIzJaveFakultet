package com.example.projektnizadatak.Entiteti.Stanista;

import java.time.LocalTime;
import java.util.*;

public class RasporedHranjenja {
    private HashMap<Staniste, LocalTime> raspored;

    public RasporedHranjenja() {
        this.raspored = new HashMap<>();
    }

    public void dodajHranjenje(Staniste staniste, LocalTime localTime){
        raspored.put(staniste, localTime);
    }

    public List<Map.Entry<Staniste, LocalTime>> dohvatiSortiraniRaspored(){
        List<Map.Entry<Staniste, LocalTime>> sortiraniRaspored = new ArrayList<>(raspored.entrySet());
        sortiraniRaspored.sort(Map.Entry.comparingByValue());
        return sortiraniRaspored;
    }

    public HashMap<Staniste, LocalTime> getRaspored() {
        return raspored;
    }
}
