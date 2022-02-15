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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.stereotype.Component;
 
/**
 * This class builds an Excel spreadsheet document with the .
 *
 */
@Component
public class ExpiringSubscriptionsExcelBuilder extends SubscriptionsExcelBuilder {
 
	@Override
	public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		populateData(model, workbook);
	}

	private void populateData(Map<String, Object> model, Workbook workbook) {
		@SuppressWarnings("unchecked")
		List<Subscription> subscriptions = (List<Subscription>) model.get("expiringSubscriptions");
        ExpiringSubscriptionRequest expiringSubscriptionRequest = (ExpiringSubscriptionRequest) model.get("expiringSubscriptionRequest");
         
        // create a new Excel sheet
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Expiring Subscriptions");
        setupSheet(sheet);
        
        setupHeader(subscriptions, expiringSubscriptionRequest, sheet, "Subscriptions Expiring or Requiring Validation");
        
        createTheTitleRow(sheet, "Subscriptions Expiring or Requiring Validation");
        createTheInfoRows(subscriptions, expiringSubscriptionRequest, sheet);
        
        // create style for header cells
        createTheTable((XSSFWorkbook) workbook, subscriptions, sheet);
	}
	
	public XSSFWorkbook createWorkbook(Map<String, Object> model) {
		XSSFWorkbook workbook = new XSSFWorkbook(); 
		populateData(model, workbook);
		return workbook;
	} 
}