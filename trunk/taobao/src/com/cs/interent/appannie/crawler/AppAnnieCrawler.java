package com.cs.interent.appannie.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppAnnieCrawler {
	private static CookieStore sslcookies = new BasicCookieStore();
	private static CookieStore cookies = new BasicCookieStore();

	public static CloseableHttpClient createSSLClientDefault(boolean isSSL){
		if(isSSL){
			SSLContext sslContext = null;
			try {
				sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					//信任所有
					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						return true;
					}
				}).build();
			} catch (KeyManagementException e) { 
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
			if(null != sslContext){
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(sslcookies).build();
			}else{
				return  HttpClients.custom().setDefaultCookieStore(cookies).build();
			}
		}else{
			return  HttpClients.custom().setDefaultCookieStore(cookies).build();
		}
	}

	public static void headerWrapper(AbstractHttpMessage methord){
		methord.setHeader("user-agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
		methord.setHeader("accept", "*/*");
		methord.setHeader("accept-language", "en-US,en;q=0.8,zh-CN;q=0.6");
		methord.setHeader("Accept-Encoding", "gzip, deflate");
		methord.setHeader("Referer", "https://ep70.eventpilot.us/web/planner.php?id=ACS15spring");
		
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		httpClient = createSSLClientDefault(true);
		HttpPost httpPost = new HttpPost("https://ep70.eventpilot.us/web/api.php");
		headerWrapper(httpPost);
		httpPost.setHeader("accept-Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		params.add(new BasicNameValuePair("interface", "filter"));
		params.add(new BasicNameValuePair("action", "list"));
		params.add(new BasicNameValuePair("confid", "ACS15spring"));
		params.add(new BasicNameValuePair("table", "media"));
		params.add(new BasicNameValuePair("filter[0][field]", "mediatype"));
		params.add(new BasicNameValuePair("filter[0][val]", "int/html"));
		params.add(new BasicNameValuePair("order[]", "name"));
		params.add(new BasicNameValuePair("adapter", "PlannerAbstractListView"));
		httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));  
		// 发送请求  
		HttpResponse httpresponse = httpClient.execute(httpPost);  
		HttpEntity entity = httpresponse.getEntity();  
		String body = EntityUtils.toString(entity);
		
		System.out.println(body);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}