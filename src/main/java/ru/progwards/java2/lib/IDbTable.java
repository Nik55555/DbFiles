package ru.progwards.java2.lib;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Интерфейс для таблиц БД,
 * включает статический метод {@link #hashSha256(String)} для получения хеша по паролю
 * @param <K> ключ
 * @param <V> значение
 */
public interface IDbTable<K, V> {
    String getTableName();

    List<V> getAll();
    List<V> select(Predicate<V> check);
    K getKey(V elem);
    boolean put(V elem) throws Exception;
    V remove(K key) throws Exception;
    V findKey(K key);
    boolean exists(K key);

    void readAll() throws Exception;
    void saveAll() throws Exception;

    /**
     * Метод для получения хеша из строки по алгоритму SHA-256
     * @param stringToEncrypt исходная строка (например, пароль)
     * @return хеш-код в виде строки байт (16x представление) с лидирующими нулями
     */
    static String hashSha256(String stringToEncrypt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] hash) {
        if (hash == null)
            return null;
        StringBuffer hexString = new StringBuffer();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
