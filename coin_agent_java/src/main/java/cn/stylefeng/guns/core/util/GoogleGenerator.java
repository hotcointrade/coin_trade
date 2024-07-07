package cn.stylefeng.guns.core.util;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GoogleGenerator {

    // 生成的key长度( Generate secret key length)
    public static final int SECRET_SIZE = 10;

    public static final String SEED = "btGjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDDECXSArvcZx";
    // Java实现随机数算法
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    // 最多可偏移的时间
    int window_size = 0; // default 3 - max 17

    public static String generateSecretKey ( ) {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance ( RANDOM_NUMBER_ALGORITHM );
            sr.setSeed (Base64.decodeBase64 ( SEED ) );
            byte[] buffer = sr.generateSeed ( SECRET_SIZE );
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode ( buffer );
            String encodedKey = new String ( bEncodedKey );
            return encodedKey;
        } catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }

    /**
     * 根据user和secret生成二维码的密钥
     *
     * @param user
     * @param host
     * @param secret
     * @return
     */
    public static String getQRBarcodeURL ( String user , String host , String secret ) {
        String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
        return String.format ( format , user , host , secret );
    }

    /**
     * 这个format不可以修改，身份验证器无法识别二维码
     *
     * @param user
     * @param secret
     * @return
     */
    public static String getQRBarcode ( String user , String secret ) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format ( format , user , secret );
    }

    public boolean check_code ( String secret , String code , long timeMsec ) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode ( secret );
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verify_code ( decodedKey , t + i );
            } catch (Exception e) {
                e.printStackTrace ();
                throw new RuntimeException ( e.getMessage () );
            }
            System.out.println ( "code=" + code );
            System.out.println ( "hash=" + hash );
            if (code.equals ( addZero ( hash ) )) {
                return true;
            }
        }
        return false;
    }

    private static int verify_code ( byte[] key , long t ) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec ( key , "HmacSHA1" );
        Mac mac = Mac.getInstance ( "HmacSHA1" );
        mac.init ( signKey );
        byte[] hash = mac.doFinal ( data );
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }


    private String addZero ( long code ) {
        return String.format ( "%06d",code );
    }

    public static void main(String[] args) {
    	String secret = generateSecretKey();
		// 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
//		String qrcode = getQRBarcode("649281612@qq.com", secret);
		System.out.println("key:" + secret);
//        String secret ="25XNN7YNUV7UPZOP";
//        String code = "054355";
//        long t = System.currentTimeMillis();
//        GoogleGenerator ga = new GoogleGenerator();
//        boolean r = ga.check_code(secret,code,t);
//        System.out.println("检查code是否正确？" + r);
    }

}
