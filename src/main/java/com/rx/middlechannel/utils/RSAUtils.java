package com.rx.middlechannel.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-05-19 11:13:52
 * @version: 1.0
 * @describe:
 */
public class RSAUtils {

    private final static String publicKey  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoZKzlx71AiGLHGmd2253uEvaEgKecrgXnGLNk9f/wIbxzkONNB5K02HuYuA8zzir+QQ8fnDDp1kT30p8ozN+s+ncjdvc5DU5JU2kG9YzvbfpQATZ+RkNUxZqdKH2YQxqW0xZHwQiT0Qr+GolHEVLIDfzo/KlPyczYiqxVVos18RBXZ/+/mRni1mRjSdw4KNeAFoBNIhgHDcQ/oCLs03QxH8BwJ7+jTSp6Skpu9WGm1Sf3qQUOGh8aHLgMjgtOb5hCaUSy30UlvdjjlZSANfGxrztGk/PzkgKRLT3pHgfY+lH+GWtCuBLJGqLUSABvhxG28VluWB4sLLk7Rn+MO6HlQIDAQAB";
    private final static String privateKey  = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQChkrOXHvUCIYscaZ3bbne4S9oSAp5yuBecYs2T1//AhvHOQ400HkrTYe5i4DzPOKv5BDx+cMOnWRPfSnyjM36z6dyN29zkNTklTaQb1jO9t+lABNn5GQ1TFmp0ofZhDGpbTFkfBCJPRCv4aiUcRUsgN/Oj8qU/JzNiKrFVWizXxEFdn/7+ZGeLWZGNJ3Dgo14AWgE0iGAcNxD+gIuzTdDEfwHAnv6NNKnpKSm71YabVJ/epBQ4aHxocuAyOC05vmEJpRLLfRSW92OOVlIA18bGvO0aT8/OSApEtPekeB9j6Uf4Za0K4EskaotRIAG+HEbbxWW5YHiwsuTtGf4w7oeVAgMBAAECggEANfCa4g2kr/Rw06hy2cnUUAOKps6XBSduQng9t2i56hVi7oBgyJCAXHdqNTo1MtzxlK2UYIXW26yCVN4yI+3SE9A0IGvRH6KhFFIKQnzNQz39sbCs8FsrtpxaJ9vWO+oAK8CeVBt2NM5Ge6U8BGchwHzNauNbxWyZ2+Dy2NLE+7byZ5FXQtL1h2BAO6OlsGAV/8PM8e7YAGsQAwL/qVHLIszkT47MLuCkN0GYPkmvQkDfMP6DgGj3YqsQ+8FTdRqMFCzlHgazB4KFgKXlMQGb0JjXkjWornDvTnPVIO1xlhRFrc4CuqcvW1ZxQL29hIjjtGAJmgBMFtl5qrk3j7QS1QKBgQDOtOj60v2M7OO3AvogKW6M5tsQ4qQmtvO6oXXj/bFxbUiVrr/ZMGXe3lmyA9woQdRJGVT05178Zw/rVD/KGVTNke51YH7RCGe8XSw7yJ7Ij/UySHXzZPTwquTc8OIgYUyKV8qUSScsjTjsA+95E8+HBXOzv8n+xogWeMIl5ucmtwKBgQDIGnXnX0S7BeuyOGnu0MNouB/0qfoa9Xw+ny+HJIFBrCLVdvDyHgAZTz0J/Z4btvoRaRNlZJ5e4ozEap73xHrVHKWfPoJdRcm2P7cnC+EWbpu7rFv2aMR4oSjJ+Cg5xtu9f20eS4wAXI1cVg8OGNTrZVcK71/PNhMqO4mazYaYEwKBgFUmVxcEFWiZKRD5y/m1t5GZg8KH6mD8WpiV7I93sUHtZ8opS2R3uG8/nylix61ZuM5H/iRIr2tANWGWuhRRrQplxBpcHsGTOdBxfUg/U5GBI9uL1/LksrX1YAPXwv7dtf8DPTMqk1WZ80neMSbqT0HqsAfqyJYISIUhR7D/VlcvAoGATTlI/z3Y/IRQbbsvxqZyg1trmbRvfS/3/wLYCMOhzoJr/w2j9wx05U+bMtvZxMBcyZNPlPW72Mp9oN+08dG1xcEEeexZyz0/l0IHoN4Iw+tz1n2yD+pmyFcJssg+Xe3Mp9khd2uYYBtXwqbDsKEMqru1V9cBlqo7wzSu6jsfElkCgYEApNGhG17OLcXEdYoMq5+Iemh1U+w4DAvUXXVQGHET1JqZjN6qgo0OR4IDInM3sGBCSgb/is1gwdGGz6Q+J5cqHactxH/Qf0G2KFLwpdMPbP1U1epY6qlrbs8ajJZuE+BIdox4Dn1oHMYeTzTFRdSmh+eDiDuQRxglbKB+DTh5MTY=";


    public static String getPublicKey(){
        return publicKey;
    }

    public static String getPrivateKey(){
        return privateKey;
    }

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) {
        try {
            String encrypt = encrypt("abc", publicKey);
            System.out.println(encrypt);
            String decrypt = decrypt(encrypt, privateKey);
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
