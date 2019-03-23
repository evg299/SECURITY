package ru.evg299.jseq.crypto.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Evgeny(e299792459@gmail.com) on 13.02.14.
 */
public class MainSignature {
    public static void main(String[] args) throws NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, SignatureException, IOException,
            ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] plainText = "sdufywuhad823yue821de21e2".getBytes("UTF8");

        System.out.println("\nStart generating RSA key");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");

        saveObjToFile("c:/keyPar.obj", key);
        KeyPair key2 = (KeyPair) getFromFile("c:/keyPar.obj");
        System.out.println(key2);

		/*
		 * PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
		 * new X509EncodedKeySpec(key2.getPublic().getEncoded()));
		 */

        // Compute signature
        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(key.getPrivate());
        instance.update(plainText);
        byte[] signature = instance.sign();

        Signature instance2 = Signature.getInstance("SHA1withRSA");
        instance2.initVerify(key.getPublic());
        instance2.update(plainText);
        boolean isvalid = instance2.verify(signature);
        System.out.println(isvalid);

    }

    public static Object getFromFile(String fn) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fn);
        ObjectInputStream oin = new ObjectInputStream(fis);
        Object result = oin.readObject();
        oin.close();
        fis.close();

        return result;
    }

    public static void saveObjToFile(String fn, Object obj) throws IOException {
        FileOutputStream fos = new FileOutputStream(fn);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }
}
