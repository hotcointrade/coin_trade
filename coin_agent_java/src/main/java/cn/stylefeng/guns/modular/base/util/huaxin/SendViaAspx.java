/*
 *  HTTPS接口发送短信
 */
package cn.stylefeng.guns.modular.base.util.huaxin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class SendViaAspx {
	private static CloseableHttpClient httpclient;
	public final static void sendSms() throws Exception{
		 httpclient = SSLClient.createSSLClientDefault();
		//接口地址
		String url = "http://dx.ipyy.net/sms.aspx";
		//用户ID。
		String userid="";
		//用户账号名
		String account="";
		//接口密码
		String password="";
		//多个手机号用逗号分隔
		String mobile="";
		String text = "【齐赚】您好，您的验证码是123456，该验证码2分钟内有效，请勿泄露于他人。";
		String sendTime="";	
		//扩展号，没有请留空
		String extno="";
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/x-www-form-urlencoded");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action","send"));
		nvps.add(new BasicNameValuePair("userid", userid));
		nvps.add(new BasicNameValuePair("account", account)); 	
		nvps.add(new BasicNameValuePair("password", MD5.GetMD5Code(password)));		
		nvps.add(new BasicNameValuePair("mobile", mobile));		
		nvps.add(new BasicNameValuePair("content", text));
		nvps.add(new BasicNameValuePair("sendTime", sendTime));
		nvps.add(new BasicNameValuePair("extno", extno));
		post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
		HttpResponse response = httpclient.execute(post);
		HttpEntity entity = response.getEntity();
		String returnString=EntityUtils.toString(entity);
		System.out.println(returnString);
		EntityUtils.consume(entity);
	}
	
}