package com.example.projektnizadatak.Entiteti;

import java.io.Serializable;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Promjene implements Serializable {
    private Map<Integer, String> staraVrijednost;
    private Map<Integer, String> novaVrijednost;
    private Map<Integer, String> role;
    private Map<Integer, LocalDateTime> datumIVrijeme;

    public Promjene(Map<Integer, String> staraVrijednost, Map<Integer, String> novaVrijednost, Map<Integer, String> role, Map<Integer, LocalDateTime> datumIVrijeme) {
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.role = role;
        this.datumIVrijeme = datumIVrijeme;
    }

    public Map<Integer, String> getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String vrijednost) {
        this.staraVrijednost.put(staraVrijednost.size() + 1, vrijednost);
    }

    public Map<Integer, String> getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String vrijednost) {
        this.novaVrijednost.put(novaVrijednost.size() + 1, vrijednost);
    }

    public Map<Integer, String> getRole() {
        return role;
    }

    public void setRole(String vrijednost) {
        this.role.put(role.size() + 1, vrijednost);
    }

    public Map<Integer, String> getDatumIVrijeme() {
        Map<Integer, String> novaMapa = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        for (Integer key: datumIVrijeme.keySet()) {
            novaMapa.put(key, datumIVrijeme.get(key).format(formatter));
        }
        return novaMapa;
    }

    public void setDatumIVrijeme(LocalDateTime vrijednost) {
        this.datumIVrijeme.put(datumIVrijeme.size() + 1, vrijednost);
    }
}
