package com.example.projektnizadatak.Util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashiranje {
    public String hashiranaLozinka(String lozinka) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        byte[] message = messageDigest.digest(lozinka.getBytes());
        BigInteger bigInt = new BigInteger(1, message);

        return bigInt.toString(16);
    }
}
