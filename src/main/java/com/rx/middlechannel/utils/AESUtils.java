package com.rx.middlechannel.utils;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;

/**
 * @author： KeA
 * @date： 2021-06-18 14:36:06
 * @version: 1.0
 * @describe:
 */
public class AESUtils {

    private static String yKey = "rongxiaozhongjianshujuduijie";
    private static String sKey = "dda82483de867d64cc6074466db80ae2";

    /**
     * 生成密钥
     */
    public static void genKey(){
        //随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

    }

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encrypt(String content){
        //获取密钥
        byte[] key = sKey.getBytes();

        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        //加密为Base64表示
        String encryptBase64 = Base64Encoder.encode(aes.encrypt(content));
        return encryptBase64;
        //解密为字符串
    }

    /**
     * 解密
     * @param encrypt
     * @return
     */
    public static String decrypt(String encrypt){
        byte[] key = sKey.getBytes();

        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        String decryptStr = aes.decryptStr(encrypt, CharsetUtil.CHARSET_UTF_8);

        return decryptStr;
    }

    public static void main(String[] args) {
        String content = "{\"salt\":\"rongxiaointerface\",\"deviceId\":\"39\",\"oldPassword\":\"010203\",\"newPassword\":\"203040\"}";
        String encrypt = AESUtils.encrypt(content);
        System.out.println(encrypt);
        String decrypt = AESUtils.decrypt(encrypt);
        System.out.println(decrypt);
    }
}
