package cn.stylefeng.guns.modular.base.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.TreeMap;

public class SignUitls
{
    public static String sha256HMACToSign(String content, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(content.getBytes());

            StringBuffer buf = new StringBuffer();
            String stmp;
            for (int n = 0; bytes != null && n < bytes.length; n++) {
                stmp = Integer.toHexString(bytes[n] & 0XFF);
                if (stmp.length() == 1) {
                    buf.append('0');
                }
                buf.append(stmp);
            }
            return buf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSign(TreeMap<String, Object> params, String secret, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {

            if ("sign".equals(entrySet.getKey()) || "key".equals(entrySet.getKey())) {
                continue;
            } else {
                sb.append("&").append(entrySet.getKey()).append("=").append(entrySet.getValue());
            }
        }
        sb.delete(0, 1);
        // 添加key
        sb.append("&key=" + key);
        // 返回签名
        return sha256HMACToSign(sb.toString(), secret).toUpperCase();
    }
}
