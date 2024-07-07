package cn.stylefeng.guns.modular.base.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * AES加密解密字符串工具类
 * 概述：AES高级加密标准，是对称密钥加密中最流行的算法之一；
 *       工作模式包括：ECB、CBC、CTR、OFB、CFB；
 * 使用范围：该工具类仅支持ECB模式下的：
 *              填充：PKCS7PADDING
 *              数据块：128位
 *              密码（key）：32字节长度（例如：12345678901234567890123456789012）
 *              偏移量（iv）：16字节长度（例如：1234567890123456）
 *              输出：hex
 *              字符集：UTF-8
 * 验证方式：http://tool.chacuo.net/cryptaes（在线AES加密解密）
 */
public class AesECBUtil {


        // 加密
        public static String Encrypt(String sSrc, String sKey) throws Exception {
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        }

        // 解密
        public static String Decrypt(String sSrc, String sKey) throws Exception {
            try {
                // 判断Key是否正确
                if (sKey == null) {
                    System.out.print("Key为空null");
                    return null;
                }
                // 判断Key是否为16位
                if (sKey.length() != 16) {
                    System.out.print("Key长度不是16位");
                    return null;
                }
                byte[] raw = sKey.getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original,"utf-8");
                    return originalString;
                } catch (Exception e) {
                    System.out.println(e.toString());
                    return null;
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
                return null;
            }
        }

        public static void main(String[] args) throws Exception {
            /*
             * 此处使用AES-128-ECB加密模式，key需要为16位。
             */
            String cKey = "2018@#$%bijiaoyi";
            // 需要加密的字串
            String cSrc = "123456";
            System.out.println(cSrc);
            // 加密
            String enString = AesECBUtil.Encrypt(cSrc, cKey);
            System.out.println("加密后的字串是：" + enString);

            // 解密
            String DeString = AesECBUtil.Decrypt(enString, cKey);
            System.out.println("解密后的字串是：" + DeString);
        }


}