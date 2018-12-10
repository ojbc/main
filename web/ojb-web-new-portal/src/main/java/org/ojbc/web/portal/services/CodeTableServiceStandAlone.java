package org.ojbc.web.portal.services;

import java.util.Map;

import javax.annotation.Resource;

import org.ojbc.web.portal.AppProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("standalone")
public class CodeTableServiceStandAlone implements CodeTableService{
	@Resource
	AppProperties appProperties;

	public Map<String, String> getMuniDispositionCodeMap(){
		return appProperties.getDispoCodeMapping();
	}

	public Map<String, String> getMuniFiledChargeCodeMap(){
		return  appProperties.getMuniFiledChargeCodeMapping();
	}
	
	public Map<String, String> getMuniAmendedChargeCodeMap(){
		return appProperties.getMuniAmendedChargeCodeMapping(); 
	}
	
	public Map<String, String> getMuniAlternateSentenceMap(){
		return appProperties.getMuniAlternateSentenceMapping(); 
	}
	
	public Map<String, String> getMuniReasonsForDismissalMap(){
		return appProperties.getMuniReasonForDismissalMapping(); 
	}

	@Override
	public Map<String, String> getDaDispositionCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaFiledChargeCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaAmendedChargeCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaAlternateSentenceMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaReasonsForDismissalMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaProvisions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}