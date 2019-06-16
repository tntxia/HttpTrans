package com.tntxia.httptrans;

import java.io.File;
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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class HttpTransfer {
	
	private String host;
	
	private int port = 80;
	
	private String contextPath;
	
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void setPort(String port) {
		this.port = Integer.valueOf(port);
	}
	
	public String getHttpCenter() {
		return "http://"+host+":"+port+"/"+contextPath;
	}
	
	private HttpPost getPost(String action){
		String url = getHttpCenter()+"/"+action+".do";
		System.out.println(url);
		return new HttpPost(url);
	}

	@SuppressWarnings("rawtypes")
	public List getList(String action,Map<String,String> params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
        
		HttpPost http = this.getPost(action);
        CloseableHttpResponse response1 = httpclient.execute(http);
        List list = null;
        try {
            HttpEntity entity = response1.getEntity();
            String res = EntityUtils.toString(entity);
            list = JSON.parseArray(res);
        } finally {
            response1.close();
        }
        
        return list;
	}
	
	public Boolean getBoolean(String action,Map<String,String> params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost http = this.getPost(action);
		
	    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	    for(Map.Entry<String, String> entry:params.entrySet()){
	    	formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
	    }
	    UrlEncodedFormEntity urlEntity =  new UrlEncodedFormEntity(formparams, "UTF-8");  
	    http.setEntity(urlEntity);  
	    CloseableHttpResponse response = httpclient.execute(http);  
	    HttpEntity entity = response.getEntity();
	    
		return (Boolean) JSON.parse(entity.toString());
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getMap(String action,Map<String,?> params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost http = this.getPost(action);
	   
	    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	    for(Map.Entry<String, ?> entry:params.entrySet()){
	    	String key = entry.getKey();
	    	Object val = entry.getValue();
	    	
	    	// 如果参数为空则忽略
	    	if(val==null){
	    		continue;
	    	}
	    	
	    	if(val instanceof String){
	    		formparams.add(new BasicNameValuePair(key, (String)entry.getValue()));
	    	}else{
	    		formparams.add(new BasicNameValuePair(key, String.valueOf(entry.getValue())));
	    	}
	    	
	    }
	    UrlEncodedFormEntity urlEntity =  new UrlEncodedFormEntity(formparams, "UTF-8");
	    http.setEntity(urlEntity);
	    CloseableHttpResponse response = httpclient.execute(http);
	    HttpEntity entity = response.getEntity();
	    
		return (Map<String,Object>) JSON.parse(EntityUtils.toString(entity, "UTF-8"));
		
	}
	
	public String uploadFile(String action,String path,Map<String,String> params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost http = this.getPost(action);
		FileBody bin = new FileBody(new File(path));
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart("bin",bin);
		for(Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			StringBody comment = new StringBody(value, ContentType.TEXT_PLAIN);
			builder.addPart(key, comment);
		}
		
		HttpEntity reqEntity = builder.build();
		http.setEntity(reqEntity);
		CloseableHttpResponse response = httpclient.execute(http);
		HttpEntity entity = response.getEntity();
		String responseText = EntityUtils.toString(entity, "UTF-8");
		
		return responseText;
		
	}

}
