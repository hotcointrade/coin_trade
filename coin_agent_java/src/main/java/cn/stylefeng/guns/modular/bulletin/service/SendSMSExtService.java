package cn.stylefeng.guns.modular.bulletin.service;

import cn.hutool.json.JSONUtil;
import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.HttpUtil;
import cn.stylefeng.guns.modular.base.util.MD5Utils;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.huaxin.MD5;
import cn.stylefeng.guns.modular.base.util.huaxin.SSLClient;
import cn.stylefeng.roses.core.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SendSMSExtService
{
	@Resource
	RedisUtil redisUtil;
	// 华信短信:  smsAccount smsPassword
	private static final String smsAccount = F.me().getSysConfigValueByCode(Constant.SMS_ACCOUNT);
	private static final String smsPassword = F.me().getSysConfigValueByCode(Constant.SMS_PASSWORD);

	private static final String smsUrl = F.me().getSysConfigValueByCode(Constant.SMS_URL);
	private static final String smsUrlI18 = F.me().getSysConfigValueByCode(Constant.SMS_URL_I18);

	private static final String apiKey = F.me().getSysConfigValueByCode(Constant.SMS_API_KEY);
	private static CloseableHttpClient httpclient;

	private static final String isSuccess = "0";
	private static final String smsAccountI18 = F.me().getSysConfigValueByCode(Constant.SMS_N18_ACCOUNT);
	private static final String smsPasswordI18 = F.me().getSysConfigValueByCode(Constant.SMS_N18_PASSWORD);
    @Resource
    private GunsProperties gunsProperties;


    public  boolean sendMeiLian(String mobile,String content){
		//连接超时及读取超时设置
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); //连接超时：30秒
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");	//读取超时：30秒
		//新建一个StringBuffer链接
		StringBuffer buffer = new StringBuffer();
		//String encode = "GBK"; //页面编码和短信内容编码为GBK。重要说明：如提交短信后收到乱码，请将GBK改为UTF-8测试。如本程序页面为编码格式为：ASCII/GB2312/GBK则该处为GBK。如本页面编码为UTF-8或需要支持繁体，阿拉伯文等Unicode，请将此处写为：UTF-8
		String encode = "UTF-8";
		String username = smsAccount;  //用户名
		String password_md5 = MD5Util.encrypt(smsPassword);  //密码
		String apikey =apiKey;  //apikey秘钥（请登录 http://m.5c.com.cn 短信平台-->账号管理-->我的信息 中复制apikey）
		try {
			String contentUrlEncode = URLEncoder.encode(content,encode);  //对短信内容做Urlencode编码操作。注意：如
			//把发送链接存入buffer中，如连接超时，可能是您服务器不支持域名解析，请将下面连接中的：【m.5c.com.cn】修改为IP：【115.28.23.78】
			buffer.append("http://m.5c.com.cn/api/send/index.php?username="+username+"&password_md5="+password_md5+"&mobile="+mobile+"&apikey="+apikey+"&content="+contentUrlEncode+"&encode="+encode);
			//System.out.println(buffer); //调试功能，输入完整的请求URL地址
			//把buffer链接存入新建的URL中
			URL url = new URL(buffer.toString());
			//打开URL链接
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			//使用POST方式发送
			connection.setRequestMethod("POST");
			//使用长链接方式
			connection.setRequestProperty("Connection", "Keep-Alive");
			//发送短信内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			//获取返回值
			String result = reader.readLine();
			//输出result内容，查看返回值，成功为success，错误为error，详见该文档起始注释
			System.out.println(result);
			if (result.contains("success")){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  false;
	}

    public boolean sendSms(String content, String phone,Long typeId) {
//		try
//		{
//			String userid="";
//			//用户账号名
//			String account=smsAccount;
//			//接口密码
//			String password=smsPassword;
//			httpclient = SSLClient.createSSLClientDefault();
//			//接口地址
//			String url = "http://sh2.ipyy.com/sms.aspx";
//			//用户ID。
//			if(typeId>0) {
//				try {
//					content= encodeHexStr(8, content);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				url = "https://dx.ipyy.net/I18NSms.aspx";
//				account = smsAccountI18;
//				password = smsPasswordI18;
//			}
//			//多个手机号用逗号分隔
//			String mobile=phone;
//			String text = content;
//			String sendTime="";
//			//扩展号，没有请留空
//			String extno="";
//			HttpPost post = new HttpPost(url);
//			post.setHeader("Content-type", "application/x-www-form-urlencoded");
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("action","send"));
//			nvps.add(new BasicNameValuePair("userid", userid));
//			if (typeId >0){
//				nvps.add(new BasicNameValuePair("code", "0"));
//			}
//			nvps.add(new BasicNameValuePair("account", account));
//			nvps.add(new BasicNameValuePair("password", MD5.GetMD5Code(password).toUpperCase()));
//			nvps.add(new BasicNameValuePair("mobile", mobile));
//			nvps.add(new BasicNameValuePair("content", text));
//			nvps.add(new BasicNameValuePair("sendTime", sendTime));
//			nvps.add(new BasicNameValuePair("extno", extno));
//			post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
//			log.info("请求地址："+url+",请求参数："+JSONObject.toJSONString(nvps));
//			HttpResponse response = httpclient.execute(post);
//			HttpEntity entity = response.getEntity();
//			String returnString=EntityUtils.toString(entity);
//			System.out.println(returnString);
//			EntityUtils.consume(entity);
//		}catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//
//
//		return true;
		//替换原有短信
		return sendSmsDXB(content, phone, typeId);
    }
	public boolean sendSmsDXB(String content, String phone,Long typeId) {
    	boolean flag = true;
		try
		{
			//String userid="";
			//用户账号名
			String account=smsAccount;
			//接口密码
			String password=smsPassword;
			httpclient = SSLClient.createSSLClientDefault();
			//接口地址
			String url = smsUrl;
			log.info("短信地址："+url);

			//短信地址拼接 get
			StringBuffer httpArg = new StringBuffer();
			httpArg.append("u=").append(account).append("&");
			httpArg.append("p=").append(MD5Utils.MD5Encode(password,null)).append("&");
			httpArg.append("m=").append(encodeUrlString(phone,"UTF-8")).append("&");
			content= encodeUrlString(content,"UTF-8");
			//用户ID。
			if(typeId>0) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
				//国际接口
				url = smsUrlI18;
				log.info("国际短信地址："+url);
			}
			httpArg.append("c=").append(content);
			redisUtil.incr(Constant.SMS_COUNT,1);
//			//多个手机号用逗号分隔
//			String mobile=phone;
//			String text = content;
//			String sendTime="";
//			//扩展号，没有请留空
//			String extno="";
//			HttpPost post = new HttpPost(url);
//			post.setHeader("Content-type", "application/x-www-form-urlencoded");
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("action","send"));
//			nvps.add(new BasicNameValuePair("userid", userid));
//			if (typeId >0){
//				nvps.add(new BasicNameValuePair("code", "0"));
//			}
//			nvps.add(new BasicNameValuePair("account", account));
//			nvps.add(new BasicNameValuePair("password", MD5.GetMD5Code(password).toUpperCase()));
//			nvps.add(new BasicNameValuePair("mobile", mobile));
//			nvps.add(new BasicNameValuePair("content", text));
//			nvps.add(new BasicNameValuePair("sendTime", sendTime));
//			nvps.add(new BasicNameValuePair("extno", extno));
//			post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			log.info("请求地址："+url + "?" + httpArg.toString());
			String returnString = HttpUtil.get(url + "?" + httpArg.toString());



			//HttpResponse response = httpclient.execute(post);
			//HttpEntity entity = response.getEntity();
			//String returnString=EntityUtils.toString(entity);
			System.out.println("短信发送接收状态码："+returnString);
			flag = returnString.equals(isSuccess)?true:false;
			//EntityUtils.consume(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public static String encodeUrlString(String str, String charset) {
		String strret = null;
		if (str == null)
			return str;
		try {
			strret = java.net.URLEncoder.encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strret;
	}

//
//    public boolean sendSms(String content, String phone,Long typeId) {
//    	boolean i=false;
//    	String r=organizationData(phone, content,typeId);
//    	String smsUrl = null;
//    	if(typeId ==1) {
//    		smsUrl = "https://dx.ipyy.net/sms.aspx?action=send&userid=&"+r;
//    	}else {
//    		smsUrl = "https://dx.ipyy.net/I18NSms.aspx?action=send&userid=&"+r;
//    	}
//        try {
//			System.out.println("请求："+smsUrl);
//            URL url = new URL(smsUrl);
//            URLConnection con = url.openConnection();
//            con.setDoOutput(true);
//            con.setRequestProperty("Cache-Control", "no-cache");
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//            String result=HttpUtil.sendPost(smsUrl,null);
//			System.out.println("发送验证码：返回内容"+result);
//            Document doc =null;
//			try {
//				doc = DocumentHelper.parseText(result);
//			} catch (DocumentException e) {
//				e.printStackTrace();
//			}
//			// 获取根节点
//			Element rootElt = doc.getRootElement();
//			// 获取根节点下的子节点的值
//			String returnstatus = rootElt.elementText("returnstatus").trim();
//            if(returnstatus.equals("Success")){
//            	i=true;
//            }else{
//            	i=false;
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return i;
//    }


    public static String organizationData(String phone, String content,Long typeId) {
        StringBuilder sendBuilder = new StringBuilder();
        if(typeId ==1) {
        	 sendBuilder.append("account="+smsAccount);//机构ID:用户登录名MYC001
             sendBuilder.append("&password="+smsPassword);//密码
        }else {
        	 sendBuilder.append("account="+smsAccountI18);//机构ID:用户登录名MYC001
             sendBuilder.append("&password="+smsPasswordI18);//密码
        }
        sendBuilder.append("&mobile=").append(phone);//接收手机号，多个号码间以逗号分隔且最大不超过100个号码
        try {
        	if(typeId ==1) {
        		sendBuilder.append("&content=").append(URLEncoder.encode(content, "UTF-8"));
        	}else {
        		sendBuilder.append("&content=").append(encodeHexStr(8, content));
        	}
            //发送内容,标准内容不能超过70个汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        sendBuilder.append("&sendTime=");//发送时间
//        sendBuilder.append("&extno=");
//        if(typeId ==1) {
//        	 sendBuilder.append("&code=8");
//        }else {
//        	 sendBuilder.append("&code=8");
//        }

        return sendBuilder.toString();
    }

 // 字符编码成HEX
 		public static String encodeHexStr(int dataCoding, String realStr) {
 			String strhex = "";
 			try {
 				byte[] bytSource = null;
 				if (dataCoding == 15) {
 					bytSource = realStr.getBytes("GBK");
 				} else if (dataCoding == 3) {
 					bytSource = realStr.getBytes("ISO-8859-1");
 				} else if (dataCoding == 8) {
 					bytSource = realStr.getBytes("UTF-16BE");
 				} else {
 					bytSource = realStr.getBytes("ASCII");
 				}
 				strhex = bytesToHexString(bytSource);

 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			return strhex;
 		}

 		/** */
		/**
		 * 把字节数组转换成16进制字符串
		 *
		 * @param bArray
		 * @return
		 */
		public static final String bytesToHexString(byte[] bArray) {
			StringBuffer sb = new StringBuffer(bArray.length);
			String sTemp;
			for (int i = 0; i < bArray.length; i++) {
				sTemp = Integer.toHexString(0xFF & bArray[i]);
				if (sTemp.length() < 2)
					sb.append("0");
				sb.append(sTemp.toUpperCase());
			}
			return sb.toString();
		}


    public boolean sendSingleSend(String smsAccount, String content) {
			boolean flag = false;
		//初始化clnt,使用单例方式
		YunpianClient clnt = new YunpianClient(apiKey).init();
		//发送短信API
		Map<String, String> param = clnt.newParam(2);
		param.put(YunpianClient.MOBILE, smsAccount);
		param.put(YunpianClient.TEXT, content);
		Result<SmsSingleSend> r = clnt.sms().single_send(param);
		log.info(JSONObject.toJSONString(r));
		//获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),
		// API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
		//账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().
		// * 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
		if (r.getCode().equals(0)){
			flag = true;
		}
		//释放clnt
		clnt.close();
		return flag;
	}
}
