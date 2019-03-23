package ru.evg299.jseq.crypto.gost3411;

/**
 * Created by Evgeny(e299792459@gmail.com) on 13.02.14.
 */

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;

import org.apache.ws.security.util.Base64;

public class MainSign {
    private static String containerName = "CntLok";
    private static String containerPass = "123123";

    public static void main(String[] args) throws Exception {
        String data = "1234567890";
        byte[] contentOfString = data.getBytes();

        Provider provider = Security.getProvider("JCP");
        KeyStore keyStore = KeyStore.getInstance("HDImageStore");
        keyStore.load(null, null);

        MessageDigest messageDigest = MessageDigest.getInstance("GOST3411",
                provider);
        messageDigest.reset();
        messageDigest.update(contentOfString);
        byte digest[] = messageDigest.digest();

        // Извлекаем закрытый ключ
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(containerName, containerPass.toCharArray());
        // Сертификат открытого ключа
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(containerName);
        // Создаем подписальщика
        Signature signature = Signature.getInstance("CryptoProSignature",
                provider);
        signature.initSign(privateKey);
        signature.update(digest);
        byte sign[] = signature.sign();

        System.out.println(split76(Base64.encode(digest)));
        System.out.println();
        System.out.println(split76(Base64.encode(sign)));
        System.out.println();
        System.out.println(split76(Base64.encode(cert.getEncoded())));
        System.out.println();
    }

    public static String split76(String string) {
        StringBuffer buffer = new StringBuffer();
        int lineCounter = 0;
        for (int i = 0; i < string.length(); i++) {
            lineCounter++;
            if (76 < lineCounter) {
                buffer.append('\n');
                lineCounter = 1;
            }
            buffer.append(string.charAt(i));
        }

        return buffer.toString();
    }
}
