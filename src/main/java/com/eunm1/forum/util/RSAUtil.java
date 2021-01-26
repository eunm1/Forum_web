package com.eunm1.forum.util;

import com.eunm1.forum.db.other.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;

// client -> server 데이터 전송시 암호화 기능 담당
public class RSAUtil {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    private KeyPairGenerator generator; //공개키와 비공개키의 페어를 생성
    private KeyFactory keyFactory; //특정 인코딩 형식 또는 키 매개 변수에 따라 키를 가져오고 내보내기위한 인프라를 제공
    private KeyPair keyPair; //단순한 홀더, 시큐러티 조치도 적용 x
    private Cipher cipher; //암호화 복호화

    public RSAUtil(){
        try{
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
        } catch (Exception e) {
            logger.warn("RSAUtil() fail", e);
        }
    }

    // 새로운 키값을 가진 RSA 생성
    public RSA createRSA(){
        RSA rsa = null;
        try{
            keyPair = generator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String modulus = publicKeySpec.getModulus().toString(16);
            String exponent = publicKeySpec.getPublicExponent().toString(16);

            rsa = new RSA(privateKey, modulus, exponent);
        }catch (Exception e){
            logger.warn("RSAUtil.createRSA()", e);
        }
        return rsa;
    }

    //개인키를 이용한 RSA복호화
    //session PK에 저장된 privateKey
    // encryptedText 암호화된 문자열
    public String getDecryptText(PrivateKey privateKey, String encryptText) throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(hexToByteArray(encryptText));
        return new String(decryptedBytes, "UTF-8");
    }

    //16진수 문자열을 byte배열로 변환
    private byte[] hexToByteArray(String encryptText) {
        if(encryptText == null || encryptText.length() % 2 != 0){
            return new byte[] {};
        }

        byte[] bytes = new byte[encryptText.length() / 2];
        for(int i = 0 ; i < encryptText.length(); i+= 2){
            byte value = (byte) Integer.parseInt(encryptText.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
}
