package cn.stylefeng.guns.modular.base.util;

import cn.stylefeng.guns.modular.extension.ida.CashDto;

import java.security.MessageDigest;

public class CasherJavaDemo {
	

//	/**
//	 * 获取收银台地址
//	 * @param cashDto 入参
//	 * @return  收银台地址
//	 */
//	private static String getUrl(CashDto cashDto)
//	{
//		String pickupUrl=cashDto.getPickupUrl();
//		String receiveUrl=cashDto.getReceiveUrl();
//		String orderNo=cashDto.getOrderNo();
//		String orderAmount=cashDto.getOrderAmount();
//		String amount=cashDto.getAmount();
//		String orderCurrency=cashDto.getOrderCurrency();
//		String customerId=cashDto.getCustomerId();
//		String crmNo=cashDto.getCrmNo();
//		String coin=cashDto.getCoin();
//		String sign=cashDto.getSign();
//		String sign_=getSign(pickupUrl, receiveUrl, "MD5", orderNo, orderAmount, orderCurrency, customerId, sign);
//		String url=cashDto.getUrl()+"?signType=MD5&pickupUrl="+pickupUrl
//				+"&receiveUrl="+receiveUrl+"&orderNo="
//				+orderNo+"&orderAmount="+orderAmount+"&amount="+amount+"&orderCurrency="+orderCurrency
//				+"&customerId="+customerId+"&sign="+sign_+"&crmNo="+crmNo+"&coin="+coin
//				+"&type=digit";
////		System.out.println("收银台链接地址为:"+url);
//		return url;
//	}
//
//	private static String getSign(String pickupUrl,String receiveUrl,String signType,String orderNo,String coinsNum,String orderCurrency,String customerId,String sign){
//
//
//		String str=pickupUrl+receiveUrl+signType+orderNo+coinsNum+orderCurrency+customerId+sign;
//		String result=string2MD5(str);
//
//		return result;
//	}
//
//
//	public static String string2MD5(String inStr) {
//		MessageDigest md5 = null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//			return "";
//		}
//		char[] charArray = inStr.toCharArray();
//		byte[] byteArray = new byte[charArray.length];
//
//		for (int i = 0; i < charArray.length; i++)
//			byteArray[i] = (byte) charArray[i];
//		byte[] md5Bytes = md5.digest(byteArray);
//		StringBuffer hexValue = new StringBuffer();
//		for (int i = 0; i < md5Bytes.length; i++) {
//			int val = ((int) md5Bytes[i]) & 0xff;
//			if (val < 16)
//				hexValue.append("0");
//			hexValue.append(Integer.toHexString(val));
//		}
//		return hexValue.toString();
//
//	}
//
	
	
}
