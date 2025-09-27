package com.staoo.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 加密工具类
 * 提供AES和RSA加密解密的核心功能
 * @author staoo
 */
public class EncryptionUtils {
    // AES加密算法
    private static final String AES_ALGORITHM = "AES";
    // AES加密模式
    private static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    // RSA加密算法
    private static final String RSA_ALGORITHM = "RSA";
    // RSA密钥长度
    private static final int RSA_KEY_SIZE = 2048;
    // AES密钥长度
    private static final int AES_KEY_SIZE = 256;

    /**
     * AES加密
     * @param data 待加密的数据
     * @param key AES密钥
     * @return 加密后的数据（Base64编码）
     */
    public static String aesEncrypt(String data, String key) {
        try {
            // 创建AES密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(key), AES_ALGORITHM);
            // 创建加密器
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 执行加密
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // 返回Base64编码的加密结果
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     * @param encryptedData 加密的数据（Base64编码）
     * @param key AES密钥
     * @return 解密后的数据
     */
    public static String aesDecrypt(String encryptedData, String key) {
        try {
            // 创建AES密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(key), AES_ALGORITHM);
            // 创建解密器
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 执行解密
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
            // 返回解密结果
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * RSA加密
     * @param data 待加密的数据
     * @param publicKey Base64编码的RSA公钥
     * @return 加密后的数据（Base64编码）
     */
    public static String rsaEncrypt(String data, String publicKey) {
        try {
            // 获取公钥对象
            byte[] publicKeyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey rsaPublicKey = keyFactory.generatePublic(keySpec);

            // 创建加密器
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

            // 执行分段加密（RSA有长度限制）
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            int maxBlockSize = (RSA_KEY_SIZE / 8) - 11; // RSA最大加密块大小
            int inputLength = dataBytes.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            StringBuilder result = new StringBuilder();

            while (inputLength - offSet > 0) {
                if (inputLength - offSet > maxBlockSize) {
                    cache = cipher.doFinal(dataBytes, offSet, maxBlockSize);
                } else {
                    cache = cipher.doFinal(dataBytes, offSet, inputLength - offSet);
                }
                result.append(Base64.encodeBase64String(cache));
                i++;
                offSet = i * maxBlockSize;
            }

            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * RSA解密
     * @param encryptedData 加密的数据（Base64编码）
     * @param privateKey Base64编码的RSA私钥
     * @return 解密后的数据
     */
    public static String rsaDecrypt(String encryptedData, String privateKey) {
        try {
            // 获取私钥对象
            byte[] privateKeyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey rsaPrivateKey = keyFactory.generatePrivate(keySpec);

            // 创建解密器
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);

            // 执行分段解密
            byte[] encryptedDataBytes = Base64.decodeBase64(encryptedData);
            int maxBlockSize = RSA_KEY_SIZE / 8;
            int inputLength = encryptedDataBytes.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            StringBuilder result = new StringBuilder();

            while (inputLength - offSet > 0) {
                if (inputLength - offSet > maxBlockSize) {
                    cache = cipher.doFinal(encryptedDataBytes, offSet, maxBlockSize);
                } else {
                    cache = cipher.doFinal(encryptedDataBytes, offSet, inputLength - offSet);
                }
                result.append(new String(cache, StandardCharsets.UTF_8));
                i++;
                offSet = i * maxBlockSize;
            }

            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("RSA解密失败", e);
        }
    }

    /**
     * 生成RSA密钥对
     * @return RSA密钥对
     */
    public static KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(RSA_KEY_SIZE, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("生成RSA密钥对失败", e);
        }
    }

    /**
     * 生成随机AES密钥
     * @return Base64编码的AES密钥
     */
    public static String generateAesKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.encodeBase64String(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    /**
     * 将公钥转换为Base64编码的字符串
     * @param publicKey 公钥对象
     * @return Base64编码的公钥字符串
     */
    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 将私钥转换为Base64编码的字符串
     * @param privateKey 私钥对象
     * @return Base64编码的私钥字符串
     */
    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }
}