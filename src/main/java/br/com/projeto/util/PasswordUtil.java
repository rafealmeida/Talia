package br.com.projeto.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtil {

    // Transforma a senha "123456" em um código ilegível (Hash)
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }
}
