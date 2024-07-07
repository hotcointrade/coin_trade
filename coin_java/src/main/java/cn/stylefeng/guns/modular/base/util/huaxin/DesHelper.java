/**
 * DES加密解密
 */
package cn.stylefeng.guns.modular.base.util.huaxin;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;

public class DesHelper {

	private final static String DES = "DES";

	/**
	 * Description 根据key对data进行加密
	 * 
	 * @param data
	 *            要加密的字符串
	 * @param key
	 *            加密键
	 * @return DES加密并进行Base64编码后的字符串
	 * @throws Exception
	 */
	public static String Encrypt(String data, String key) throws Exception {
		byte[] btData = data.getBytes("utf-8");
		byte[] btKey = key.getBytes("utf-8");
		// 获取加密的key
		byte[] nKey = new byte[8];
		System.arraycopy(btKey, 0, nKey, 0, btKey.length > 8 ? 8 : btKey.length);
		byte[] bt = Encrypt(btData, nKey);
		String strs = Base64.encodeBase64String(bt);
		return strs;
	}

	/**
	 * Description 根据key对data进行加密
	 * 
	 * @param data
	 *            要加密的数据，byte数组
	 * @param key
	 *            加密键，byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] Encrypt(byte[] data, byte[] key) throws Exception {
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(key);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String Decrypt(String data, String key) throws IOException,
			Exception {
		if (data == null)
			return null;
		byte[] buf = Base64.decodeBase64(data);
		byte[] bt = Decrypt(buf, key.getBytes());
		return new String(bt);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] Decrypt(byte[] data, byte[] key) throws Exception {
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(key);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		return cipher.doFinal(data);
	}

}
