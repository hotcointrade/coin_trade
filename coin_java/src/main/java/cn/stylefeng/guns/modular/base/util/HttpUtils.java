package cn.stylefeng.guns.modular.base.util;

import cn.stylefeng.guns.modular.base.state.F;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 20000;
	
	static {
		// 设置连接池?
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大值?
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		// Validate connections after 1 sec of inactivity
		connMgr.setValidateAfterInactivity(1000);
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时?
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		
		requestConfig = configBuilder.build();
	}
	
	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 *
	 * @param url
	 * @return
	 */
	public static JSONObject doGet(String url) {
		return doGet(url, new HashMap<String, Object>());
	}
	
	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject doGet(String url, Map<String, Object> params) {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			} else {
				param.append("&");
			}
			
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		String result = null;
		HttpClient httpClient = null;
		System.err.println("url：" + apiUrl);
		if (apiUrl.startsWith("https")) {
			httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = IOUtils.toString(instream, "UTF-8");
			}
		} catch (IOException e) {
		
		}
		return JSON.parseObject(result);
	}
	
	public static JSONObject doGetS(String url, Map<String, String> params) {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			} else {
				param.append("&");
			}
			
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		String result = null;
		HttpClient httpClient = null;
		System.err.println("url：" + apiUrl);
		if (apiUrl.startsWith("https")) {
			httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = IOUtils.toString(instream, "UTF-8");
			}
		} catch (IOException e) {
		
		}
		return JSON.parseObject(result);
	}
	
	/**
	 * 发送 POST 请求（HTTP），不带输入数据
	 *
	 * @param apiUrl
	 * @return
	 */
	public static JSONObject doPost(String apiUrl) {
		return doPost(apiUrl, new HashMap<String, Object>());
	}
	
	/**
	 * 发送 POST 请求，K-V形式
	 *
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static JSONObject doPost(String apiUrl, Map<String, Object> params) {
		CloseableHttpClient httpClient = null;
		if (apiUrl.startsWith("https")) {
			httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		
		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			System.err.println(httpPost);
			response = httpClient.execute(httpPost);
			System.err.println(response);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
		
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
				
				}
			}
		}
		return JSON.parseObject(httpStr);
	}
	
	/**
	 * 发送 POST 请求，JSON形式
	 *
	 * @param apiUrl
	 * @param json   json对象
	 * @return JSONObject
	 */
	public static JSONObject doPost(String apiUrl, Object json) {
		CloseableHttpClient httpClient = null;
		if (apiUrl.startsWith("https")) {
			httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		
		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
		
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
				
				}
			}
		}
		return JSON.parseObject(httpStr);
	}
	
	/**
	 * 创建SSL安全连接
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
				
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		
		
		return sslsf;
	}
	
	/**
	 * 创建钱包地址
	 */
	public static JSONObject setTransactionByTxid(String memberCode, String key, String secret, String coin, Long id, String createUrl) {
		TreeMap<String, Object> params = new TreeMap<>();
		//时间戳
		params.put("timeStamp", System.currentTimeMillis());
		//用户id
		params.put("userId", id.toString());
		//商户编码
		params.put("memberCode", memberCode);
		//币种code
		params.put("coin", coin);
		//随机字符串
		params.put("nonceStr", GenUtils.toSerialCode(id));
		//创建签名字符串
		String sign = SignUitls.getSign(params, secret, key);
		//商户公钥
		params.put("key", key);
		//签名字符串
		params.put("sign", sign);
		JSONObject httpPost = HttpUtils.doPost(createUrl, params);
		F.me().wlog("创建钱包地址", createUrl, httpPost.toJSONString());
		return httpPost;
	}
	
	/**
	 * 发送提币交易
	 */
	public static JSONObject sendCoinWithdraw(String memberCode, String key, String secret, String serialNo, String coin, Long id, String toaddress, String mentionUrl, BigDecimal amount) {
		TreeMap<String, Object> params = new TreeMap<>();
		//时间戳
		params.put("timeStamp", System.currentTimeMillis());
		//商户编码
		params.put("memberCode", memberCode);
		//用户id
		params.put("uid", id);
		//备注
		params.put("remark", "");
		//币种code
		params.put("coin", coin);
		//业务提幣id
		params.put("businessId", serialNo);
		//接收hash地址
		params.put("toAddress", toaddress);
		//提幣数量
		params.put("amount", amount);
		//随机字符串
		params.put("nonceStr", GenUtils.toSerialCode(id));
		//创建签名字符串
		String sign = SignUitls.getSign(params, secret, key);
		//商户公钥
		params.put("key", key);
		//签名字符串
		params.put("sign", sign);
		JSONObject httpPost = HttpUtils.doPost(mentionUrl, params);
		F.me().wlog("发送提币交易", mentionUrl, httpPost.toJSONString());
		return httpPost;
	}
	
	/**
	 * 校验地址
	 */
	public static JSONObject checkAddress(String memberCode, String key, String secret, String coin, Long id, String address, String checkAddressUrl) {
		TreeMap<String, Object> params = new TreeMap<>();
		//时间戳
		params.put("timeStamp", System.currentTimeMillis());
		//商户编码
		params.put("memberCode", memberCode);
		//币种code
		params.put("coin", coin);
		
		//校验地址
		params.put("address", address);
		
		//随机字符串
		params.put("nonceStr", GenUtils.toSerialCode(id));
		//创建签名字符串
		String sign = SignUitls.getSign(params, secret, key);
		//商户公钥
		params.put("key", key);
		//签名字符串
		params.put("sign", sign);
		JSONObject httpPost = HttpUtils.doPost(checkAddressUrl, params);
		F.me().wlog("校验地址", checkAddressUrl, httpPost.toJSONString());
		return httpPost;
	}
	
	/**
	 * 查询余额
	 *
	 * @param coin              coin
	 * @param key               key
	 * @param memberCode        memberCode
	 * @param secret            secret
	 * @param accountBalanceUrl accountBalanceUrl
	 * @return JSONObject
	 */
	public static JSONObject accountBalance(String coin, String key, String memberCode, String secret, String accountBalanceUrl) {
		TreeMap<String, Object> params = new TreeMap<>();
		//时间戳
		params.put("timeStamp", System.currentTimeMillis());
		//商户编码
		params.put("memberCode", memberCode);
		//币种code
		params.put("coin", coin);
		
		//随机字符串
		params.put("nonceStr", GenUtils.toSerialCode(1L));
		//创建签名字符串
		String sign = SignUitls.getSign(params, secret, key);
		//商户公钥
		params.put("key", key);
		//签名字符串
		params.put("sign", sign);
		JSONObject httpPost = HttpUtils.doPost(accountBalanceUrl, params);
		F.me().wlog("校验地址", accountBalanceUrl, httpPost.toJSONString());
		return httpPost;
	}
	
	/**
	 * 查询主账户地址
	 *
	 * @param coin               coin
	 * @param key                key
	 * @param memberCode         memberCode
	 * @param secret             secret
	 * @param findMainAccountUrl findMainAccountUrl
	 * @return JSONObject
	 */
	public static JSONObject findMainAccount(String coin, String key, String memberCode, String secret, String findMainAccountUrl) {
		TreeMap<String, Object> params = new TreeMap<>();
		//时间戳
		params.put("timeStamp", System.currentTimeMillis());
		//商户编码
		params.put("memberCode", memberCode);
		//币种code
		params.put("coin", coin);
		
		//随机字符串
		params.put("nonceStr", GenUtils.toSerialCode(1L));
		//创建签名字符串
		String sign = SignUitls.getSign(params, secret, key);
		//商户公钥
		params.put("key", key);
		//签名字符串
		params.put("sign", sign);
		System.out.println("coin=" + coin + "&key=" + key + "&memberCode=" + memberCode + "&nonceStr=" + params.get("nonceStr") + "&sign=" + sign + "&timeStamp=" + params.get("timeStamp"));
		JSONObject httpPost = HttpUtils.doPost(findMainAccountUrl, params);
		F.me().wlog("查询主账户地址", findMainAccountUrl, httpPost.toJSONString());
		return httpPost;
	}
	
}
