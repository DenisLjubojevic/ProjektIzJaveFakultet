package com.example.projektnizadatak.Main;

import com.example.projektnizadatak.Entiteti.*;

import java.time.LocalDate;
import java.util.*;

import com.example.projektnizadatak.Iznimke.NeispravniIntException;
import com.example.projektnizadatak.Iznimke.NeispravniSpolException;
import com.example.projektnizadatak.Util.Datoteke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner unos = new Scanner(System.in);

        List<Zivotinja> zivotinje = ucitajZivotinje(unos);
        List<Zaposlenici> zaposlenici = ucitajZaposlenike(unos);
        List<Staniste> stanista = ucitajStanista(unos, zivotinje);
        List<Aktivnost> aktivnosti = ucitajAktivnosti(unos);

        Skladiste skladiste = ucitajSkladiste(unos);

        Map<String, Integer> mapaAktivnosti = new HashMap<>();

        for (Aktivnost a: aktivnosti) {
            mapaAktivnosti.put(a.getNaziv(), a.getCijena());
        }

        List<Zivotinja> zivotinjeZaNaruciti = new ArrayList<>();
        zivotinjeZaNaruciti.add(zivotinje.get(2));

        List<Hrana> hranaZaNaruciti = new ArrayList<>();
        hranaZaNaruciti.add(new Hrana("Meso", 100));

        Narucivanje<Zivotinja> naruceneZivotinje = new Narucivanje<>(zivotinjeZaNaruciti, 250, LocalDate.now());
        Narucivanje<Hrana> naruceneHrane = new Narucivanje<>(hranaZaNaruciti, 30, LocalDate.now());

        System.out.println("Cijenik");
        for(String key: mapaAktivnosti.keySet()){
            System.out.println(key + ": " + mapaAktivnosti.get(key) + " Eura");
        }

        System.out.println("Naručene životinje:");
        for (Zivotinja z: naruceneZivotinje.dohvatiNaruceno()) {
            System.out.println(z.getSistematika().vrsta());
        }

        Parovi<Zaposlenici, Zivotinja> njegovatelj = new Parovi<>(zaposlenici.get(2), zivotinje.get(1));
        njegovatelj.ispisParova();

        Datoteke.spremiZivotinje(zivotinje);
        Datoteke.spremiZaposlenike(zaposlenici);
        Datoteke.spremiStanista(stanista);
        Datoteke.spremiAktivnosti(aktivnosti);

        Datoteke.serijalizirajSkladiste(skladiste);
    }

    static List<Zivotinja> ucitajZivotinje(Scanner unos){
        List<Zivotinja> zivotinjaList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            System.out.println("Unesite " + (i + 1) + ". životinju:");
            System.out.print("Unesite vrstu životinje: ");
            String vrsta = unos.nextLine();

            System.out.print("Unesite razred životinje: ");
            String razred = unos.nextLine();


            Integer starost = null;
            Boolean neispravniUnos = false;
            do{
                try{
                    neispravniUnos = false;
                    starost = ucitajStarost(unos);
                }catch (NeispravniIntException e){
                    logger.info("Pogrešan unos exception! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                    unos.nextLine();
                }
            }while (neispravniUnos);

            unos.nextLine();

            String spol = null;
            neispravniUnos = false;
            do{
                try{
                    neispravniUnos = false;
                    spol = ucitajSpol(unos);
                }catch (NeispravniSpolException e){
                    logger.info("Pogrešan unos exception! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                }
            }while (neispravniUnos);

            zivotinjaList.add(new Zivotinja(i, new Sistematika(vrsta, razred), starost, spol));
        }

        return zivotinjaList;
    }

    private static Integer ucitajStarost(Scanner unos) throws NeispravniIntException{
            Integer starost;
            System.out.print("Unesite starost životinje: ");

            try{
                starost = unos.nextInt();
                if (starost < 0 || starost > 100){
                    throw new NeispravniIntException("Starost životinje ne može biti manja od 0 i veća od 100!");
                }
            } catch (InputMismatchException ex){
                String message = "Pogrešan unos! Unos mora sadržavati brojeve!";
                throw new NeispravniIntException(message);
            }

            return starost;
    }

    private static String ucitajSpol (Scanner unos) throws NeispravniSpolException{
        String spol;
        System.out.print("Unesite spol životinje (Musko / Zensko): ");

        try{
            spol = unos.nextLine();
            if (!spol.equals("Musko") && !spol.equals("Zensko")){
                throw new NeispravniSpolException("Morate unjeti jedan od traženih spolova u zagradi!");
            }
        } catch (InputMismatchException ex){
            String message = "Pogrešan unos! Unos mora sadržavati slova!";
            throw new NeispravniSpolException(message);
        }

        return spol;

    }

    static List<Zaposlenici> ucitajZaposlenike(Scanner unos){
        List<Zaposlenici> zaposleniciList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            System.out.println("Unesite " + (i + 1) + ". zaposlenika:");
            System.out.print("Unesite ime zaposlenika: ");
            String ime = unos.nextLine();

            System.out.print("Unesite prezime zaposlenika: ");
            String prezime = unos.nextLine();

            System.out.print("Unesite zanimanje zaposlenika: ");
            String zanimanje = unos.nextLine();

            Integer cijenaPoSatu = null;
            Boolean neispravniUnos = false;
            do{
                try{
                    neispravniUnos = false;
                    cijenaPoSatu = ucitajCijenu(unos);
                }catch (NeispravniIntException e){
                    logger.info("Pogrešan unos exception! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                    unos.nextLine();
                }
            }while (neispravniUnos);

            unos.nextLine();

            Integer satnica = null;
            neispravniUnos = false;
            do{
                try{
                    neispravniUnos = false;
                    satnica = ucitajSatnicu(unos);
                }catch (NeispravniIntException e){
                    logger.info("Pogrešan unos exception! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                    unos.nextLine();
                }
            }while (neispravniUnos);

            unos.nextLine();

            zaposleniciList.add(new Zaposlenici(i, zanimanje, cijenaPoSatu, satnica, ime, prezime));
        }

        return zaposleniciList;
    }

    private static Integer ucitajCijenu(Scanner unos) throws NeispravniIntException {
        Integer cijena;
        System.out.print("Unesite cijenu po satu zaposlenika (Euro): ");

        try{
            cijena = unos.nextInt();
            if (cijena < 0 || cijena > 10){
                throw new NeispravniIntException("Cijena ne smije biti manja od 0 Eura i veća od 10 Eura!");
            }
        } catch (InputMismatchException ex){
            String message = "Pogrešan unos! Unos mora sadržavati brojeve!";
            throw new NeispravniIntException(message);
        }

        return cijena;
    }

    private static Integer ucitajSatnicu(Scanner unos) throws NeispravniIntException{
        Integer satnica;
        System.out.print("Unesite mjesečnu satnicu zaposlenika: ");

        try{
            satnica = unos.nextInt();
            if (satnica < 180 || satnica > 200){
                throw new NeispravniIntException("Satnica ne može biti manja od 180 sati i veća od 200 sati!");
            }
        } catch (InputMismatchException ex){
            String message = "Pogrešan unos! Unos mora sadržavati brojeve!";
            throw new NeispravniIntException(message);
        }

        return satnica;
    }

    static List<Staniste> ucitajStanista(Scanner unos, List<Zivotinja> zivotinje){
        List<Staniste> stanisteList = new ArrayList<>();
        List<Integer> zauzeteZivotinje = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            List<Zivotinja> zivotinjeStanista = new ArrayList<>();
            System.out.println("Unesite " + (i + 1) + ". stanište:");
            for (int k = 0; k < 2; k++){
                System.out.println("Odaberite " + (k + 1) + ". životinju: ");
                for (int j = 0; j < zivotinje.size(); j++) {
                    System.out.println((j + 1) + ". " + zivotinje.get(j).getSistematika().vrsta());
                }

                Integer odabirZivotinje = null;
                Boolean neispravniUnos = false;
                do{
                    try{
                        odabirZivotinje = odabirZivotinje(unos, zivotinje.size(), zauzeteZivotinje);
                        neispravniUnos = false;
                    }catch (NeispravniIntException e){
                        logger.info("Pogrešan unos! - " + e.getMessage());
                        System.out.println(e.getMessage());
                        neispravniUnos = true;
                        unos.nextLine();
                    }

                }while (neispravniUnos);

                unos.nextLine();

                zivotinjeStanista.add(zivotinje.get(odabirZivotinje - 1));
            }

            stanisteList.add(new Staniste(i, zivotinjeStanista, zivotinjeStanista.size(),false, false));
        }

        return stanisteList;
    }

    private static Integer odabirZivotinje(Scanner unos, Integer brojZivotinja, List<Integer> zauzeteZivotinje) throws NeispravniIntException{
        Integer odabir;
        System.out.print("Odabir >> ");
        try{
            odabir = unos.nextInt();
            if(odabir < 1 || odabir > brojZivotinja){
                throw new NeispravniIntException("Morate odabrati postojeći redni broj životinje!");
            }

            for (int i = 0; i <zauzeteZivotinje.size(); i++){
                if(zauzeteZivotinje.get(i).equals(odabir)){
                    throw new NeispravniIntException("Odaberite životinju koja nema niti jedno stanište!");
                }
            }
            zauzeteZivotinje.add(odabir);
        }
        catch (InputMismatchException ex){
            String message = "Pogrešan unos! Pokušajte ponovo!";
            throw new NeispravniIntException(message);
        }

        return odabir;
    }

    static List<Aktivnost> ucitajAktivnosti(Scanner unos){
        List<Aktivnost> aktivnostsList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            System.out.println("Unesite " + (i + 1) + ". aktivnost:");
            System.out.print("Unesite naziv aktivnosti: ");
            String naziv = unos.nextLine();

            Integer cijena = null;
            Boolean neispravniUnos = false;
            do{
                try{
                    cijena = unosCijeneAktivnosti(unos);
                    neispravniUnos = false;
                }catch (NeispravniIntException e){
                    logger.info("Pogrešan unos! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                    unos.nextLine();
                }

            }while (neispravniUnos);

            unos.nextLine();

            Integer trajanje = null;
            neispravniUnos = false;
            do{
                try{
                    trajanje = trajanjeAktivnosti(unos);
                    neispravniUnos = false;
                }catch (NeispravniIntException e){
                    logger.info("Pogrešan unos! - " + e.getMessage());
                    System.out.println(e.getMessage());
                    neispravniUnos = true;
                    unos.nextLine();
                }

            }while (neispravniUnos);

            unos.nextLine();

            aktivnostsList.add(new Aktivnost.Builder(0, naziv, cijena).saTrajanjem(trajanje).build());
        }

        return  aktivnostsList;
    }

    private static Integer unosCijeneAktivnosti(Scanner unos) throws NeispravniIntException{
        Integer cijena;
        System.out.print("Unesite cijenu aktivnosti (Euro): ");
        try{
            cijena = unos.nextInt();
            if(cijena < 0 || cijena > 15){
                throw new NeispravniIntException("Cijena ne smije biti manja od 0 Eura i veća od 5 Eura!");
            }
        }
        catch (InputMismatchException ex){
            String message = "Pogrešan unos! Pokušajte ponovo!";
            throw new NeispravniIntException(message);
        }

        return cijena;
    }

    private static Integer trajanjeAktivnosti(Scanner unos) throws NeispravniIntException{
        Integer trajanje;
        System.out.print("Unesite trajanje aktivnosti (min): ");
        try{
            trajanje = unos.nextInt();
            if(trajanje < 5 || trajanje > 30){
                throw new NeispravniIntException("Aktivnost ne smije biti kraća od 5 min i duža od 30 min!");
            }
        }
        catch (InputMismatchException ex){
            String message = "Pogrešan unos! Pokušajte ponovo!";
            throw new NeispravniIntException(message);
        }

        return trajanje;
    }

    static Skladiste ucitajSkladiste(Scanner unos){
        Set<Hrana> hranaSet = new HashSet<>();
        System.out.print("Unesite veličinu skladišta: ");
        Integer velicina = unos.nextInt();

        unos.nextLine();

        for(int i = 0; i < velicina; i++){
            System.out.print("Unesite naziv hrane u skladištu: ");
            String hranaNaziv = unos.nextLine();
            System.out.print("Unesite kolicinu te hrane u skladištu: ");
            Integer hranaKolicina = unos.nextInt();
            unos.nextLine();

            hranaSet.add(new Hrana(hranaNaziv, hranaKolicina));
        }

        return new Skladiste(hranaSet, velicina);
    }

}
