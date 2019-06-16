package com.tntxia.httptrans.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.DocumentException;

import com.tntxia.httptrans.HttpTransfer;
import com.tntxia.httptrans.HttpTransferFactory;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException, DocumentException {
		
		HttpTransfer transfer = HttpTransferFactory.generate("file_center");
		transfer.setHost("localhost");
		transfer.setContextPath("file_center");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", "用户上传");
		
		Map<String,Object> res = transfer.getMap("file!add",  params);
		String uuid = (String) res.get("uuid");
		
		Map<String,String> p = new HashMap<String,String>();
		p.put("uuid", uuid);
		transfer.uploadFile("file!upload", "D:\\test.sql", p);
		
		params.put("uuid", uuid);
		
		transfer.getMap("file!finish", params);
		
		System.out.println(res);

	}
	
	

}
