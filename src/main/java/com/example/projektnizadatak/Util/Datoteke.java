package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.*;

import java.io.*;
import java.util.*;

public class Datoteke {
    public static final Integer BROJ_ZAPISA_ZIVOTINJA = 5;
    public static final Integer BROJ_ZAPISA_ZAPOSLENIKA = 6;
    public static final Integer BROJ_ZAPISA_STANISTA = 3;
    public static final Integer BROJ_ZAPISA_AKTIVNOSTI = 3;

    public static HashMap<String, String> roles = new HashMap<>();
    public static HashMap<String, String> dohvatiKorisnike() throws FileNotFoundException {
        HashMap<String, String> korisnici = new HashMap<>();
        Scanner scanner = new Scanner(new File("Datoteke/korisnici.txt"));
        while (scanner.hasNext()){
            String[] korisnickoImeILozinka = scanner.nextLine().split(",");
            korisnici.put(korisnickoImeILozinka[0], korisnickoImeILozinka[1]);
            roles.put(korisnickoImeILozinka[0], korisnickoImeILozinka[2]);
        }
        return korisnici;
    }

    public static List<Zivotinja> ucitajDatotekuZivotinje(){
        List<Zivotinja> zivotinje = new ArrayList<>();

        try (BufferedReader bufferZivotinja = new BufferedReader(new FileReader("Datoteke/zivotinjeDat.txt"))) {
            List<String> datotekaZivotinja = bufferZivotinja.lines().toList();

            for(int i = 0; i < datotekaZivotinja.size() / BROJ_ZAPISA_ZIVOTINJA; i++){
                String id = datotekaZivotinja.get(i * BROJ_ZAPISA_ZIVOTINJA);
                String vrsta = datotekaZivotinja.get(i * BROJ_ZAPISA_ZIVOTINJA + 1);
                String razred = datotekaZivotinja.get(i * BROJ_ZAPISA_ZIVOTINJA + 2);
                String starost = datotekaZivotinja.get(i * BROJ_ZAPISA_ZIVOTINJA + 3);
                String spol = datotekaZivotinja.get(i * BROJ_ZAPISA_ZIVOTINJA + 4);
                zivotinje.add(new Zivotinja(Integer.valueOf(id), new Sistematika(vrsta, razred), Integer.valueOf(starost), spol));
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return zivotinje;
    }

    public static List<Zaposlenici> ucitajDatotekuZaposlenika(){
        List<Zaposlenici> zaposlenici = new ArrayList<>();

        try (BufferedReader bufferZaposlenika = new BufferedReader(new FileReader("Datoteke/zaposleniciDat.txt"))) {
            List<String> datotekaZaposlenika = bufferZaposlenika.lines().toList();

            for(int i = 0; i < datotekaZaposlenika.size() / BROJ_ZAPISA_ZAPOSLENIKA; i++){
                String id = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA);
                String zanimanje = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA + 1);
                String cijenaPoSatu = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA + 2);
                String satnica = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA + 3);
                String prezime = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA + 4);
                String ime = datotekaZaposlenika.get(i * BROJ_ZAPISA_ZAPOSLENIKA + 5);

