package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.io.FileUtils;

public class SubscriptionReportFileProcessor {
	
	public void copyFileToInputDirectory(Exchange exchange, @Header("inputMessageFilePath") String inputDirString) throws IOException {
		
		File subscriptionReport = (File) exchange.getIn().getBody();
		
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		File subscriptionReportDated = new File("Generate_Report_" + date);
		
		File inputDir = new File(inputDirString + "/input");
		
		FileUtils.copyFile(subscriptionReport, subscriptionReportDated);
		FileUtils.copyFileToDirectory(subscriptionReportDated, inputDir);
				
	}
}
