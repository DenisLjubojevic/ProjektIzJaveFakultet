package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.*;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BazaPodataka {
    private static Connection connectToDataBase() throws SQLException, IOException{
        Properties konfiguracijaBaze = new Properties();
        konfiguracijaBaze.load(new FileInputStream("Datoteke/bazaPodataka.properties"));
        org.h2.tools.Server.createTcpServer("-tcpAllowOthers").start();

        return DriverManager.getConnection(
                konfiguracijaBaze.getProperty("bazaPodatakaURL"),
                konfiguracijaBaze.getProperty("korisnickoIme"),
                konfiguracijaBaze.getProperty("lozinka")
        );
    }

    public static List<Korisnik> dohvatiKorisnike() throws BazaPodatakaException {
        List<Korisnik> korisnici = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM USERS");

            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                Integer role = rs.getInt("role");

                korisnici.add(new Korisnik(username, password, role));
            }

        }catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return korisnici;
    }

    public static void stovoriKorisnika(Korisnik korisnik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO USERS(username, password, role)"
                    + "VALUES (?, ?, ?)");
            pstmt.setString(1, korisnik.getKorisnickoIme());
            pstmt.setString(2, korisnik.getLozinka());
            pstmt.setInt(3, korisnik.getRole());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

    }

    public static List<Zivotinja> dohvatiSveZivotinje()throws BazaPodatakaException{
        List<Zivotinja> zivotinje = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM ZIVOTINJA");

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return zivotinje;
    }

    public static Optional<Zivotinja> dohvatiZivotinju(Integer id) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM ZIVOTINJA WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return Optional.empty();
    }

    public static List<Obrok> dohvatiSveObroke()throws BazaPodatakaException{
        List<Obrok> obroci = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM OBROCI");

            while (rs.next()){
                Integer id = rs.getInt("id");
                String vrstaHrane = rs.getString("vrsta_hrane");
                Double kolicina = rs.getDouble("kolicina");
                Time vrijemeObroka = rs.getTime("vrijeme_obroka");
                String napomena = rs.getString("napomena");

                obroci.add(new Obrok(id,vrstaHrane,kolicina,vrijemeObroka.toLocalTime(),napomena));
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return obroci;
    }

    public static Optional<Obrok> dohvatiObrokById(Integer id)throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            pstmt = con.prepareStatement("SELECT * FROM OBROCI WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String vrstaHrane = rs.getString("vrsta_hrane");
                Double kolicina = rs.getDouble("kolicina");
                Time vrijemeObroka = rs.getTime("vrijeme_obroka");
                String napomena = rs.getString("napomena");

                Obrok obrok = new Obrok(id2, vrstaHrane, kolicina, vrijemeObroka.toLocalTime(), napomena);
                return Optional.of(obrok);
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return Optional.empty();
    }

    public static void spremiZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO ZIVOTINJA(vrsta, razred, starost, spol)"
                    + "VALUES (?, ?, ?, ?)");
            pstmt.setString(1, zivotinja.getSistematika().vrsta());
            pstmt.setString(2, zivotinja.getSistematika().razred());
            pstmt.setInt(3, zivotinja.getStarost());
            pstmt.setString(4, zivotinja.getSpol());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE ZIVOTINJA SET " +
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
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiZivotinju(Zivotinja zivotinja) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM ZIVOTINJA WHERE ID = ?");
            pstmt.setInt(1, zivotinja.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static List<Zaposlenici> dohvatiSveZaposlenike()throws BazaPodatakaException{
        List<Zaposlenici> zaposlenici = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM ZAPOSLENICI");

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return zaposlenici;
    }

    public static Optional<Zaposlenici> dohvatiZaposlenika(Integer id) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM ZAPOSLENICI WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return Optional.empty();
    }

    public static void spremiZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO ZAPOSLENICI(ime, prezime, zanimanje, cijena_po_satu, satnica)"
                    + "VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, zaposlenik.getIme());
            pstmt.setString(2, zaposlenik.getPrezime());
            pstmt.setString(3, zaposlenik.getZanimanje());
            pstmt.setInt(4, zaposlenik.getCijenaPoSatu());
            pstmt.setInt(5, zaposlenik.getMjesecnaSatnica());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE ZAPOSLENICI SET " +
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
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiZaposlenika(Zaposlenici zaposlenik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM ZAPOSLENICI WHERE ID = ?");
            pstmt.setInt(1, zaposlenik.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static List<Staniste> dohvatiSvaStanista()throws BazaPodatakaException{
        List<Staniste> stanista = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM STANISTA");

            List<Zivotinja> sveZivotinje = dohvatiSveZivotinje();
            List<Zivotinja> odabraneZivotinje = new ArrayList<>();
            while (rs.next()){
                Integer id = rs.getInt("id");
                String vrsta = rs.getString("vrsta");
                Integer broj_jedinki = rs.getInt("broj_jedinki");
                Integer obrok_id = rs.getInt("obrok_id");

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(vrsta.equals(z.getSistematika().vrsta())){
                        odabraneZivotinje.add(z);
                    }
                }

                Optional<Obrok> obrok = dohvatiObrokById(obrok_id);

                stanista.add(new Staniste(id, odabraneZivotinje, broj_jedinki, obrok.get()));
            }

            con.close();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return stanista;
    }

    public static Optional<Staniste> dohvatiStaniste(Integer id) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM STANISTA WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            List<Zivotinja> sveZivotinje = dohvatiSveZivotinje();
            List<Zivotinja> odabraneZivotinje = new ArrayList<>();
            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String vrsta = rs.getString("vrsta");
                Integer broj_jedinki = rs.getInt("broj_jedinki");
                Integer obrok_id = rs.getInt("obrok_id");

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(z.getSistematika().vrsta().equals(vrsta)){
                        odabraneZivotinje.add(z);
                    }
                }

                Optional<Obrok> obrok = dohvatiObrokById(obrok_id);
                Staniste novoStaniste = new Staniste(id2, odabraneZivotinje, broj_jedinki, obrok.get());
                return Optional.of(novoStaniste);
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return Optional.empty();
    }

    public static void spremiStaniste(Staniste staniste) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO STANISTA(vrsta, broj_jedinki, obrok_id)"
                    + "VALUES (?, ?, ?)");
            pstmt.setString(1, staniste.getSistematika().vrsta());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setInt(3, staniste.getObrok().getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajStaniste(Staniste staniste) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE STANISTA SET " +
                    "VRSTA = ?," +
                    "BROJ_JEDINKI = ?," +
                    "OBROK_ID = ?" +
                    "WHERE ID = ?");
            pstmt.setString(1, staniste.getSistematika().vrsta());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setInt(3, staniste.getObrok().getId());
            pstmt.setInt(4, staniste.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiStaniste(Staniste staniste) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM STANISTA WHERE ID = ?");
            pstmt.setInt(1, staniste.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static List<Aktivnost> dohvatiSveAktivnosti()throws BazaPodatakaException{
        List<Aktivnost> aktivnosti = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM AKTIVNOSTI");

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return aktivnosti;
    }

    public static Optional<Aktivnost> dohvatiAktivnost(Integer id) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM AKTIVNOSTI WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

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
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return Optional.empty();
    }

    public static void spremiAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO AKTIVNOSTI(naziv, cijena, trajanje) "
                    + "VALUES (?, ?, ?)");
            pstmt.setString(1, aktivnost.getNaziv());
            pstmt.setInt(2, aktivnost.getCijena());
            pstmt.setInt(3, aktivnost.getTrajanje());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE AKTIVNOSTI SET " +
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
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiAktivnost(Aktivnost aktivnost) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM AKTIVNOSTI WHERE ID = ?");
            pstmt.setInt(1, aktivnost.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }


}
