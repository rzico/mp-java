package com.uns.util;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtils {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> Dom2Map(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if (list.size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

	/**
	 * map to xml xml <node><key label="key1">value1</key><key
	 * label="key2">value2</key>......</node>
	 * 
	 * @param map
	 * @return
	 */
	public static String maptoXml(Map map) {
		Document document = DocumentHelper.createDocument();
		Element nodeElement = document.addElement("Xmobo");
		for (Object obj : map.keySet()) {
			Object v = map.get(obj);
			if(v == null) v = "";
			Element keyElement = nodeElement.addElement(String.valueOf(obj));
			if (v instanceof String) {
				keyElement.setText(String.valueOf(v));
			} else {
				maptoXml(keyElement,(Map) v);
//				Map mv = (Map) v;
//				for (Object o : mv.keySet()) {
//					Object value = mv.get(o);
//					if(value == null) value = "";
//					Element el = keyElement.addElement(String.valueOf(o));
//					el.setText(String.valueOf(value));
//				}
			}
		}
		return doc2String(document);
	}
	
//	public static void main(String[] args) {
//		Map merchant = new HashMap<>();
//		Map m1 = new HashMap<>();
//		m1.put("version", "1.0.0");
//		
//		
//		Map m2 = new HashMap<>();
//		m2.put("msgType", "02");
//		
//		merchant.put("head", m1);
//		merchant.put("body", m2);
//		
//		System.out.println(maptoXmls(merchant,"merchant"));
//	}
	
	/**
	 * map to xml xml <node><key label="key1">value1</key><key
	 * label="key2">value2</key>......</node>
	 * 
	 * @param map
	 * @return
	 */
	public static String maptoXmls(Map map,String mapName) {
		Document document = DocumentHelper.createDocument();
		Element nodeElement = document.addElement(mapName);
		for (Object obj : map.keySet()) {
			Object v = map.get(obj);
			if(v == null) v = "";
			Element keyElement = nodeElement.addElement(String.valueOf(obj));
			if (v instanceof String) {
				keyElement.setText(String.valueOf(v));
			} else {
				maptoXml(keyElement,(Map) v);
//				Map mv = (Map) v;
//				for (Object o : mv.keySet()) {
//					Object value = mv.get(o);
//					if(value == null) value = "";
//					Element el = keyElement.addElement(String.valueOf(o));
//					el.setText(String.valueOf(value));
//				}
			}
		}
		return doc2String(document);
	}
	
	
	private static void maptoXml(Element keyElement, Map map) {
		for (Object o : map.keySet()) {
			Object value = map.get(o);
			if (value == null)
				value = "";
			Element el = keyElement.addElement(String.valueOf(o));
			if(value instanceof Map)
				maptoXml(el,(Map) value);
			else if(value instanceof List)
				maptoXml(el,(List) value);
			else 
				el.setText(String.valueOf(value));
		}
	}
	
	private static void maptoXml(Element keyElement, List<Object> list) {
		for (Object value : list) {
			if (value == null)
				value = "";
			maptoXml(keyElement,(Map) value);
		}
	}

	public static Map xmltoMap(String xml) {
		try {
			Map map = new HashMap();
			Document document = DocumentHelper.parseText(xml);
			Element nodeElement = document.getRootElement();
			List node = nodeElement.elements();
			for (Iterator it = node.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				map.put(elm.attributeValue("label"), elm.getText());
				elm = null;
			}
			node = null;
			nodeElement = null;
			document = null;
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doc2String(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 使用UTF-8编码
			OutputFormat format = new OutputFormat("   ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

    /**
     * 根据子节点的名字获得XML中子节点的内容
     * @param xml
     * @param nodeName
     * @return
     */
    public static String getNode(String xml, String nodeName){
    	if( xml.indexOf("<" + nodeName + ">") < 0 )
    		return "";
    	return xml.substring(xml.indexOf("<" + nodeName + ">") + nodeName.length() + 2, xml.indexOf("</" + nodeName + ">"));
    }

}