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
package org.ojbc.util.lucene.personid;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StringUtils;

/**
 * An implementation of the identifier generation strategy that uses Lucene to maintain an index that maps sets of attributes to identifiers.
 * 
 */
public class IndexedIdentifierGenerationStrategy implements IdentifierGenerationStrategy {

	private static final Log log = LogFactory.getLog(IndexedIdentifierGenerationStrategy.class);
	private static final String NULL_FIELD_VALUE = ".null.";

	protected IndexWriter indexWriter;
	private boolean resolveEquivalentNames = true;
	private FirstNameEquivalentCorpus firstNameEquivalentCorpus;
	protected String indexDirectoryPath;

	public IndexedIdentifierGenerationStrategy(String indexDirectoryPath) throws Exception{
		
		if (StringUtils.isEmpty(indexDirectoryPath))
		{
			throw new IllegalStateException("Index Directory Path Required.");
		}	
		
		this.indexDirectoryPath = indexDirectoryPath;

		init();
		
		firstNameEquivalentCorpus = new FirstNameEquivalentCorpus();
		
	}
	
	public boolean getResolveEquivalentNames() {
		return resolveEquivalentNames;
	}
	
	public void setResolveEquivalentNames(boolean value) {
		resolveEquivalentNames = value;
	}
	
	public void destroy() throws IOException
	{
		indexWriter.close();
	}

	@Override
	public synchronized String generateIdentifier(Map<String, Object> attributes) throws IOException {
		log.debug("Generate identifier for attributes=" + attributes);
		Document d = searchForExistingAttributes(attributes);
		if (d == null) {
			d = createDocumentFromAttributeMap(attributes);
			indexWriter.addDocument(d);
			indexWriter.commit();
			log.debug("Wrote new document to index: " + attributes);
			log.debug("Document: " + d);
		}
		String id = d.get(ID_FIELD);
		log.debug("Generated ID=" + id);
		return id;
	}

	private Document searchForExistingAttributes(Map<String, Object> attributes) throws IOException {
		
		Document ret = null;
		
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		
		String firstNameAttributeValue = getFormattedAttributeValue(attributes.get(FIRST_NAME_FIELD));
		if (resolveEquivalentNames) {
			firstNameAttributeValue = firstNameEquivalentCorpus.getEquivalentName(firstNameAttributeValue);
		}
		builder.add(new FuzzyQuery(new Term(FIRST_NAME_FIELD, firstNameAttributeValue), 2, 1), Occur.MUST);
		builder.add(new FuzzyQuery(new Term(LAST_NAME_FIELD, getFormattedAttributeValue(attributes.get(LAST_NAME_FIELD))), 2, 1), Occur.MUST);
		builder.add(new FuzzyQuery(new Term(MIDDLE_NAME_FIELD, getFormattedAttributeValue(attributes.get(MIDDLE_NAME_FIELD))), 2, 1), Occur.SHOULD);
		builder.add(new FuzzyQuery(new Term(BIRTHDATE_FIELD, getFormattedAttributeValue(attributes.get(BIRTHDATE_FIELD))), 1, 0), Occur.MUST);
		builder.add(new TermQuery(new Term(SEX_FIELD, getFormattedAttributeValue(attributes.get(SEX_FIELD)))), Occur.SHOULD);
		builder.add(new FuzzyQuery(new Term(SSN_FIELD, getFormattedAttributeValue(attributes.get(SSN_FIELD))), 1, 0), Occur.SHOULD);
		
		BooleanQuery query = builder.build();
		
		log.debug("Query: " + query.toString());
		
		Directory directory = indexWriter.getDirectory();
		if (DirectoryReader.indexExists(directory))
		{	
			DirectoryReader reader = DirectoryReader.open(directory);
			
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs hits = searcher.search(query, 2);
			
			ScoreDoc[] hitDocs = hits.scoreDocs;
			if (hitDocs.length > 1) {
				throw new RuntimeException("Invalid index state:  multiple matches for attributes=" + attributes);
			}
			if (hitDocs.length == 1) {
				StoredFields storedFields = searcher.storedFields();
				
				int id = hitDocs[0].doc;
				ret = storedFields.document(hitDocs[0].doc);
				log.debug("Found a match, id=" + id);
			}
			
			reader.close();
		}	
		
		return ret;
		
	}

	private String getFormattedAttributeValue(Object value) {
		String ret = NULL_FIELD_VALUE;
		if (value != null) {
			if (value instanceof Date) {
				ret = DateTools.dateToString((Date) value, DateTools.Resolution.DAY);
			} else {
				ret = ((String) value).toUpperCase();
			}
		}
		return ret;
	}

	private void init() throws Exception {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		log.info("Set Lucene index directory to " + indexDirectory.toString());
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		config.setIndexDeletionPolicy(new SnapshotDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy()));
		indexWriter = new IndexWriter(indexDirectory, config);
	}

	private Document createDocumentFromAttributeMap(Map<String, Object> attributes) {
		Document ret = new Document();
		for (Entry<String, Object> entry : attributes.entrySet()) {
			ret.add(createFieldFromMapEntry(entry));
		}
		ret.add(new StringField(ID_FIELD, UUID.randomUUID().toString(), Store.YES));
		return ret;
	}

	private IndexableField createFieldFromMapEntry(Entry<String, Object> entry) {
		return new StringField(entry.getKey(), getFormattedAttributeValue(entry.getValue()), Store.YES);
	}
	
}
