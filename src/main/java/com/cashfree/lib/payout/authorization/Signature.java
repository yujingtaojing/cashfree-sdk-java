package com.cashfree.lib.payout.authorization;
import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.time.Instant;
import java.util.Base64;

public class Signature {
    public static String generateEncryptedSignature(String clientId , String pathname) {
         String clientIdWithEpochTimeStamp = clientId+"."+ Instant.now().getEpochSecond();
        String encrytedSignature = "";
        try {
            byte[] keyBytes = Files
                    .readAllBytes(new File(pathname).toPath()); // Absolute Path to be replaced
//                    .readAllBytes(new File("/Users/sameera/Downloads/payout_test_public_key.pem").toPath()); // Absolute Path to be replaced

            String publicKeyContent = new String(keyBytes);
            System.out.println(publicKeyContent);
            publicKeyContent = publicKeyContent.replaceAll("[\\t\\n\\r]", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            System.out.println(publicKeyContent);
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(publicKeyContent));
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encrytedSignature = Base64.getEncoder().encodeToString(cipher.doFinal(clientIdWithEpochTimeStamp.getBytes()));
            System.out.println(encrytedSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrytedSignature;
    }
}
