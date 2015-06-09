package org.ojbc.adapters.analyticaldatastore.dao;

import java.util.List;

import net.sf.ehcache.transaction.xa.processor.XARequest.RequestType;

import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.dao.model.KeyValue;

public interface CodeTableDAO {
	public List<KeyValue> retrieveCodeDescriptions(CodeTable codeTable);
}
