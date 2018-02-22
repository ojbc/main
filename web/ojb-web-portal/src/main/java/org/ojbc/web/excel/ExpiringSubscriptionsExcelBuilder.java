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
package org.ojbc.web.excel;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.web.servlet.view.document.AbstractExcelView;
 
/**
 * This class builds an Excel spreadsheet document with the .
 *
 */
public class ExpiringSubscriptionsExcelBuilder extends AbstractExcelView {
 
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        @SuppressWarnings("unchecked")
		List<Subscription> subscriptions = (List<Subscription>) model.get("expiringSubscriptions");
         
        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet("Expiring Subscriptions");
        sheet.setDefaultColumnWidth(15);
        
        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        style.setFont(font);
         
        // create header row
        HSSFRow header = sheet.createRow(0);
         
        header.createCell(0).setCellValue("Agency Name");
        header.getCell(0).setCellStyle(style);
         
        header.createCell(1).setCellValue("ORI");
        header.getCell(1).setCellStyle(style);
         
        header.createCell(2).setCellValue("Subscription Owner");
        header.getCell(2).setCellStyle(style);
         
        header.createCell(3).setCellValue("Subscription Subject");
        header.getCell(3).setCellStyle(style);
         
        header.createCell(4).setCellValue("Case Number (OCA)");
        header.getCell(4).setCellStyle(style);
         
        header.createCell(5).setCellValue("Start Date");
        header.getCell(5).setCellStyle(style);
        
        header.createCell(6).setCellValue("Last Validated");
        header.getCell(6).setCellStyle(style);
        
        header.createCell(7).setCellValue("End Date");
        header.getCell(7).setCellStyle(style);
        
        header.createCell(8).setCellValue("Validation Due Date");
        header.getCell(8).setCellStyle(style);
        
        // create data rows
        int rowCount = 1;
         
        for (Subscription subscription : subscriptions) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(subscription.getAgencyName());
            aRow.createCell(1).setCellValue(subscription.getOri());
            aRow.createCell(2).setCellValue(subscription.getSubscriptionOwnerName());
            aRow.createCell(3).setCellValue(subscription.getPersonFullName());
            aRow.createCell(4).setCellValue(subscription.getAgencyCaseNumber());
            aRow.createCell(5).setCellValue(subscription.getStartDate().toString("MM/dd/yyyy"));
            aRow.createCell(6).setCellValue(subscription.getLastValidationDate().toString("MM/dd/yyyy"));
            aRow.createCell(7).setCellValue(subscription.getEndDate().toString("MM/dd/yyyy"));
            aRow.createCell(8).setCellValue(subscription.getValidationDueDate().toString("MM/dd/yyyy"));
        }
        
    }
 
}