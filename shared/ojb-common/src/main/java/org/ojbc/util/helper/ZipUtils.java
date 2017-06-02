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
package org.ojbc.util.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Zip Utility class. 
 * 
 * This class provides assistance with Zip/Unzip byte[] objects.
 */
public class ZipUtils {
	private static final Log log = LogFactory.getLog( ZipUtils.class );
	
	public static byte[] zip(byte[] originalData){
    	Deflater compressor = new Deflater();
    	compressor.setLevel(Deflater.BEST_COMPRESSION);
	    compressor.setInput(originalData);
	    compressor.finish();

	    ByteArrayOutputStream bos = new ByteArrayOutputStream(originalData.length);

	    byte[] buf = new byte[1024];
	    while (!compressor.finished()) {
	      int count = compressor.deflate(buf);
	      bos.write(buf, 0, count);
	    }
	    
	    try {
			bos.close();
		} catch (IOException e) {
			log.error("Failed to zip data " + originalData, e );
		}
	    byte[] compressedData = bos.toByteArray();
	    
	    log.debug("Orignal data:" + originalData.length + " bytes");
	    log.debug("Compressed data:" + compressedData.length + " bytes");
		return compressedData;
	}
	
	public static byte[] unzip(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count;
					count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (Exception e) {
			log.error("Failed to unzip data", e);
		}

		byte[] output = outputStream.toByteArray();
		log.debug("Original: " + data.length + " bytes");
		log.debug("Decompressed: " + output.length + " bytes");
		return output;
	}

}

