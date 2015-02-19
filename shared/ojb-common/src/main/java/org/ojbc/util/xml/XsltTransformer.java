package org.ojbc.util.xml;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;


public class XsltTransformer {
	
	public String transform(Source sourceXml, Source sourceXsl, Map<String,Object> params) {

		if (sourceXml == null) {
			return null;
		}

		StringWriter stringWriter = new StringWriter();
		
		try {			
			String saxonTransformerName = net.sf.saxon.TransformerFactoryImpl.class.getCanonicalName();
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance(saxonTransformerName, null);
			
			Transformer transformer;
			if (sourceXsl == null) {
				transformer = transformerFactory.newTransformer();
			} else {
				transformer = transformerFactory.newTransformer(sourceXsl);
			}

			addParams(transformer,params);
			transformer.transform(sourceXml, new StreamResult(stringWriter));
			return stringWriter.toString();

		} catch (TransformerException e) {
			throw new RuntimeException("An error occured when applying XSLT - " + e.getMessage(), e);
		}

	}

	private void addParams(Transformer transformer,Map<String,Object> params) {
		if(params == null){
			return;
		}
		for(Entry<String, Object> entry: params.entrySet()){
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
    }
	
}

