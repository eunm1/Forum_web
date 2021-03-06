package com.eunm1.forum.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Checksum {
    private String result;

    public MD5Checksum(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(input.getBytes("UTF-8"));
        byte[] md5Hash = md5.digest();
        StringBuilder hexMD5Hash = new StringBuilder();
        for(byte b : md5Hash){
            String hexString = String.format("%02x",b);
            hexMD5Hash.append(hexString);
        }
        result = hexMD5Hash.toString();
    }

    public String toString(){
        return result;
    }
}
