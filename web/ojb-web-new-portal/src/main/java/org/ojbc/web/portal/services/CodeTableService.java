package org.ojbc.web.portal.services;

import java.util.Map;

public interface CodeTableService {
	public Map<String, String> getMuniDispositionCodeMap();
	public Map<String, String> getMuniFiledChargeCodeMap();
	public Map<String, String> getMuniAmendedChargeCodeMap();
	public Map<String, String> getMuniAlternateSentenceMap();
	public Map<String, String> getMuniReasonsForDismissalMap();
	
}