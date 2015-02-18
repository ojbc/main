package org.ojbc.util.ndex.util;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.StringUtils;

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
