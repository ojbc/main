package org.ojbc.web.portal.disposition;

public interface DispositionService {

	String findDispositions(DispositionSearchRequest dispositionSearchRequest) throws Throwable;
}