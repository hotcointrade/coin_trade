package cn.stylefeng.guns.modular.base.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
	 public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            //1.获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            //2.中文有乱码的需要将PrintWriter改为如下
	            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        System.out.println("post推送结果："+result);
	        return result;
	    }
	
	 	/**
	 	 * Send a get request
	 	 * @param url
	 	 * @return response
	 	 * @throws IOException 
	 	 */
	 	static public String get(String url) throws IOException {
	 		return get(url, new HashMap<>());
	 	}
	 	static public String get(String url,String BM) throws IOException {
	 		return get(url, null,BM);
	 	}
	 	 

	 	/**
	 	 * Send a get request
	 	 * @param url         Url as string
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String get(String url,
	 			Map<String, String> headers) throws IOException {
	 		return fetch("GET", url, null, headers);
	 	}
	 	static public String get(String url,
	 			Map<String, String> headers,String BM) throws IOException {
	 		return fetch("GET", url, null, headers,BM);
	 	}

	 	/**
	 	 * Send a post request
	 	 * @param url         Url as string
	 	 * @param body        Request body as string
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String post(String url, String body,
	 			Map<String, String> headers) throws IOException {
	 		return fetch("POST", url, body, headers);
	 	}

	 	/**
	 	 * Send a post request
	 	 * @param url         Url as string
	 	 * @param body        Request body as string
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String post(String url, String body) throws IOException {
	 		return post(url, body, null);
	 	}

	 	/**
	 	 * Post a form with parameters
	 	 * @param url         Url as string
	 	 * @param params      map with parameters/values
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String postForm(String url, Map<String, String> params) 
	 			throws IOException {
	 		return postForm(url, params, null);
	 	}

	 	/**
	 	 * Post a form with parameters
	 	 * @param url         Url as string
	 	 * @param params      Map with parameters/values
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String postForm(String url, Map<String, String> params,
	 			Map<String, String> headers) throws IOException {
	 		// set content type
	 		if (headers == null) {
	 			headers = new HashMap<>();
	 		}
	 		headers.put("Content-Type", "application/x-www-form-urlencoded");

	 		// parse parameters
	 		String body = "";
	 		if (params != null) {
	 			boolean first = true;
	 			for (String param : params.keySet()) {
	 				if (first) {
	 					first = false;
	 				}
	 				else {
	 					body += "&";
	 				}
	 				String value = params.get(param);
	 				body += URLEncoder.encode(param, "UTF-8") + "=";
	 				body += URLEncoder.encode(value, "UTF-8");
	 			}
	 		}

	 		return post(url, body, headers);
	 	}

	 	/**
	 	 * Send a put request
	 	 * @param url         Url as string
	 	 * @param body        Request body as string
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String put(String url, String body,
	 			Map<String, String> headers) throws IOException {
	 		return fetch("PUT", url, body, headers);
	 	}

	 	/**
	 	 * Send a put request
	 	 * @param url         Url as string
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String put(String url, String body) throws IOException {
	 		return put(url, body, null);
	 	}
	 	
	 	/**
	 	 * Send a delete request
	 	 * @param url         Url as string
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String delete(String url,
	 			Map<String, String> headers) throws IOException {
	 		return fetch("DELETE", url, null, headers);
	 	}
	 	
	 	/**
	 	 * Send a delete request
	 	 * @param url         Url as string
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String delete(String url) throws IOException {
	 		return delete(url, null);
	 	}
	 	
	 	/**
	 	 * Append query parameters to given url
	 	 * @param url         Url as string
	 	 * @param params      Map with query parameters
	 	 * @return url        Url with query parameters appended
	 	 * @throws IOException 
	 	 */
	 	static public String appendQueryParams(String url, 
	 			Map<String, String> params) throws IOException {
	 		String fullUrl = url;
	 		if (params != null) {
	 			boolean first = (fullUrl.indexOf('?') == -1);
	 			for (String param : params.keySet()) {
	 				if (first) {
	 					fullUrl += '?';
	 					first = false;
	 				}
	 				else {
	 					fullUrl += '&';
	 				}
	 				String value = params.get(param);
	 				fullUrl += URLEncoder.encode(param, "UTF-8") + '=';
	 				fullUrl += URLEncoder.encode(value, "UTF-8");
	 			}
	 		}
	 		
	 		return fullUrl;
	 	}
	 	
	 	/**
	 	 * Retrieve the query parameters from given url
	 	 * @param url         Url containing query parameters
	 	 * @return params     Map with query parameters
	 	 * @throws IOException 
	 	 */
	 	static public Map<String, String> getQueryParams(String url) 
	 			throws IOException {
	 		Map<String, String> params = new HashMap<>();
	 	
	 		int start = url.indexOf('?');
	 		while (start != -1) {
	 			// read parameter name
	 			int equals = url.indexOf('=', start);
	 			String param = "";
	 			if (equals != -1) {
	 				param = url.substring(start + 1, equals);
	 			}
	 			else {
	 				param = url.substring(start + 1);
	 			}
	 			
	 			// read parameter value
	 			String value = "";
	 			if (equals != -1) {
	 				start = url.indexOf('&', equals);
	 				if (start != -1) {
	 					value = url.substring(equals + 1, start);
	 				}
	 				else {
	 					value = url.substring(equals + 1);
	 				}
	 			}
	 			
	 			params.put(URLDecoder.decode(param, "UTF-8"), 
	 				URLDecoder.decode(value, "UTF-8"));
	 		}
	 		
	 		return params;
	 	}

	 	/**
	 	 * Returns the url without query parameters
	 	 * @param url         Url containing query parameters
	 	 * @return url        Url without query parameters
	 	 * @throws IOException 
	 	 */
	 	static public String removeQueryParams(String url) 
	 			throws IOException {
	 		int q = url.indexOf('?');
	 		if (q != -1) {
	 			return url.substring(0, q);
	 		}
	 		else {
	 			return url;
	 		}
	 	}
	 	
	 	/**
	 	 * Send a request
	 	 * @param method      HTTP method, for example "GET" or "POST"
	 	 * @param url         Url as string
	 	 * @param body        Request body as string
	 	 * @param headers     Optional map with headers
	 	 * @return response   Response as string
	 	 * @throws IOException 
	 	 */
	 	static public String fetch(String method, String url, String body,
	 			Map<String, String> headers) throws IOException {
	 		// connection
	 		URL u = new URL(url);
	 		HttpURLConnection conn = (HttpURLConnection)u.openConnection();
	 		conn.setConnectTimeout(10000);
	 		conn.setReadTimeout(10000);

	 		// method
	 		if (method != null) {
	 			conn.setRequestMethod(method);
	 		}

	 		// headers
	 		if (headers != null) {
	 			for(String key : headers.keySet()) {
	 				conn.addRequestProperty(key, headers.get(key));
	 			}
	 		}

	 		// body
	 		if (body != null) {
	 			conn.setDoOutput(true);
	 			OutputStream os = conn.getOutputStream();
	 			os.write(body.getBytes());
	 			os.flush();
	 			os.close();
	 		}
	 		
	 		// response
	 		InputStream is = conn.getInputStream();
	 		String response = streamToString(is);
	 		is.close();
	 		
	 		// handle redirects
	 		if (conn.getResponseCode() == 301) {
	 			String location = conn.getHeaderField("Location");
	 			return fetch(method, location, body, headers);
	 		}
	 		
	 		return response;
	 	}
	 	static public String fetch(String method, String url, String body,
	 			Map<String, String> headers,String BM) throws IOException {
	 		// connection
	 		URL u = new URL(url);
	 		HttpURLConnection conn = (HttpURLConnection)u.openConnection();
	 		conn.setConnectTimeout(10000);
	 		conn.setReadTimeout(10000);

	 		// method
	 		if (method != null) {
	 			conn.setRequestMethod(method);
	 		}

	 		// headers
	 		if (headers != null) {
	 			for(String key : headers.keySet()) {
	 				conn.addRequestProperty(key, headers.get(key));
	 			}
	 		}

	 		// body
	 		if (body != null) {
	 			conn.setDoOutput(true);
	 			OutputStream os = conn.getOutputStream();
	 			os.write(body.getBytes());
	 			os.flush();
	 			os.close();
	 		}
	 		
	 		// response
	 		InputStream is = conn.getInputStream();
	 		String response = streamToString(is , BM);
	 		is.close();
	 		
	 		// handle redirects
	 		if (conn.getResponseCode() == 301) {
	 			String location = conn.getHeaderField("Location");
	 			return fetch(method, location, body, headers,BM);
	 		}
	 		
	 		return response;
	 	}
	 	/**
	 	 * Read an input stream into a string
	 	 * @param in
	 	 * @return
	 	 * @throws IOException
	 	 */
	 	static public String streamToString(InputStream in) throws IOException {
	 		StringBuffer out = new StringBuffer();
	 		byte[] b = new byte[4096];
	 		for (int n; (n = in.read(b)) != -1;) {
	 			out.append(new String(b, 0, n));
	 		}
	 		return out.toString();
	 	}
	 	static public String streamToString(InputStream in,String BM) throws IOException {
	 		StringBuffer sb = new StringBuffer();
	 		 BufferedReader reader = new BufferedReader(new InputStreamReader(in,BM));
	 		  String lines;
	 		 while ((lines = reader.readLine()) != null) {
	              sb.append(lines);
	          }
	 		  reader.close();
	 		return sb.toString();
	 	}
	 	
	 	 public static String doPost(String url, String param) throws IOException {
		        PrintWriter out = null;
		        BufferedReader in = null;
		        String result = "";
		        try {
		            URL realUrl = new URL(url);
		            // 打开和URL之间的连接
		            URLConnection conn = realUrl.openConnection();
		            // 设置通用的请求属性
		            conn.setRequestProperty("accept", "*/*");
		            conn.setRequestProperty("connection", "Keep-Alive");
		            conn.setRequestProperty("Content-type","application/json");
		            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		            // 发送POST请求必须设置如下两行
		            conn.setDoOutput(true);
		            conn.setDoInput(true);
		            //1.获取URLConnection对象对应的输出流
		            out = new PrintWriter(conn.getOutputStream());
		            //2.中文有乱码的需要将PrintWriter改为如下
		            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
		            // 发送请求参数
		            out.print(param);
		            // flush输出流的缓冲
		            out.flush();
		            // 定义BufferedReader输入流来读取URL的响应
		            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            String line;
		            while ((line = in.readLine()) != null) {
		                result += line;
		            }
		        } catch (Exception e) {
		            System.out.println("发送 POST 请求出现异常！"+e);
		            e.printStackTrace();
		        }
		        //使用finally块来关闭输出流、输入流
		        finally{
		            try{
		                if(out!=null){
		                    out.close();
		                }
		                if(in!=null){
		                    in.close();
		                }
		            }
		            catch(IOException ex){
		                ex.printStackTrace();
		            }
		        }
		       return result;
		    }

	/**
	 * 获取当前网络ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	 	
//	 	@SuppressWarnings("unused")
//		public static void main(String[] args) throws IOException {
//	 		Map<String, Object> map = new HashMap<>();
//	 		Map<String, Object> layout = new HashMap<>();
//	 		layout.put("width", -1);
//	 		layout.put("logo", "adawdawdawdaw");
//	 		layout.put("layoutX", 30);
//	 		map.put("layout", layout);
//	 		Map<String, Object> item = new HashMap<>();
//	 		List<Object> list = new ArrayList<>();
//	 		JSONObject json = (JSONObject) JSONObject.parse("{'layout':{'width':-1,'logo':'http://','layoutX':130,'id':1,'layoutY':70,'launcherBg':'http://','height':500},'items':[{'sortNo':1,'itemX':100,'width':100,'id':1,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_tv_icon_img.png','tempid':1,'title':'电视','shorthand':'TV','type':1,'operation':1,'height':100},{'sortNo':2,'itemX':100,'width':100,'id':2,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_movie_icon_img.png','tempid':1,'title':'电影','shorthand':'Movie','type':2,'operation':1,'height':100},{'sortNo':3,'itemX':100,'width':100,'id':3,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_tv_icon_img.png','tempid':1,'title':'应用','shorthand':'APP_LAN','type':3,'operation':2,'height':100},{'sortNo':5,'itemX':100,'width':100,'id':4,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_technician_img.png','tempid':1,'title':'酒水介绍','shorthand':'introduce','type':5,'operation':3,'height':100},{'sortNo':6,'itemX':100,'width':100,'id':5,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_About_us_img.png','tempid':1,'title':'本店介绍','shorthand':'About us','type':6,'operation':3,'height':100},{'sortNo':4,'itemX':100,'width':100,'id':6,'itemY':100,'iconURL':'http://39.104.28.166:8000/html/imgs/index/home_tv_icon_img.png','tempid':1,'title':'技师介绍','shorthand':'  ','type':4,'operation':3,'height':100}]}");
//	 		String a = post("http://localhost:8000/webpage/updateMainIndex", "sessionkey=1&id=1&main="+json.toJSONString());
//	 		System.err.println(a);
//	 	}
	 }

