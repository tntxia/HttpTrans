package com.tntxia.httptrans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class HttpTrans {
	
	public static String getString(String url,Map<String,?> params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost http = new HttpPost(url);
	   
		if(params!=null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		    for(Map.Entry<String, ?> entry:params.entrySet()){
		    	String value = entry.getValue().toString();
		    	formparams.add(new BasicNameValuePair(entry.getKey(),value ));
		    }
		    UrlEncodedFormEntity urlEntity =  new UrlEncodedFormEntity(formparams, "UTF-8");  
		    http.setEntity(urlEntity);  
		}
		
		
		CloseableHttpResponse response1 = httpclient.execute(http);
       
        try {
            HttpEntity entity = response1.getEntity();
            String res = EntityUtils.toString(entity);
            return res;
        } finally {
            response1.close();
        }
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List getList(String url,Map<String,?> params) throws ClientProtocolException, IOException{
		
		return JSON.parseArray(getString(url,params));
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Map getMap(String url,Map<String,?> params) throws ClientProtocolException, IOException{
		Map res = null;
		String responseText = null;
		try {
			responseText = getString(url, params);
			res = JSON.parseObject(responseText);
		}catch(Exception ex) {
			ex.printStackTrace();	
			System.out.println("parse string fail, "+responseText);
		}
		return res;
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List getList(String url) throws ClientProtocolException, IOException{
		
		return JSON.parseArray(getString(url,null));
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Map getMap(String url) throws ClientProtocolException, IOException{
		
		return JSON.parseObject(getString(url, null));
		
	}

}
