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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * A dirty simple program that reads an Excel file.
 * @author www.codejava.net
 *
 */
public class SimpleExcelReaderExample {
     
    public static void main(String[] args) throws IOException {
    	Map<String, Map<String, Integer>> mapOfCodeMaps = new HashMap<String, Map<String, Integer>>();

    	String excelFilePath = "src/test/resources/codeSpreadSheets/AdamsCountyAnalyticsCodeTables.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
         
        Workbook workbook = new XSSFWorkbook(inputStream);
        
        for (int i=0; i<workbook.getNumberOfSheets(); i++){
        	Sheet sheet = workbook.getSheetAt(i); 
        	
        	Map<String, Integer> codePkMap = new HashMap<String, Integer>();
            for (int j = 1; j<=sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                
                String codeOrDescription = row.getCell(row.getLastCellNum() -1).getStringCellValue(); 
                Integer pkId = Double.valueOf(row.getCell(0).getNumericCellValue()).intValue();
                codePkMap.put(codeOrDescription, pkId);
            }
            
            mapOfCodeMaps.put(sheet.getSheetName(), codePkMap);
        	
        }
         
        workbook.close();
        inputStream.close();
    }
 
}