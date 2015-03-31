package org.ojbc.web.portal.controllers.helpers;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.portal.controllers.dto.PersonSearchCommand;

public class UserSessionTest {

	UserSession unit;
	
	@Before
	public void setup(){
		unit = new UserSession();
	}
	@Test
	public void mostRecentSearch() {
		PersonSearchCommand mostRecentSearch = new PersonSearchCommand();
		unit.setMostRecentSearch(mostRecentSearch);
		assertSame(mostRecentSearch, unit.getMostRecentSearch());
	}

	@Test
	public void mostRecentSearchResult() {
		String mostRecentSearchResult ="some xml";
		unit.setMostRecentSearchResult(mostRecentSearchResult );
		assertSame(mostRecentSearchResult, unit.getMostRecentSearchResult());
	}

}
