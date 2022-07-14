package com.bsks.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SM2Utils {

    private static String privateKeyPath = "privateKey.pem";
    private static String publicKeyPath = "publicKey.pem";
    private static String algorithm = "SM2";// 不可修改

    /**
     * 创建公秘钥文档
     * @throws IOException
     */
    public static void createKeyPem() throws IOException {
        KeyPair pair = SecureUtil.generateKeyPair(algorithm);
        byte[] privateKeyByte = pair.getPrivate().getEncoded();
        byte[] publicKeyByte = pair.getPublic().getEncoded();
        writeBytesToFile(privateKeyByte, privateKeyPath);
        writeBytesToFile(publicKeyByte, publicKeyPath);
    }

    /**
     * 获取秘钥
     * @return
     * @throws IOException
     */
    public static PrivateKey getPrivateKey() throws IOException {
        File privateKeyFile = new File(privateKeyPath);
        if (!privateKeyFile.exists()){
            createKeyPem();
        }
        return SecureUtil.generatePrivateKey(algorithm, getBytesFromFile(privateKeyFile));
    }

    /**
     * 获取公钥
     * @return
     * @throws IOException
     */
    public static PublicKey getPublicKey() throws IOException {
        File publicKeyFile = new File(publicKeyPath);
        if (!publicKeyFile.exists()){
            createKeyPem();
        }
        return SecureUtil.generatePublicKey(algorithm, getBytesFromFile(publicKeyFile));
    }

    /**
     * 使用公钥加密
     * @param data
     * @return
     */
    public static String encrypt(String data) throws IOException {
        SM2 sm2 = new SM2(getPrivateKey(),getPublicKey());
        return sm2.encryptBcd(data, KeyType.PublicKey);
    }

    /**
     * 使用私钥解密
     * @param encryptStr
     * @return
     */
    public static String decrypt(String encryptStr) throws IOException {
        SM2 sm2 = new SM2(getPrivateKey(),getPublicKey());
        return StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
    }

    /**
     * 生成签名成String
     * @param data
     * @return
     */
    public static String signToString(String data) throws IOException {
        SM2 sm2 = new SM2(getPrivateKey(),getPublicKey());
        return sm2.signHex(data);
    }

    /**
     * 生成签名Byte
     * @param data
     * @return
     */
    public static byte[] sign(byte[] data) throws IOException {
        SM2 sm2 = new SM2(getPrivateKey(),getPublicKey());
        return sm2.sign(data);
    }

    /**
     * 验签
     * @param data
     * @return
     */
    public static boolean verify(byte[] data,byte[] sign) throws IOException {
        System.out.println(getPrivateKey());
        SM2 sm2 = new SM2(getPrivateKey(),getPublicKey());
        return sm2.verify(data,sign);
    }

    /**
     * byte变成文件
     * @param bs
     * @param path
     * @throws IOException
     */
    public static void writeBytesToFile(byte[] bs, String path) throws IOException {
        OutputStream out = new FileOutputStream(path);
        InputStream is = new ByteArrayInputStream(bs);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }

    /**
     * 文件变成byte
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);// 获取文件大小
        long lengths = file.length();
        if (lengths > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) lengths];// 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
