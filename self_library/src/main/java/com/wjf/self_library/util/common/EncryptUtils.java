package com.wjf.self_library.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptUtils {

    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 公司约定默认的AES加密key
     *
     * @return
     */
    public static byte[] dk() {
        return encryptMd5("f57d95a9bdb4981b477c24a0bd05ea25".getBytes());
    }

    ///////////////////////////////////////////////////////////////////////////
    // hash encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMd5ToString(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptMd5ToString(data.getBytes());
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMd5ToString(final String data, final String salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return ConvertUtils.bytes2HexString(encryptMd5(data.getBytes()));
        }
        if (data == null) {
            return ConvertUtils.bytes2HexString(encryptMd5(salt.getBytes()));
        }
        return ConvertUtils.bytes2HexString(encryptMd5((data + salt).getBytes()));
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMd5ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptMd5(data));
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMd5ToString(final byte[] data, final byte[] salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return ConvertUtils.bytes2HexString(encryptMd5(data));
        }
        if (data == null) {
            return ConvertUtils.bytes2HexString(encryptMd5(salt));
        }
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return ConvertUtils.bytes2HexString(encryptMd5(dataSalt));
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    public static byte[] encryptMd5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * Return the hex string of file's MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the hex string of file's MD5 encryption
     */
    public static String encryptMd5File2String(final String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return encryptMd5File2String(file);
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMd5File(final String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return encryptMd5File(file);
    }

    /**
     * Return the hex string of file's MD5 encryption.
     *
     * @param file The file.
     * @return the hex string of file's MD5 encryption
     */
    public static String encryptMd5File2String(final File file) {
        return ConvertUtils.bytes2HexString(encryptMd5File(file));
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param file The file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMd5File(final File file) {
        if (file == null) {
            return new byte[0];
        }
        DigestInputStream digestInputStream = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (digestInputStream.read(buffer) < 1) {
                    break;
                }
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (Exception e) {
            LogUtils.error(e);
            return new byte[0];
        } finally {
            try {
                if (digestInputStream != null) {
                    digestInputStream.close();
                }

            } catch (Exception e) {
                LogUtils.error(e);
            }
        }
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    public static String encryptSha1ToString(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSha1ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    public static String encryptSha1ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSha1(data));
    }

    /**
     * Return the bytes of SHA1 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA1 encryption
     */
    public static byte[] encryptSha1(final byte[] data) {
        return hashTemplate(data, "SHA-1");
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    public static String encryptSha256ToString(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSha256ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    public static String encryptSha256ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSha256(data));
    }

    /**
     * Return the bytes of SHA256 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA256 encryption
     */
    public static byte[] encryptSha256(final byte[] data) {
        return hashTemplate(data, "SHA-256");
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    public static String encryptSha512ToString(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSha512ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    public static String encryptSha512ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSha512(data));
    }

    /**
     * Return the bytes of SHA512 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA512 encryption
     */
    public static byte[] encryptSha512(final byte[] data) {
        return hashTemplate(data, "SHA-512");
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return new byte[0];
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // hmac encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    public static String encryptHmacMd5ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) {
            return "";
        }
        return encryptHmacMd5ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    public static String encryptHmacMd5ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacMd5(data, key));
    }

    /**
     * Return the bytes of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacMD5 encryption
     */
    public static byte[] encryptHmacMd5(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacMD5");
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    public static String encryptHmacSha1ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) {
            return "";
        }
        return encryptHmacSha1ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    public static String encryptHmacSha1ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSha1(data, key));
    }

    /**
     * Return the bytes of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA1 encryption
     */
    public static byte[] encryptHmacSha1(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA1");
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    public static String encryptHmacSha256ToString(final String data, final String key) {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return "";
        }
        return encryptHmacSha256ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    public static String encryptHmacSha256ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSha256(data, key));
    }

    /**
     * Return the bytes of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA256 encryption
     */
    public static byte[] encryptHmacSha256(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA256");
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    public static String encryptHmacSha512ToString(final String data, final String key) {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return "";
        }
        return encryptHmacSha512ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    public static String encryptHmacSha512ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSha512(data, key));
    }

    /**
     * Return the bytes of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA512 encryption
     */
    public static byte[] encryptHmacSha512(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA512");
    }

    /**
     * Return the bytes of hmac encryption.
     *
     * @param data      The data.
     * @param key       The key.
     * @param algorithm The name of hmac encryption.
     * @return the bytes of hmac encryption
     */
    private static byte[] hmacTemplate(
            final byte[] data, final byte[] key, final String algorithm) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return new byte[0];
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the Base64-encode bytes of DES encryption
     */
    public static byte[] encryptDes2Base64(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return EncodeUtils.base64Encode(encryptDes(data, key, transformation, iv));
    }

    /**
     * Return the hex string of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the hex string of DES encryption
     */
    public static String encryptDes2HexString(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return ConvertUtils.bytes2HexString(encryptDes(data, key, transformation, iv));
    }

    /**
     * Return the bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of DES encryption
     */
    public static byte[] encryptDes(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "DES", transformation, iv, true);
    }

    /**
     * Return the bytes of DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of DES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64Des(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return decryptDes(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * Return the bytes of DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of DES decryption for hex string
     */
    public static byte[] decryptHexStringDes(
            final String data, final byte[] key, final String transformation, final byte[] iv) {
        return decryptDes(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * Return the bytes of DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of DES decryption
     */
    public static byte[] decryptDes(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "DES", transformation, iv, false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 3DES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the Base64-encode bytes of 3DES encryption
     */
    public static byte[] encrypt3Des2Base64(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return EncodeUtils.base64Encode(encrypt3Des(data, key, transformation, iv));
    }

    /**
     * Return the hex string of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the hex string of 3DES encryption
     */
    public static String encrypt3Des2HexString(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return ConvertUtils.bytes2HexString(encrypt3Des(data, key, transformation, iv));
    }

    /**
     * Return the bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of 3DES encryption
     */
    public static byte[] encrypt3Des(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "DESede", transformation, iv, true);
    }

    /**
     * Return the bytes of 3DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of 3DES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64With3Des(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return decrypt3Des(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * Return the bytes of 3DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of 3DES decryption for hex string
     */
    public static byte[] decryptHexString3Des(
            final String data, final byte[] key, final String transformation, final byte[] iv) {
        return decrypt3Des(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * Return the bytes of 3DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of 3DES decryption
     */
    public static byte[] decrypt3Des(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "DESede", transformation, iv, false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the Base64-encode bytes of AES encryption
     */
    public static byte[] encryptAes2Base64(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return EncodeUtils.base64Encode(encryptAes(data, key, transformation, iv));
    }

    /**
     * Return the hex string of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the hex string of AES encryption
     */
    public static String encryptAes2HexString(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return ConvertUtils.bytes2HexString(encryptAes(data, key, transformation, iv));
    }

    /**
     * Return the bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of AES encryption
     */
    public static byte[] encryptAes(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "AES", transformation, iv, true);
    }

    /**
     * Return the bytes of AES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of AES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64Aes(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return decryptAes(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * Return the bytes of AES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of AES decryption for hex string
     */
    public static byte[] decryptHexStringAes(
            final String data, final byte[] key, final String transformation, final byte[] iv) {
        return decryptAes(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * Return the bytes of AES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the buffer are copied to protect against
     *                       subsequent modification.
     * @return the bytes of AES decryption
     */
    public static byte[] decryptAes(
            final byte[] data, final byte[] key, final String transformation, final byte[] iv) {
        return symmetricTemplate(data, key, "AES", transformation, iv, false);
    }

    /**
     * Return the bytes of symmetric encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param algorithm      The name of algorithm.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of symmetric encryption or decryption
     */
    private static byte[] symmetricTemplate(
            final byte[] data,
            final byte[] key,
            final String algorithm,
            final String transformation,
            final byte[] iv,
            final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return new byte[0];
        }
        try {
            SecretKey secretKey;
            if ("DES".equals(algorithm)) {
                DESKeySpec desKey = new DESKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
                secretKey = keyFactory.generateSecret(desKey);
            } else {
                secretKey = new SecretKeySpec(key, algorithm);
            }
            Cipher cipher = Cipher.getInstance(transformation);
            if (iv == null || iv.length == 0) {
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey);
            } else {
                AlgorithmParameterSpec params = new IvParameterSpec(iv);
                cipher.init(
                        isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, params);
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // RSA encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of RSA encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the Base64-encode bytes of RSA encryption
     */
    public static byte[] encryptRsa2Base64(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return EncodeUtils.base64Encode(encryptRsa(data, key, isPublicKey, transformation));
    }

    /**
     * Return the hex string of RSA encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the hex string of RSA encryption
     */
    public static String encryptRsa2HexString(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return ConvertUtils.bytes2HexString(encryptRsa(data, key, isPublicKey, transformation));
    }

    /**
     * Return the bytes of RSA encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA encryption
     */
    public static byte[] encryptRsa(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return rsaTemplate(data, key, isPublicKey, transformation, true);
    }

    /**
     * Return the bytes of RSA decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64Rsa(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return decryptRsa(EncodeUtils.base64Decode(data), key, isPublicKey, transformation);
    }

    /**
     * Return the bytes of RSA decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption for hex string
     */
    public static byte[] decryptHexStringRsa(
            final String data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return decryptRsa(ConvertUtils.hexString2Bytes(data), key, isPublicKey, transformation);
    }

    /**
     * Return the bytes of RSA decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption
     */
    public static byte[] decryptRsa(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation) {
        return rsaTemplate(data, key, isPublicKey, transformation, false);
    }

    /**
     * Return the bytes of RSA encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param isPublicKey    True to use public key, false to use private key.
     * @param transformation The name of the transformation, error.g., <i>DES/CBC/PKCS1Padding</i>.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of RSA encryption or decryption
     */
    private static byte[] rsaTemplate(
            final byte[] data,
            final byte[] key,
            final boolean isPublicKey,
            final String transformation,
            final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return new byte[0];
        }
        try {
            Key rsaKey;
            if (isPublicKey) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
                rsaKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            } else {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
                rsaKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            }
            if (rsaKey == null) {
                return new byte[0];
            }
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, rsaKey);
            int len = data.length;
            int maxLen = isEncrypt ? 117 : 128;
            int count = len / maxLen;
            if (count > 0) {
                byte[] ret = new byte[0];
                byte[] buff = new byte[maxLen];
                int index = 0;
                for (int i = 0; i < count; i++) {
                    System.arraycopy(data, index, buff, 0, maxLen);
                    ret = joins(ret, cipher.doFinal(buff));
                    index += maxLen;
                }
                if (index != len) {
                    int restLen = len - index;
                    buff = new byte[restLen];
                    System.arraycopy(data, index, buff, 0, restLen);
                    ret = joins(ret, cipher.doFinal(buff));
                }
                return ret;
            } else {
                return cipher.doFinal(data);
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return new byte[0];
    }

    private static byte[] joins(final byte[] prefix, final byte[] suffix) {
        byte[] ret = new byte[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, ret, 0, prefix.length);
        System.arraycopy(suffix, 0, ret, prefix.length, suffix.length);
        return ret;
    }
}
