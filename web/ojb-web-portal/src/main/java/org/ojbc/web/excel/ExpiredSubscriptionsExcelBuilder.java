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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
 
/**
 * This class builds an Excel spreadsheet document with the .
 *
 */
public class ExpiredSubscriptionsExcelBuilder extends SubscriptionsExcelBuilder {
 
    @SuppressWarnings("unchecked")
	@Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		List<Subscription> subscriptions = (List<Subscription>) model.get("expiredSubscriptions");
        ExpiringSubscriptionRequest expiredSubscriptionRequest = (ExpiringSubscriptionRequest) model.get("expiredSubscriptionRequest");
         
        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet("Expired Subscriptions");
        setupSheet(sheet);
        
        setupHeader(subscriptions, expiredSubscriptionRequest, sheet, "Expired Subscriptions");
        
        createTheTitleRow(sheet, "Expired Subscriptions");
        createTheInfoRows(subscriptions, expiredSubscriptionRequest, sheet);
        
        // create style for header cells
        createTheTable(workbook, subscriptions, sheet);
    }
 
}