package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.Aktivnosti.Aktivnost;
import com.example.projektnizadatak.Entiteti.Korisnici.Korisnik;
import com.example.projektnizadatak.Entiteti.Korisnici.Role;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Zaposlenici.RasporedRada;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Smjena;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Zaposlenici;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Zivotinje.ZdravstveniKarton;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;

import javax.sound.midi.ShortMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class BazaPodataka {
    public static Connection connectToDataBase() throws SQLException, IOException{
        Properties konfiguracijaBaze = new Properties();
        konfiguracijaBaze.load(new FileInputStream("Datoteke/bazaPodataka.properties"));

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
                Integer id = rs.getInt("idusers");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                korisnici.add(new Korisnik(id, username, password, Role.valueOf(role)));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return korisnici;
    }

    public static Korisnik dohvatiKorisnikById(Integer id) throws BazaPodatakaException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM USERS WHERE IDUSERS = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("idusers");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");;

                return new Korisnik(id2, username, password, Role.valueOf(role));
            }

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return null;
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
            pstmt.setString(3, korisnik.getRole().getRolaKorisnika());

            pstmt.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajKorisnika(Korisnik korisnik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE USERS SET " +
                    "USERNAME = ?,"+
                    "PASSWORD = ?,"+
                    "ROLE = ? " +
                    "WHERE IDUSERS = ?");
            pstmt.setString(1, korisnik.getKorisnickoIme());
            pstmt.setString(2, korisnik.getLozinka());
            pstmt.setString(3, korisnik.getRole().getRolaKorisnika());
            pstmt.setInt(4, korisnik.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiKorisnika(Korisnik korisnik) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM USERS WHERE IDUSERS = ?");
            pstmt.setInt(1, korisnik.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static List<Promjene> dohvatiPromjene() throws BazaPodatakaException{
        List<Promjene> promjene = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PROMJENE");

            while(rs.next()){
                Integer id = rs.getInt("id");
                Integer userId = rs.getInt("userId");
                String opisPromjene = rs.getString("opisPromjene");
                Timestamp vrijemePromjene = rs.getTimestamp("vrijemePromjene");

                promjene.add(new Promjene(id, userId, opisPromjene, vrijemePromjene));
            }

        }catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }

        return promjene;
    }

    public static void spremiPromjenu(Promjene promjena) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO PROMJENE(userId, opisPromjene, vrijemePromjene)"
                    + "VALUES (?, ?, ?)");
            pstmt.setInt(1, promjena.getUserId());
            pstmt.setString(2, promjena.getOpisPromjene());
            pstmt.setTimestamp(3, promjena.getVrijemePromjene());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiPromjenu(Promjene promjena) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM PROMJENE WHERE ID = ?");
            pstmt.setInt(1, promjena.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiSvePromjene() throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM PROMJENE");

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

    public static void spremiZdravstveniKarton(ZdravstveniKarton karton, Integer zivotinjaId) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            pstmt = con.prepareStatement("INSERT INTO ZDRAVSTVENI_KARTON(datum_pregleda, dijagnoza, terapija, napomene, zivotinja_id)"
                            + "VALUES (?, ?, ?, ?, ?)");

            pstmt.setDate(1, Date.valueOf(karton.getDatumPregleda()));
            pstmt.setString(2, karton.getDijagnoza());
            pstmt.setString(3, karton.getTerapija());
            pstmt.setString(4, karton.getNapomena());
            pstmt.setInt(5, zivotinjaId);

            pstmt.executeUpdate();
        }catch (Exception e) {
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (con != null) con.close(); } catch (SQLException e) { }
        }
    }

    public static List<ZdravstveniKarton> dohvatiZdravsteveneKartoneZaZivotinju(Integer id) throws BazaPodatakaException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("SELECT * FROM ZDRAVSTVENI_KARTON WHERE ZIVOTINJA_ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            List<ZdravstveniKarton> kartoniZivotinje = new ArrayList<>();
            while (rs.next()){
                Integer id2 = rs.getInt("id");
                LocalDate datumPregleda = rs.getDate("datum_pregleda").toLocalDate();
                String dijagnoza = rs.getString("dijagnoza");
                String terapija = rs.getString("terapija");
                String napomene = rs.getString("napomene");

                ZdravstveniKarton noviKarton = new ZdravstveniKarton(id2, datumPregleda, dijagnoza, terapija, napomene);
                kartoniZivotinje.add(noviKarton);
            }

            return kartoniZivotinje;
        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM ZDRAVSTVENI_KARTON WHERE ID = ?");
            pstmt.setInt(1, zdravstveniKarton.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static List<Hrana> dohvatiSvuHranu()throws BazaPodatakaException{
        List<Hrana> obroci = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM HRANA");

            while (rs.next()){
                Integer id = rs.getInt("id");
                String vrstaHrane = rs.getString("vrsta_hrane");
                Double kolicina = rs.getDouble("kolicina");
                String napomena = rs.getString("napomena");

                obroci.add(new Hrana(id, vrstaHrane, kolicina, napomena));
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

    public static Optional<Hrana> dohvatiHranuById(Integer id)throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            pstmt = con.prepareStatement("SELECT * FROM HRANA WHERE ID = ?");
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()){
                Integer id2 = rs.getInt("id");
                String vrstaHrane = rs.getString("vrsta_hrane");
                Double kolicina = rs.getDouble("kolicina");
                String napomena = rs.getString("napomena");

                Hrana hrana = new Hrana(id2, vrstaHrane, kolicina, napomena);
                return Optional.of(hrana);
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

    public static void spremiHranu(Hrana hrana) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO HRANA(VRSTA_HRANE, KOLICINA, NAPOMENA)"
                    + "VALUES (?, ?, ?)");
            pstmt.setString(1, hrana.getVrstaHrane());
            pstmt.setDouble(2, hrana.getKolicina());
            pstmt.setString(3, hrana.getNapomena());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void azurirajHranu(Hrana hrana) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("UPDATE HRANA SET " +
                    "VRSTA_HRANE = ?,"+
                    "KOLICINA = ?,"+
                    "NAPOMENA = ? " +
                    "WHERE ID = ?");
            pstmt.setString(1, hrana.getVrstaHrane());
            pstmt.setDouble(2, hrana.getKolicina());
            pstmt.setString(3, hrana.getNapomena());
            pstmt.setInt(4, hrana.getId());

            pstmt.executeUpdate();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiHranu(Hrana hrana) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM HRANA WHERE ID = ?");
            pstmt.setInt(1, hrana.getId());

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
                Integer hrana_id = rs.getInt("hrana_id");
                Blob stanisteSlikaBlob = rs.getBlob("slika_stanista");
                Time vrijeme_hranjenja = rs.getTime("vrijeme_hranjenja");

                byte[] slikaStanista = null;
                if (stanisteSlikaBlob != null){
                    slikaStanista = stanisteSlikaBlob.getBytes(1,(int) stanisteSlikaBlob.length());
                }

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(vrsta.equals(z.getSistematika().vrsta())){
                        odabraneZivotinje.add(z);
                    }
                }

                Optional<Hrana> hrana = dohvatiHranuById(hrana_id);

                stanista.add(new Staniste(id, odabraneZivotinje, broj_jedinki, hrana.get(), vrijeme_hranjenja.toLocalTime(), slikaStanista));
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
                Integer hrana_id = rs.getInt("hrana_id");
                Blob stanisteSlikaBlob = rs.getBlob("slika_stanista");
                Time vrijeme_hranjenja = rs.getTime("vrijeme_hranjenja");

                byte[] slikaStanista = null;
                if (stanisteSlikaBlob != null){
                    slikaStanista = stanisteSlikaBlob.getBytes(1,(int) stanisteSlikaBlob.length());
                }

                odabraneZivotinje.clear();
                for (Zivotinja z: sveZivotinje) {
                    if(z.getSistematika().vrsta().equals(vrsta)){
                        odabraneZivotinje.add(z);
                    }
                }

                Optional<Hrana> hrana = dohvatiHranuById(hrana_id);
                Staniste novoStaniste = new Staniste(id2, odabraneZivotinje, broj_jedinki, hrana.orElse(null), vrijeme_hranjenja.toLocalTime(), slikaStanista);
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

            pstmt = con.prepareStatement("INSERT INTO STANISTA(vrsta, broj_jedinki, hrana_id, slika_stanista, vrijeme_hranjenja)"
                    + "VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, staniste.getSistematika().vrsta());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setInt(3, staniste.getHrana().getId());
            pstmt.setBytes(4, staniste.getSlikaStanista());
            pstmt.setTime(5, Time.valueOf(staniste.getVrijemeHranjenja()));

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
                    "HRANA_ID = ?," +
                    "SLIKA_STANISTA = ?," +
                    "VRIJEME_HRANJENJA = ?" +
                    "WHERE ID = ?");
            pstmt.setString(1, staniste.getSistematika().vrsta());
            pstmt.setInt(2, staniste.getBrojJedinki());
            pstmt.setInt(3, staniste.getHrana().getId());
            pstmt.setBytes(4, staniste.getSlikaStanista());
            pstmt.setTime(5, Time.valueOf(staniste.getVrijemeHranjenja()));
            pstmt.setInt(6, staniste.getId());

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

    public static RasporedRada dohvatiRasporedPoZaposleniku(Integer zaposlenikId) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = connectToDataBase();

            pstmt = con.prepareStatement("SELECT * FROM RASPOREDRADA WHERE zaposlenik_id = ?");
            pstmt.setInt(1, zaposlenikId);

            rs = pstmt.executeQuery();

            Map<String, Smjena> smjenaPodanima = new HashMap<>();
            Integer id2 = null;

            while (rs.next()){
                if (id2 == null) {
                    id2 = rs.getInt("id");
                }

                String dan = rs.getString("dan");
                String smjena = rs.getString("smjena");
                smjenaPodanima.put(dan, Smjena.valueOf(smjena));
            }

            return new RasporedRada(id2, zaposlenikId, smjenaPodanima);
        } catch (Exception e){
            e.printStackTrace();
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu! - RAS");
        }finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void spremiRasporedRada(RasporedRada rasporedRada) throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("INSERT INTO RASPOREDRADA(ZAPOSLENIK_ID, DAN, SMJENA)"
                    + "VALUES (?, ?, ?)");

            for (Map.Entry<String, Smjena> entry : rasporedRada.getSmjenaPoDanima().entrySet()){
                pstmt.setInt(1, rasporedRada.getZaposlenikId());
                pstmt.setString(2, entry.getKey());
                pstmt.setString(3, entry.getValue().name());

                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (Exception e){
            throw new BazaPodatakaException("Pogreška prilikom povezivanja na mrežu!");
        }finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {  }
            try { if (con != null) con.close(); } catch (SQLException e) {  }
        }
    }

    public static void obrisiSveRasporede() throws BazaPodatakaException{
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = connectToDataBase();

            if(con != null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            pstmt = con.prepareStatement("DELETE FROM RASPOREDRADA");

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
