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
package org.ojbc.util.camel.processor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestFileAndDirectoryVerificationProcessor {

	@Test
	public void testDoesDirectoryWithFilesExist() throws Exception
	{
		FileAndDirectoryVerificationProcessor fileAndDirectoryVerificationProcessor = new FileAndDirectoryVerificationProcessor();
		
		fileAndDirectoryVerificationProcessor.setDirectoryPath("/tmp/ojb/someDirectory");
		assertFalse(fileAndDirectoryVerificationProcessor.doesDirectoryWithFilesExist());
		
		File parentFolder = new File("/tmp/ojb/someDirectory");
		parentFolder.mkdirs();
		
		File emptyFile = new File("/tmp/ojb/someDirectory/file.txt");
		emptyFile.createNewFile();
		
		assertTrue(fileAndDirectoryVerificationProcessor.doesDirectoryWithFilesExist());
		
		FileUtils.deleteDirectory(parentFolder);
	}
}
