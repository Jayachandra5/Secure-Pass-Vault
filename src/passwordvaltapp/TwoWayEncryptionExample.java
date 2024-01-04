package passwordvaltapp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class TwoWayEncryptionExample {

    static String encrypt(String originalMessage, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Generate a random IV
            byte[] ivBytes = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // Use a separate byte array for the salt
            byte[] salt = secretKey.getBytes();
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
            SecretKey secretKeyObj = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeyObj, iv);
            byte[] encryptedBytes = cipher.doFinal(originalMessage.getBytes());

            // Prepend the IV to the encrypted message
            byte[] combined = new byte[ivBytes.length + encryptedBytes.length];
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combined, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String decrypt(String encryptedMessage, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Extract the IV from the encrypted message
            byte[] combined = Base64.getDecoder().decode(encryptedMessage);
            byte[] ivBytes = new byte[16];
            System.arraycopy(combined, 0, ivBytes, 0, ivBytes.length);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // Use a separate byte array for the salt
            byte[] salt = secretKey.getBytes();
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
            SecretKey secretKeyObj = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, secretKeyObj, iv);
            byte[] encryptedBytes = new byte[combined.length - ivBytes.length];
            System.arraycopy(combined, ivBytes.length, encryptedBytes, 0, encryptedBytes.length);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
