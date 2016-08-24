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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * A dirty simple program that reads an Excel file.
 * @author www.codejava.net
 *
 */
public class SqlScriptFromExcelGenerator {
     
    public static void main(String[] args) throws IOException {
        generatePolulateCodeTableScript("src/test/resources/db/populate-adams-code-tables.sql", 
        		"src/test/resources/codeSpreadSheets/AdamsCountyAnalyticsCodeTables.xlsx");
        generatePolulateCodeTableScript("src/test/resources/db/populate-pima-code-tables.sql", 
        		"src/test/resources/codeSpreadSheets/PimaCountyAnalyticsCodeTables.xlsx");
    }

	private static void generatePolulateCodeTableScript(String sqlScriptPath, String excelFilePath) throws FileNotFoundException, IOException {
		Path adamsSqlPath = Paths.get(sqlScriptPath);

        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
         
        Workbook workbook = new XSSFWorkbook(inputStream);
        StringBuilder sb = new StringBuilder(); 
        
        for (int i=0; i<workbook.getNumberOfSheets(); i++){
        	Sheet sheet = workbook.getSheetAt(i); 
        
        	String idColumnName = sheet.getRow(0).getCell(0).getStringCellValue(); 
        	String descriptionColumnName = sheet.getRow(0).getCell(1).getStringCellValue(); 

        	System.out.println("descriptionColumnName: " + descriptionColumnName);
        	String baseString = "insert into " + sheet.getSheetName() + 
        			" (" + idColumnName + ", " + descriptionColumnName + ") values (";
            for (int j = 1; j<=sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                
                String description = row.getCell(1).getStringCellValue(); 
                Integer pkId = Double.valueOf(row.getCell(0).getNumericCellValue()).intValue();
                
                String insertString = baseString + "'" + pkId + "', " +  "'" + description.replace("'", "''") + "');\n";
                sb.append(insertString);
            }
            
        }
         
        workbook.close();
        inputStream.close();
        
        try (BufferedWriter writer = Files.newBufferedWriter(adamsSqlPath)) {
            writer.write(sb.toString());
        }
        
        System.out.println("Sql script " + sqlScriptPath + " generated. ");
	}
 
}