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
package org.ojbc.util.lucene.util;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.ojbc.util.lucene.personid.IdentifierGenerationStrategy;

/**
 * A class of utilities for dealing with Lucene indexes.  The main method removes unwanted / unexpected fields from the index, based upon the standard
 * fields defined in IdentifierGenerationStrategy.
 *
 */
public class LuceneUtils {
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.err.println("Must provide source and target index directories as command line arguments");
			System.exit(1);
		}
		Directory sourceDir = FSDirectory.open(Paths.get(args[0]));
		DirectoryReader reader = DirectoryReader.open(sourceDir);

		Directory targetDir = FSDirectory.open(Paths.get(args[1]));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		config.setIndexDeletionPolicy(new SnapshotDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy()));
		IndexWriter writer = new IndexWriter(targetDir, config);

		Set<String> allowedFields = new HashSet<String>();
		allowedFields.add(IdentifierGenerationStrategy.FIRST_NAME_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.LAST_NAME_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.MIDDLE_NAME_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.BIRTHDATE_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.SEX_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.SSN_FIELD);
		allowedFields.add(IdentifierGenerationStrategy.ID_FIELD);

		try {
			for (LeafReaderContext leaf : reader.leaves()) {
				LeafReader leafReader = leaf.reader();
				for (int docID = 0; docID < leafReader.maxDoc(); docID++) {
					StoredFields storedFields = leafReader.storedFields();
					Document d = storedFields.document(docID);
					Document newDoc = new Document();
					for (IndexableField field : d.getFields()) {
						String fieldName = field.name();
						if (allowedFields.contains(fieldName)) {
							newDoc.add(new StringField(fieldName, field.stringValue(), Field.Store.YES));
						}
					}
					writer.addDocument(newDoc);
				}
			}
			writer.commit();
		} finally {
			reader.close();
			writer.close();
		}
	}

}
