package com.example.projektnizadatak.Util;

import org.h2.engine.Database;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {

    public void saveStanisteImage(int stanisteId, String imagePath) throws SQLException, IOException {
        String query = "UPDATE STANISTA SET slika_stanista = ? WHERE id = ?";
        try(Connection conn = BazaPodataka.connectToDataBase();
            PreparedStatement pstm = conn.prepareStatement(query);
            InputStream is = getClass().getResourceAsStream(imagePath)) {

            if (is == null) {
                throw new IOException("File not found: " + imagePath);
            }

            pstm.setBinaryStream(1, is, is.available());
            pstm.setInt(2, stanisteId);
            pstm.executeUpdate();
        }
    }

    public void loadStanisteImage(int stanisteId, String outputPath) throws SQLException, IOException{
        String query = "SELECT slika_stanista FROM Stanista WHERE id = ?";
        try (Connection conn = BazaPodataka.connectToDataBase();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, stanisteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("slika_stanista");
                try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                    fos.write(imageBytes);
                }
            }
        }
    }
}
