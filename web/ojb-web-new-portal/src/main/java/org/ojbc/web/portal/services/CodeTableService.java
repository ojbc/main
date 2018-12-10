package org.ojbc.web.portal.services;

import java.util.Map;

public interface CodeTableService {
	public Map<String, String> getMuniDispositionCodeMap();
	public Map<String, String> getMuniFiledChargeCodeMap();
	public Map<String, String> getMuniAmendedChargeCodeMap();
	public Map<String, String> getMuniAlternateSentenceMap();
	public Map<String, String> getMuniReasonsForDismissalMap();
	public Map<String, String> getDaDispositionCodeMap();
	public Map<String, String> getDaFiledChargeCodeMap();
	public Map<String, String> getDaAmendedChargeCodeMap();
	public Map<String, String> getDaAlternateSentenceMap();
	public Map<String, String> getDaReasonsForDismissalMap();
	public Map<String, String> getDaProvisions();
	
}