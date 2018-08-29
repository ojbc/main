package org.ojbc.web.portal.disposition;

import org.w3c.dom.Element;

public interface DispositionService {

	String findDispositions(DispositionSearchRequest dispositionSearchRequest, Element samlToken) throws Throwable;
}