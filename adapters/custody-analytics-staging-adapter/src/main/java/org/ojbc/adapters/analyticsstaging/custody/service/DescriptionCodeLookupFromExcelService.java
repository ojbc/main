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
package org.ojbc.adapters.analyticsstaging.custody.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DescriptionCodeLookupFromExcelService
{

	protected Log log = LogFactory.getLog(this.getClass());

	protected Map<String, Map<String, Integer>> mapOfCodeMaps = null;
	
	public DescriptionCodeLookupFromExcelService() {
		super();
	}
	
	@Autowired
	public DescriptionCodeLookupFromExcelService(
			@Value("${custody.stagingAdapter.codeTableExcelFilePath}") String codeTableExcelFilePath) 
					throws IOException{
		this();
		
    	loadMapOfCodeMaps(codeTableExcelFilePath);

	}

	private void loadMapOfCodeMaps(String codeTableExcelFilePath)
			throws FileNotFoundException, IOException {
		log.info("Recache code table maps.");

    	mapOfCodeMaps = new HashMap<String, Map<String, Integer>>();
    	
        FileInputStream inputStream = new FileInputStream(new File(codeTableExcelFilePath));
        
        Workbook workbook = new XSSFWorkbook(inputStream);
        
        for (int i=0; i<workbook.getNumberOfSheets(); i++){
        	Sheet sheet = workbook.getSheetAt(i); 
        	
        	Map<String, Integer> codePkMap = new HashMap<String, Integer>();
            for (int j = 1; j<=sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                
                if ( row.getCell(row.getLastCellNum() -1).getCellType() == Cell.CELL_TYPE_NUMERIC){
                	row.getCell(row.getLastCellNum() -1).setCellType(Cell.CELL_TYPE_STRING);
                }
              
                String codeOrDescription = StringUtils.upperCase(row.getCell(row.getLastCellNum() -1).getStringCellValue()); 
                Integer pkId = Double.valueOf(row.getCell(0).getNumericCellValue()).intValue();
                codePkMap.put(codeOrDescription, pkId);
            }
            
            mapOfCodeMaps.put(sheet.getSheetName(), codePkMap);
        	
        }
         
        workbook.close();
        inputStream.close();
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
	 * 	 if the given description does not have a matching code, 
	 * 	 it'll try to locate and return the code for "Unknown", 
	 *   or null if both were not found,  
	 */
	public Integer retrieveCode(CodeTable codeTable, String description) {
		try {
			Map<?, ?> lookupMap = mapOfCodeMaps.get(codeTable.name());
			if (lookupMap == null) {
				log.warn("No map entry found in lookup map for the CodeTable: " + codeTable);
				return null;
			}
			
			if (StringUtils.isBlank(description)){
				log.warn(codeTable.name() + " code is empty."); 
				return null;
			}
			
			Integer code = (Integer) lookupMap.get(StringUtils.trimToEmpty(description).toUpperCase());
			if (code == null){
				log.warn("Did not find code " + StringUtils.trimToEmpty(description) + 
						" in code table " + codeTable.name());
			}
			
			return code ;
		}
		catch (Exception e) {
			log.warn("Error occurred in retrieveCode() for code table ["
					+ codeTable + "] and description [" + description + "]", e);
			return null;
		}
	}

	/**
	 * Returns a particular code table map.
	 * @param codeTable
	 * @return
	 */
	public Map<String, Integer> getCodeTable(CodeTable codeTable) {
		return mapOfCodeMaps.get(codeTable.name());
	}

}
