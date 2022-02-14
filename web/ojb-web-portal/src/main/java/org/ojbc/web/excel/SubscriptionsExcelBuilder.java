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

import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
 
/**
 * This class builds an Excel spreadsheet document with the .
 *
 */
public abstract class SubscriptionsExcelBuilder extends AbstractXlsxView {
 
	public SubscriptionsExcelBuilder() {
		super();
	}

	void createTheTable(XSSFWorkbook workbook,
			List<Subscription> subscriptions, XSSFSheet sheet) {
		XSSFCellStyle borderedBoldstyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        
        borderedBoldstyle.setFont(font);
        borderedBoldstyle.setBorderBottom(BorderStyle.THIN);
        borderedBoldstyle.setBorderTop(BorderStyle.THIN);
        borderedBoldstyle.setBorderLeft(BorderStyle.THIN);
        borderedBoldstyle.setBorderRight(BorderStyle.THIN);
         
        XSSFCellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);
        // create header row
        XSSFRow header = sheet.createRow(5);
         
        header.createCell(0).setCellValue("Agency Name");
        header.getCell(0).setCellStyle(borderedBoldstyle);
         
        header.createCell(1).setCellValue("ORI");
        header.getCell(1).setCellStyle(borderedBoldstyle);
         
        header.createCell(2).setCellValue("Subscription Owner");
        header.getCell(2).setCellStyle(borderedBoldstyle);
         
        header.createCell(3).setCellValue("Subscription Subject");
        header.getCell(3).setCellStyle(borderedBoldstyle);
         
        header.createCell(4).setCellValue("Case Number (OCA)");
        header.getCell(4).setCellStyle(borderedBoldstyle);
         
        header.createCell(5).setCellValue("Start Date");
        header.getCell(5).setCellStyle(borderedBoldstyle);
        
        header.createCell(6).setCellValue("Last Validated");
        header.getCell(6).setCellStyle(borderedBoldstyle);
        
        header.createCell(7).setCellValue("End Date");
        header.getCell(7).setCellStyle(borderedBoldstyle);
        
        header.createCell(8).setCellValue("Validation Due Date");
        header.getCell(8).setCellStyle(borderedBoldstyle);
        
        // create data rows
        int rowCount = 6;
         
        for (Subscription subscription : subscriptions) {
            XSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(subscription.getAgencyName());
            aRow.createCell(1).setCellValue(subscription.getOri());
            aRow.createCell(2).setCellValue(subscription.getSubscriptionOwnerName());
            aRow.createCell(3).setCellValue(subscription.getPersonFullName());
            aRow.createCell(4).setCellValue(subscription.getAgencyCaseNumber());
            aRow.createCell(5).setCellValue(subscription.getStartDateString());
            aRow.createCell(6).setCellValue(subscription.getLastValidationDateString());
            aRow.createCell(7).setCellValue(subscription.getEndDateString());
            aRow.createCell(8).setCellValue(subscription.getValidationDueDateString());

            for (int i = 0; i <= 8; i++){
            	aRow.getCell(i).setCellStyle(borderedStyle);
            }
        }
	}

	void createTheInfoRows(List<Subscription> subscriptions,
			ExpiringSubscriptionRequest expiringSubscriptionRequest,
			XSSFSheet sheet) {
		XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Time Period(X):");
        row2.createCell(1).setCellValue(expiringSubscriptionRequest.getDaysUntilExpiry());
        XSSFRow row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue("Total Count:");
        row3.createCell(1).setCellValue(subscriptions.size());
	}

	void setupHeader(List<Subscription> subscriptions,
			ExpiringSubscriptionRequest expiringSubscriptionRequest,
			XSSFSheet sheet, String centerString) {
		Header sheetHeader = sheet.getHeader(); 
        sheetHeader.setCenter(centerString);
        sheetHeader.setLeft("Time Period(X): "  + expiringSubscriptionRequest.getDaysUntilExpiry() + 
        		"\nTotal Count: " + subscriptions.size());
        sheetHeader.setRight("Page " + HSSFHeader.page());
	}

	void setupSheet(XSSFSheet sheet) {
		sheet.setDefaultColumnWidth(15);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
	}
 
	void createTheTitleRow(XSSFSheet sheet, String titleString) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(titleString);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
	}
    

}