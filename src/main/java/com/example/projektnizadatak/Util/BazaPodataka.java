package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.*;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;

import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class BazaPodataka {

    private static Connection connectToDataBase() throws Exception{
        Properties konfiguracijaBaze = new Properties();
        konfiguracijaBaze.load(new FileInputStream("Datoteke/bazaPodataka.properties"));
        Connection con = DriverManager.getConnection(
                konfiguracijaBaze.getProperty("bazaPodatakaURL"),
                konfiguracijaBaze.getProperty("korisnickoIme"),
                konfiguracijaBaze.getProperty("lozinka"));

        return con;
    }

    public static List<Zivotinja> dohvatiSveZivotinje()throws BazaPodatakaException{
        List<Zivotinja> zivotinje = new ArrayList<>();
        try{
            Connection con = connectToDataBase();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM ZIVOTINJA");

            while (rs.next()){
                Integer id = rs.getInt("id");
                String vrsta = rs.getString("vrsta");
                String razred = rs.getString("razred");
                Integer starost = rs.getInt("starost");
                String spol = rs.getString("spol");

                zivotinje.add(new Zivotinja(id, new Sistematika(vrsta, razred), starost, spol));
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return zivotinje;
    }

    public static Optional<Zivotinja> dohvatiZivotinju(Integer id) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ZIVOTINJA WHERE ID = ?");
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String vrsta = rs.getString("vrsta");
                String razred = rs.getString("razred");
                Integer starost = rs.getInt("starost");
                String spol = rs.getString("spol");

                Zivotinja novaZivotinja = new Zivotinja(id2, new Sistematika(vrsta, razred), starost, spol);

                return Optional.of(novaZivotinja);
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return Optional.empty();
    }

    public static void spremiZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO ZIVOTINJA(vrsta, razred, starost, spol)"
                    + "VALUES (?, ?, ?, ?)");
            pstmt.setString(1, zivotinja.getSistematika().vrsta());
            pstmt.setString(2, zivotinja.getSistematika().razred());
            pstmt.setInt(3, zivotinja.getStarost());
            pstmt.setString(4, zivotinja.getSpol());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void azurirajZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("UPDATE ZIVOTINJA SET " +
                    "VRSTA = ?,"+
                    "RAZRED = ?,"+
                    "STAROST = ?,"+
                    "SPOL = ? " +
                    "WHERE ID = ?");
            pstmt.setString(1, zivotinja.getSistematika().vrsta());
            pstmt.setString(2, zivotinja.getSistematika().razred());
            pstmt.setInt(3, zivotinja.getStarost());
            pstmt.setString(4, zivotinja.getSpol());
            pstmt.setInt(5, zivotinja.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void obrisiZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM ZIVOTINJA WHERE ID = ?");
            pstmt.setInt(1, zivotinja.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static List<Zaposlenici> dohvatiSveZaposlenike()throws BazaPodatakaException{
        List<Zaposlenici> zaposlenici = new ArrayList<>();
        try{
            Connection con = connectToDataBase();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM ZAPOSLENICI");

            while (rs.next()){
                Integer id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String zanimanje = rs.getString("zanimanje");
                Integer cijenaPoSatu = rs.getInt("cijena_po_satu");
                Integer satnica = rs.getInt("satnica");

                zaposlenici.add(new Zaposlenici(id, zanimanje, cijenaPoSatu, satnica, ime, prezime));
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return zaposlenici;
    }

    public static Optional<Zaposlenici> dohvatiZaposlenika(Integer id) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ZAPOSLENICI WHERE ID = ?");
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String zanimanje = rs.getString("zanimanje");
                Integer cijenaPoSatu = rs.getInt("cijena_po_satu");
                Integer satnica = rs.getInt("satnica");

                Zaposlenici noviZaposlenik = new Zaposlenici(id2, zanimanje, cijenaPoSatu, satnica, ime, prezime);

                return Optional.of(noviZaposlenik);
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return Optional.empty();
    }

    public static void spremiZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO ZAPOSLENICI(ime, prezime, zanimanje, cijena_po_satu, satnica)"
                    + "VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, zaposlenik.getIme());
            pstmt.setString(2, zaposlenik.getPrezime());
            pstmt.setString(3, zaposlenik.getZanimanje());
            pstmt.setInt(4, zaposlenik.getCijenaPoSatu());
            pstmt.setInt(5, zaposlenik.getMjesecnaSatnica());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void azurirajZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("UPDATE ZAPOSLENICI SET " +
                    "IME = ?,"+
                    "PREZIME = ?,"+
                    "ZANIMANJE = ?,"+
                    "CIJENA_PO_SATU = ?," +
                    "SATNICA = ? " +
                    "WHERE ID = ?");
            pstmt.setString(1, zaposlenik.getIme());
            pstmt.setString(2, zaposlenik.getPrezime());
            pstmt.setString(3, zaposlenik.getZanimanje());
            pstmt.setInt(4, zaposlenik.getCijenaPoSatu());
            pstmt.setInt(5, zaposlenik.getMjesecnaSatnica());
            pstmt.setInt(6, zaposlenik.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void obrisiZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM ZAPOSLENICI WHERE ID = ?");
            pstmt.setInt(1, zaposlenik.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static List<Staniste> dohvatiSvaStanista()throws BazaPodatakaException{
        List<Staniste> stanista = new ArrayList<>();
        try{
            Connection con = connectToDataBase();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM STANISTA");

            List<Zivotinja> sveZivotinje = dohvatiSveZivotinje();
            List<Zivotinja> odabraneZivotinje = new ArrayList<>();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String razred = rs.getString("razred");
                Integer broj_jedinki = rs.getInt("broj_jedinki");
                String ima_hrane = rs.getString("ima_hrane");
                String ima_vode = rs.getString("ima_vode");

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(razred.equals(z.getSistematika().razred())){
                        odabraneZivotinje.add(z);
                    }
                }

                stanista.add(new Staniste(id, odabraneZivotinje, broj_jedinki, Boolean.valueOf(ima_hrane), Boolean.valueOf(ima_vode)));
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return stanista;
    }

    public static Optional<Staniste> dohvatiStaniste(Integer id) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM STANISTA WHERE ID = ?");
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            List<Zivotinja> sveZivotinje = dohvatiSveZivotinje();
            List<Zivotinja> odabraneZivotinje = new ArrayList<>();
            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String razred = rs.getString("razred");
                Integer broj_jedinki = rs.getInt("broj_jedinki");
                String ima_hrane = rs.getString("ima_hrane");
                String ima_vode = rs.getString("ima_vode");

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(z.getSistematika().razred().equals(razred)){
                        odabraneZivotinje.add(z);
                    }
                }

                Staniste novoStaniste = new Staniste(id2, odabraneZivotinje, broj_jedinki, Boolean.getBoolean(ima_hrane), Boolean.getBoolean(ima_vode));
                return Optional.of(novoStaniste);
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return Optional.empty();
    }

    public static void spremiStaniste(Staniste staniste) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO STANISTA(razred, broj_jedinki, ima_hrane, ima_vode)"
                    + "VALUES (?, ?, ?, ?)");
            pstmt.setString(1, staniste.getSistematika().razred());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setString(3, staniste.getImaHrane().toString());
            pstmt.setString(4, staniste.getImaVode().toString());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void azurirajStaniste(Staniste staniste) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("UPDATE STANISTA SET " +
                    "RAZRED = ?," +
                    "BROJ_JEDINKI = ?," +
                    "IMA_HRANE = ?,"+
                    "IMA_VODE = ? "+
                    "WHERE ID = ?");
            pstmt.setString(1, staniste.getSistematika().razred());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setString(3, staniste.getImaHrane().toString());
            pstmt.setString(4, staniste.getImaVode().toString());
            pstmt.setInt(5, staniste.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void obrisiStaniste(Staniste staniste) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM STANISTA WHERE ID = ?");
            pstmt.setInt(1, staniste.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static List<Aktivnost> dohvatiSveAktivnosti()throws BazaPodatakaException{
        List<Aktivnost> aktivnosti = new ArrayList<>();
        try{
            Connection con = connectToDataBase();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM AKTIVNOSTI");

            while (rs.next()){
                Integer id = rs.getInt("id");
                String naziv = rs.getString("naziv");
                Integer cijena = rs.getInt("cijena");
                Integer trajanje = rs.getInt("trajanje");
                aktivnosti.add(new Aktivnost.Builder(id, naziv, cijena).saTrajanjem(trajanje).build());
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return aktivnosti;
    }

    public static Optional<Aktivnost> dohvatiAktivnost(Integer id) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM AKTIVNOSTI WHERE ID = ?");
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String naziv = rs.getString("naziv");
                Integer cijena = rs.getInt("cijena");
                Integer trajanje = rs.getInt("trajanje");
                Aktivnost novaAktivnost = new Aktivnost.Builder(id2, naziv, cijena).saTrajanjem(trajanje).build();

                return Optional.of(novaAktivnost);
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }

        return Optional.empty();
    }

    public static void spremiAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO AKTIVNOSTI(naziv, cijena, trajanje) "
                    + "VALUES (?, ?, ?)");
            pstmt.setString(1, aktivnost.getNaziv());
            pstmt.setInt(2, aktivnost.getCijena());
            pstmt.setInt(3, aktivnost.getTrajanje());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void azurirajAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("UPDATE AKTIVNOSTI SET " +
                    "NAZIV = ?,"+
                    "CIJENA = ?,"+
                    "TRAJANJE = ? "+
                    "WHERE ID = ?");
            pstmt.setString(1, aktivnost.getNaziv());
            pstmt.setInt(2, aktivnost.getCijena());
            pstmt.setInt(3, aktivnost.getTrajanje());
            pstmt.setInt(4, aktivnost.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }

    public static void obrisiAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        try{
            Connection con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM AKTIVNOSTI WHERE ID = ?");
            pstmt.setInt(1, aktivnost.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }
    }
}
