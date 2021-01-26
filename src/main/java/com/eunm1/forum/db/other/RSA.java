package com.eunm1.forum.db.other;

import lombok.Data;

import java.security.PrivateKey;

//개인키 공개키를 담는 VO
@Data
public class RSA {
    private PrivateKey privateKey;
    private String modulus;
    private String exponent;

    public RSA(){
    }

    public RSA(PrivateKey privateKey, String modulus, String exponent){
        this.privateKey = privateKey;
        this.modulus = modulus;
        this.exponent = exponent;
    }
}
