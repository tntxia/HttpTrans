package com.tntxia.httptrans.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.DocumentException;

import com.tntxia.httptrans.HttpTrans;
import com.tntxia.httptrans.HttpTransfer;
import com.tntxia.httptrans.HttpTransferFactory;

public class Test2 {

	public static void main(String[] args) throws ClientProtocolException, IOException, DocumentException {
		
		List list = HttpTrans.getList("http://localhost/file_center/file!list.do");
		
		System.out.println(list);

	}
	
	

}
