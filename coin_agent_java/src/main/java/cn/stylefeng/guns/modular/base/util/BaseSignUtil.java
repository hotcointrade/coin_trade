package cn.stylefeng.guns.modular.base.util;

import java.util.Map;

/**
 * 基础签名操作
 */
public class BaseSignUtil
{

    final static boolean isDebugger = false;

    /**
     * 进行加签
     *
     * @param key      用户的key
     * @param valueMap 需要签名的集合，未处理前的
     * @return 处理后，返回的签名值
     */
    public static String getSign(String key, Map<String, String> valueMap) {

        String soreValueMap = SortUtils.formatUrlParam(valueMap, "utf-8", true);//对参数按key进行字典升序排列

        String signValue = soreValueMap+key;//将key拼接在请求参数的后面

        String md5SignVlues = MD5Utils.MD5Encode(signValue, "utf8");//形成MD5加密后的签名

        return md5SignVlues;

    }

    /**
     * 进行验签操作
     *
     * @param valueMap 请求参数
     * @param sign     接口调用方传过来的sign
     * @param key      系统存储的商户key
     * @return 验签成功返回true  否则返回false
     */
    public static boolean verifySign(Map<String, String> valueMap, String sign, String key) {
        if(isDebugger)
        {

            System.out.println("服务器接收签名为:" + sign);
        }


        String soreValueMap = SortUtils.formatUrlParam(valueMap, "utf-8", true);//对参数按key进行字典升序排列

        String signValue = soreValueMap+key;//将key拼接在请求参数的后面

        String md5SignVlues = MD5Utils.MD5Encode(signValue, "utf8");//形成MD5加密后的签名

        if(isDebugger){
            System.out.println("服务端处理得到签名为:" + md5SignVlues);
        }


        if (md5SignVlues.equals(sign)) {
            return true;
        }

        return false;
    }

//    public static void main(String[] args) {
//        //NotifyUrlVo(orderNo=G156895222470460d8486, type=WECHAT, money=1000.00, status=SUCCESS, timeEnd=1568960262980, sign=e843e04f53e58be099e21d41462fd58a)
//        String key = "key3472894";
//
////        String uid = "MT34728492";
//        Map<String, String> map = new HashMap<>();
////        map.put("orderNo","G156895222470460d8486");
//        map.put("type", "fds");
//        map.put("UID", "MT34728492");
//        map.put("money", "100");
//        map.put("extparm", "EX3724938432");
//        map.put("notifyUrl", "http://www.baidu.com:8999/calback");
//        //f5817174e8453e98dfbe6edc58cde0d4
//        String getSign = getSign(key, map);
//        Map<String, String> verifyMap = new HashMap<>();
////        verifyMap.put("UID",uid);
//        verifyMap.put("money", "200");
//        verifyMap.put("type", "WECHAT");
//
//        System.out.println("验签结果;" + verifySign(map, getSign, key));
//
//    }


}
