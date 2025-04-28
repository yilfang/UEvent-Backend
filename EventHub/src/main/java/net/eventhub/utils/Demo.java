package net.eventhub.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Demo {
    static StandardPBEStringEncryptor encryptor;
    
    public static void main(String[] args) {
        Demo d = new Demo();
        String enc = d.encryptString("MyNewString@123");
        System.out.println("ENC : "+enc);
        
        String dec = d.decryptString(enc);
        System.out.println("DEC : "+dec);
    }
    
    public static String encryptString(String hash){
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("ABXY"); 
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES"); 
        return encryptor.encrypt(hash);
    }
    
    public static String decryptString(String hash){
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("ABXY");
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        return encryptor.decrypt(hash);
    }
    
}
