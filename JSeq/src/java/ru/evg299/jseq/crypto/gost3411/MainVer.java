package ru.evg299.jseq.crypto.gost3411;

/**
 * Created by Evgeny(e299792459@gmail.com) on 13.02.14.
 */

import org.apache.ws.security.util.Base64;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MainVer {
    public static void main(String[] args) throws Exception {
        String data = "1234567890";
        Provider provider = Security.getProvider("JCP");
        KeyStore keyStore = KeyStore.getInstance("HDImageStore");
        keyStore.load(null, null);

        MessageDigest messageDigest = MessageDigest.getInstance("GOST3411",
                provider);
        messageDigest.reset();
        messageDigest.update(data.getBytes());
        byte digest[] = messageDigest.digest();
        System.out.println(Base64.encode(digest));

        String signB64 = "TInFEhPLebzN6bu/WmVd67YN1cs6OWQUyAfsJKnGWU8AhvnXgpJenJLxjFeFhN+ro6DI52GchecRihPm/kqglQ==";
        String sertB64 = "MIIDgTCCAzCgAwIBAgIKcZAJOQACAAM+(------//--------)5JB7WQXD18C8sthf58rqFXY4MzHQbfX8BhfR/luNuBa75OjWecyMaLJ3DJDA1Bt8ferLslnNA31mzv1c4ziUkg==";

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(Base64.decode(sertB64));
        X509Certificate cert = (X509Certificate) certFactory
                .generateCertificate(in);
        System.out.println(cert);

        for (Provider.Service service : provider.getServices()) {
            if ("Signature".equals(service.getType())) {
                if ("GOST3411withGOST3410".equals(service.getAlgorithm())
                        || "NONEwithCryptoProSignature".equals(service
                        .getAlgorithm())
                        || "NONEwithGOST3410".equals(service.getAlgorithm()))
                    continue;

                // Проверяем
                System.out.println("  Algorithm: " + service.getAlgorithm());
                Signature signature2 = Signature.getInstance(service.getAlgorithm(), provider);
                signature2.initVerify(cert);
                signature2.update(digest);
                System.out.println("Check: " + signature2.verify(Base64.decode(signB64)));
            }
        }
    }

    public static String readFile(String fn, String encoding)
            throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(fn), encoding);
        StringBuffer sb = new StringBuffer();
        int data = reader.read();
        while (data != -1) {
            char dataChar = (char) data;
            sb.append(dataChar);
            data = reader.read();
        }
        reader.close();

        return sb.toString().trim();
    }
}
