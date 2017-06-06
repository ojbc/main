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
package org.ojbc.util.ndex.util;

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

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.StringUtils;

/**
 * N-DEx submissions require a unique sequence number in the web service payload.
 * The number needs to be between 0 and 9999 and will be left-padded with zeros.
 * 
 * This class will help generate that unique sequence number for N-DEx submissions.
 * 
 * @author yogeshchawla
 *
 */

public class UniqueIDGenerator {

	/**
	 * Atomic Integer with the actual message ID
	 */
	private final AtomicInteger messageID;

	/**
	     *
	     */
	public UniqueIDGenerator() throws Exception {
		// Generate a new messageID.
		messageID = new AtomicInteger();
	}

	/**
	 * Method will return the next sequence number from 0 to 9999
	 * 
	 * @return
	 * @throws Exception
	 */

	public int getNextMessageID() throws Exception {
		int n = messageID.incrementAndGet();
		return (n % 10000);
	}

	public String getNextMessageIDInString() throws Exception {
		int n = this.getNextMessageID();
		String numberAsString = String.valueOf(n);
		String paddedNumber = StringUtils.leftPad(numberAsString, 4, '0');
		return paddedNumber;

	}

	public static void main(String[] args) throws Exception {
		UniqueIDGenerator uniqueIDGenerator = new UniqueIDGenerator();

		for (int i = 0; i < 10000; i++)
			System.out.println(uniqueIDGenerator.getNextMessageIDInString());
	}
}
