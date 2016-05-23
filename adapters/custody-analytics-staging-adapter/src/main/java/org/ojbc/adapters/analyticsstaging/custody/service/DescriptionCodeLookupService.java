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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticsstaging.custody.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.CodeTableDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptionCodeLookupService
{

	protected Log log = LogFactory.getLog(this.getClass());

	protected Map<String, Integer>[] mapArray = null;
	
	private CodeTableDAO codeTableDAO;

	public DescriptionCodeLookupService() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Autowired
	public DescriptionCodeLookupService(CodeTableDAO codeTableDAO){
		this();
		this.setCodeTableDAO(codeTableDAO);
    	log.info("Recache code table descriptions.");

    	mapArray = new Map[CodeTable.values().length];
    	
        for (CodeTable codeTable : CodeTable.values()) {
            mapArray[codeTable.ordinal()] = convertListToMap(codeTableDAO.retrieveCodeDescriptions(codeTable));
        }
	}

	/**
	 * Looks up a value in the appropriate HashMap cache depending on the two
	 * arguments passed. This method will select the appropriate cache based on
	 * the codeTable argument and look up the value of the second argument.
	 * For the codeTable argument, use the final static variables of this type.
	 * 
	 * @param codeTable - The table name of the cache to be searched. 
	 * @param code - The java.lang.String value to lookup in the cache.
	 * @return A java.lang.String containing the cache search result.
	 */
	public Integer retrieveCode(CodeTable codeTable, String description) {
		if (StringUtils.isBlank( description )) {
			return null;
		}

		try {
			Map<?, ?> lookupMap = mapArray[codeTable.ordinal()];
			if (lookupMap == null) {
				log.warn("No map entry found in lookup map for the CodeTable: " + codeTable);
				return null;
			}
			return (Integer) lookupMap.get(description.trim());
		}
		catch (Exception e) {
			log.warn("Error occurred in retrieveCode() for code table ["
					+ codeTable + "] and description [" + description + "]", e);
			return null;
		}
	}

	/**
	 * Converts a list of KeyValue pair objects that contain codes and descriptions
	 * which were retrieved from a code table to Map<String, String>.
	 * @param codes
	 * @return Map<String, String>
	 */
	protected Map<String, Integer> convertListToMap(List<KeyValue> codes) {
		Map<String, Integer> codeMap = new HashMap<String, Integer>();

		for (KeyValue kv : codes)
			codeMap.put( kv.getValue(), kv.getKey());

		return codeMap;
	}

	/**
	 * Returns a particular code table map.
	 * @param codeTable
	 * @return
	 */
	public Map<String, Integer> getCodeTable(CodeTable codeTable) {
		return mapArray[codeTable.ordinal()];
	}

	public CodeTableDAO getCodeTableDAO() {
		return codeTableDAO;
	}

	public void setCodeTableDAO(CodeTableDAO codeTableDAO) {
		this.codeTableDAO = codeTableDAO;
	}
	
}
