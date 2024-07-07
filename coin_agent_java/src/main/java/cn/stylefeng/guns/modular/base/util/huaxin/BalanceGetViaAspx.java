/**
 * 获取余额接口
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

public class BalanceGetViaAspx {

	private static CloseableHttpClient httpclient;
	
	public final static void balance(){
		try
		{
			httpclient = SSLClient.createSSLClientDefault();
			//接口地址
			String url = "";
			//账号名
			String accountName="";
			//接口密码
			String password="";
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("action","overage"));
			nvps.add(new BasicNameValuePair("userid", ""));
			nvps.add(new BasicNameValuePair("account", accountName));
			nvps.add(new BasicNameValuePair("password", MD5.GetMD5Code(password)));
			post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			HttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				// 将字符转化为XML
				String returnString=EntityUtils.toString(entity);
				System.out.println(returnString);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}