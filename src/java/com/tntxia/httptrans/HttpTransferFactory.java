package com.tntxia.httptrans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import com.tntxia.xml.util.Dom4jUtil;

public class HttpTransferFactory {
	
	private static Map<String,HttpTransfer> mapping = null;
	
	@SuppressWarnings("rawtypes")
	private synchronized static void init() throws DocumentException {
		
		mapping = new HashMap<String,HttpTransfer>();
		
		String filePath = HttpTransferFactory.class.getClassLoader().getResource("trans.xml").toString();
		
		Document doc = Dom4jUtil.getDoc(filePath);
		
		Element root = doc.getRootElement();
		List list = root.selectNodes("trans-list/trans");
		
		for(int i=0;i<list.size();i++) {
			Node n = (Node) list.get(i);
			String name = Dom4jUtil.getProp(n, "name");
			String host = Dom4jUtil.getProp(n, "host");
			String port = Dom4jUtil.getProp(n, "port");
			String contextPath = Dom4jUtil.getProp(n, "context-path");
			
			HttpTransfer transfer = new HttpTransfer();
			transfer.setHost(host);
			if(port!=null) {
				transfer.setPort(Integer.valueOf(port));
			}
			transfer.setContextPath(contextPath);
			mapping.put(name, transfer);
			
		}
	}
	
	public static HttpTransfer generate(String name) throws DocumentException {
		if(mapping==null) {
			init();
		}
		return mapping.get(name);
	}

}
