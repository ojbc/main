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
package org.ojbc.adapters.analyticaldatastore.personid;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.ojbc.util.lucene.personid.IndexedIdentifierGenerationStrategy;
import org.springframework.stereotype.Component;

/**
 * An implementation of the identifier generation strategy that uses Lucene to maintain an index that maps sets of attributes to identifiers.
 * 
 */
@Component
public class IndexedPersonIdentifierStrategy  extends IndexedIdentifierGenerationStrategy implements PersonIdentifierStrategy{

	private static final Log log = LogFactory.getLog(IndexedPersonIdentifierStrategy.class);
	protected String indexBackupRoot;

	public IndexedPersonIdentifierStrategy(String indexDirectoryPath, String indexBackupRoot) throws Exception{
		super(indexDirectoryPath);
		this.indexBackupRoot=indexBackupRoot;
	}
	
	public void destroy() throws IOException
	{
		indexWriter.close();
	}

	/**
	 * This method will back up the lucene cache using the specified backup path.
	 * It will create a new directory with the format yyyy_MM_dd_HH_mm_ss which will
	 * contain the backed up index.  Hot backups are allowed.
	 * 
	 */
	@Override
	public String backup() throws Exception {
	    SnapshotDeletionPolicy snapshotter = (SnapshotDeletionPolicy) indexWriter.getConfig().getIndexDeletionPolicy();
	    
	    IndexCommit commit = null;
	    String backupFileDirectory = "";
	    
	    try {
	    	LocalDateTime today = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu_MM_dd_HH_mm_ss");
			String dateTimeStamp = today.format(dtf);
			
			if ( !(indexBackupRoot.endsWith("/") || indexBackupRoot.endsWith("\\")) )
			{	
				indexBackupRoot = indexBackupRoot + System.getProperty("file.separator");
			}

		    backupFileDirectory = indexBackupRoot + dateTimeStamp + System.getProperty("file.separator");
	    	
	    	commit = snapshotter.snapshot();
	        for (String fileName : commit.getFileNames()) {
	           log.debug("File Name: " + fileName);
	           
	           File srcFile = new File(indexDirectoryPath + System.getProperty("file.separator") + fileName);
	           File destFile = new File(backupFileDirectory + System.getProperty("file.separator") + fileName);
	           
	           FileUtils.copyFile(srcFile, destFile);
	           
	        }
	    } catch (Exception e) {
	    	log.error("Exception", e);
	    } finally {
	        snapshotter.release(commit);
	    }	
	    
	    return backupFileDirectory;
	} 
	
}
