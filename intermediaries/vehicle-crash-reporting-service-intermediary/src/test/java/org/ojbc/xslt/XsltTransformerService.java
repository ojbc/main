/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.xslt;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.TransformerFactoryImpl;

import org.springframework.stereotype.Service;

@Service
public class XsltTransformerService {

		
	public String transform(Source sourceXml, Source sourceXsl,Map<String,Object> params) {
			
		if (sourceXml == null) {
			return null;
		}

		StringWriter stringWriter = new StringWriter();
		try {
			TransformerFactory transformerFactory = TransformerFactoryImpl.newInstance(
			        TransformerFactoryImpl.class.getCanonicalName(), null);

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
