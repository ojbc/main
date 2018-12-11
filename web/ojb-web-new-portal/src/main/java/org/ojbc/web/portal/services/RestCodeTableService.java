package org.ojbc.web.portal.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ojbc.web.portal.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"arrest-search"})
public class RestCodeTableService implements CodeTableService{
	private final WebClient webClient;

	@Autowired
	public RestCodeTableService(WebClient.Builder webClientBuilder, AppProperties appProperties) {
		this.webClient = webClientBuilder.baseUrl(appProperties.getRestServiceBaseUrl()).build();
	}
	
	public Map<String, String> getMuniDispositionCodeMap(){
		return getCodeDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-disposition-codes");
	}

	public Map<String, String> getMuniFiledChargeCodeMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-filed-charge-codes");
	}
	
	public Map<String, String> getMuniAmendedChargeCodeMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-amended-charge-codes");
	}
	
	public Map<String, String> getMuniAlternateSentenceMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-alternate-sentences");
	}
	
	public Map<String, String> getMuniReasonsForDismissalMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-reasons-for-dismissal");
	}
	
	
	public Map<String, String> getIdDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
		return function.apply(uri)
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
					(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getCodeDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
		return function.apply(uri)
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getCode, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public List<CodeTableEntry> getCodeTableEntries(String uri) {
		return this.webClient.get().uri(uri)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
	@Override
	public Map<String, String> getDaDispositionCodeMap() {
		return getCodeDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-disposition-codes");
	}

	@Override
	public Map<String, String> getDaFiledChargeCodeMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-filed-charge-codes");
	}

	@Override
	public Map<String, String> getDaAmendedChargeCodeMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-amended-charge-codes");
	}

	@Override
	public Map<String, String> getDaAlternateSentenceMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-alternative-sentences");
	}

	@Override
	public Map<String, String> getDaReasonsForDismissalMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-reasons-for-dismissal");
	}

	@Override
	public Map<String, String> getDaProvisions() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-provisions");
	}
	
}