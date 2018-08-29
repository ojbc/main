package org.ojbc.web.portal.arrest;

public interface ArrestService {

	String findArrests(ArrestSearchRequest arrestSearchRequest) throws Throwable;
}