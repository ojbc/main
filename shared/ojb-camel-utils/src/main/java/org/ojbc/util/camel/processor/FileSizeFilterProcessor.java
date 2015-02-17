package org.ojbc.util.camel.processor;

import java.io.File;

import org.apache.camel.Exchange;

public class FileSizeFilterProcessor {

	private double fileSizeInKilobytes;
	
	/**
	 * This method will filter files less than a certain size.  If file size is 0, we just return.
	 * This helps out in scenarios where the downloaded file is corrupted and for example only has a few lines
	 * in it.  This results in all their records getting unsubscribed.
	 * 
	 * This processor sets a header which triggers an email alert.
	 * 
	 * @param ex
	 */
	public void filterFilesLessThanSpecifiedSize(Exchange ex)
	{
		if (fileSizeInKilobytes == 0)
		{
			return;
		}	
		
		File fileToProcess = ex.getIn().getBody(File.class);
		
		double bytes = fileToProcess.length();
		double kilobytes = (bytes / 1024);
		
		if (kilobytes < fileSizeInKilobytes)
		{
			ex.getIn().setHeader("fileSizeTooSmall", true);
		}	

	}

	public double getFileSizeInKilobytes() {
		return fileSizeInKilobytes;
	}

	public void setFileSizeInKilobytes(double fileSizeInKilobytes) {
		this.fileSizeInKilobytes = fileSizeInKilobytes;
	}
	
	
}
