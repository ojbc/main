package org.ojbc.web.portal.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
		return getMuniDispositionCodes()
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getCode, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new))
				; 
	}

	public Map<String, String> getMuniFiledChargeCodeMap(){
		return getMuniFiledChargeCodes()
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getMuniAmendedChargeCodeMap(){
		return getMuniAmendedChargeCodes()
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getMuniAlternateSentenceMap(){
		return getMuniAlternateSentences()
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription , 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getMuniReasonsForDismissalMap(){
		return getMuniReasonsForDismissal()
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public List<CodeTableEntry> getMuniDispositionCodes() {
		return this.webClient.get().uri("/criminalhistory/municipal-disposition-codes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}

	public List<CodeTableEntry> getMuniFiledChargeCodes() {
		return this.webClient.get().uri("/criminalhistory/municipal-filed-charge-codes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
	public List<CodeTableEntry> getMuniAmendedChargeCodes() {
		return this.webClient.get().uri("/criminalhistory/municipal-amended-charge-codes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
	public List<CodeTableEntry> getMuniAlternateSentences() {
		return this.webClient.get().uri("/criminalhistory/municipal-alternate-sentences")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
	public List<CodeTableEntry> getMuniReasonsForDismissal() {
		return this.webClient.get().uri("/criminalhistory/municipal-reasons-for-dismissal")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
}