                zaposlenici.add(new Zaposlenici(Integer.valueOf(id), zanimanje, Integer.valueOf(cijenaPoSatu),
                        Integer.valueOf(satnica), ime, prezime));
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return zaposlenici;
    }

    public static List<Staniste> ucitajDatotekuStanista(){
        List<Staniste> stanista = new ArrayList<>();
        List<Zivotinja> zivotinje = ucitajDatotekuZivotinje();

        try (BufferedReader bufferStaniste = new BufferedReader(new FileReader("Datoteke/stanistaDat.txt"))) {
            List<String> datotekaStanista = bufferStaniste.lines().toList();

            for(int i = 0; i < datotekaStanista.size() / BROJ_ZAPISA_STANISTA; i++){
                String idZivotinja = datotekaStanista.get(i * BROJ_ZAPISA_STANISTA);
                String imaHrane = datotekaStanista.get(i * BROJ_ZAPISA_STANISTA + 1);
                String imaVode = datotekaStanista.get(i * BROJ_ZAPISA_STANISTA + 2);

                List<Zivotinja> ziovtinjeStanista = new ArrayList<>();
                if(idZivotinja.length() != 0){
                    int j = 0;
                    while(j < idZivotinja.length()){
                        ziovtinjeStanista.add(zivotinje.get(Integer.parseInt(String.valueOf(idZivotinja.charAt(j)))));
                        j += 2;
                    }
                }

                stanista.add(new Staniste(i, ziovtinjeStanista, ziovtinjeStanista.size(), Boolean.valueOf(imaHrane), Boolean.valueOf(imaVode)));
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return stanista;
    }

    public static List<Aktivnost> ucitajDatotekuAktivnosti(){
        List<Aktivnost> aktivnosti = new ArrayList<>();

        try (BufferedReader bufferAktivnosti = new BufferedReader(new FileReader("Datoteke/aktivnostiDat.txt"))) {
            List<String> datotekaAktivnosti = bufferAktivnosti.lines().toList();

            for(int i = 0; i < datotekaAktivnosti.size() / BROJ_ZAPISA_AKTIVNOSTI; i++){
                String naziv = datotekaAktivnosti.get(i * BROJ_ZAPISA_AKTIVNOSTI);
                String cijena = datotekaAktivnosti.get(i * BROJ_ZAPISA_AKTIVNOSTI + 1);
                String trajanje = datotekaAktivnosti.get(i * BROJ_ZAPISA_AKTIVNOSTI + 2);

                aktivnosti.add(new Aktivnost.Builder(i, naziv, Integer.valueOf(cijena)).saTrajanjem(Integer.valueOf(trajanje)).build());
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return aktivnosti;
    }

    public static void spremiZivotinje(List<Zivotinja> zivotinje){
        try(PrintWriter out = new PrintWriter(new FileWriter("Datoteke/zivotinjeDat.txt"))){
            int i = 0;
            do{
                out.println(zivotinje.get(i).getId());
                out.println(zivotinje.get(i).getSistematika().vrsta());
                out.println(zivotinje.get(i).getSistematika().razred());
                out.println(zivotinje.get(i).getStarost());
                out.println(zivotinje.get(i).getSpol());
                i++;
            }while (i < zivotinje.size());

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void spremiZaposlenike(List<Zaposlenici> zaposlenici){
        try(PrintWriter out = new PrintWriter(new FileWriter("Datoteke/zaposleniciDat.txt"))){
            int i = 0;
            do{
                out.println(zaposlenici.get(i).getId());
                out.println(zaposlenici.get(i).getZanimanje());
                out.println(zaposlenici.get(i).getCijenaPoSatu());
                out.println(zaposlenici.get(i).getMjesecnaSatnica());
                out.println(zaposlenici.get(i).getPrezime());
                out.println(zaposlenici.get(i).getIme());
                i++;
            }while (i < zaposlenici.size());

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void spremiStanista(List<Staniste> stanista){
        try(PrintWriter out = new PrintWriter(new FileWriter("Datoteke/stanistaDat.txt"))){
            int i = 0;
            do{
                for (Zivotinja z: stanista.get(i).getZivotinja()){
                    out.print(z.getId() + " ");
                }
                out.println("");
                out.println(stanista.get(i).getImaHrane());
                out.println(stanista.get(i).getImaVode());
                i++;
            }while (i < stanista.size());

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void spremiAktivnosti(List<Aktivnost> aktivnosti){
        try(PrintWriter out = new PrintWriter(new FileWriter("Datoteke/aktivnostiDat.txt"))){
            int i = 0;
            do{
                out.println(aktivnosti.get(i).getNaziv());
                out.println(aktivnosti.get(i).getCijena());
                out.println(aktivnosti.get(i).getTrajanje());
                i++;
            }while (i < aktivnosti.size());

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void serijalizirajSkladiste(Skladiste skladiste){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Datoteke/skladiste.dat"));
            out.writeObject(skladiste);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Skladiste> deserijalizirajSkladiste(){
        Skladiste procitanoSkladiste;
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("Datoteke/skladiste.dat"));
            procitanoSkladiste = (Skladiste) in.readObject();

            return Optional.of(procitanoSkladiste);
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }

        return Optional.empty();
    }

    public static  void serijalizirajPromjene(Promjene promjena){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Datoteke/promjene.dat"));
            out.writeObject(promjena);
            out.close();
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public static Optional<Promjene> deserijalizirajPromjene() {
        Promjene promjene;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("Datoteke/promjene.dat"));
            promjene = (Promjene) in.readObject();

            return Optional.of(promjene);
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }

        return Optional.empty();
    }
}
