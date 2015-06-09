package org.ojbc.adapters.analyticaldatastore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.CodeTableDAO;
import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.dao.model.KeyValue;

public class DescriptionCodeLookupService
{

	protected Log log = LogFactory.getLog(this.getClass());

	protected Map<String, Long>[] mapArray = null;
	
	private CodeTableDAO codeTableDAO;

	@SuppressWarnings("unchecked")
	public DescriptionCodeLookupService(CodeTableDAO codeTableDao){
    	log.info("Recache code table descriptions.");

    	mapArray = new Map[CodeTable.values().length];
    	
    	this.setCodeTableDAO( codeTableDao );

        for (CodeTable codeTable : CodeTable.values()) {
            mapArray[codeTable.ordinal()] = convertListToMap(getCodeTableDAO().retrieveCodeDescriptions(codeTable));
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
	public Long retrieveCode(CodeTable codeTable, String description) {
		if (StringUtils.isBlank( description )) {
			return null;
		}

		try {
			Map<?, ?> lookupMap = mapArray[codeTable.ordinal()];
			if (lookupMap == null) {
				log.warn("No map entry found in lookup map for the CodeTable: " + codeTable);
				return null;
			}
			return (Long) lookupMap.get(description);
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
	protected Map<String, Long> convertListToMap(List<KeyValue> codes) {
		Map<String, Long> codeMap = new HashMap<String, Long>();

		for (KeyValue kv : codes)
			codeMap.put( kv.getValue(), kv.getKey());

		return codeMap;
	}

	/**
	 * Returns a particular code table map.
	 * @param codeTable
	 * @return
	 */
	public Map<String, Long> getCodeTable(CodeTable codeTable) {
		return mapArray[codeTable.ordinal()];
	}
	
	private CodeTableDAO getCodeTableDAO(){
	    return this.codeTableDAO; 
	}
    private void setCodeTableDAO(CodeTableDAO codeTableDAO) {
        this.codeTableDAO = codeTableDAO;
    }

}
