/**
 * 
 */
package org.ojbc.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ojbc.util.statemanager.HashMapStateManager;

/**
 * @author yogeshchawla
 *
 */
public class TestHashMapStateManager  {



	/**
	 * Test method for {@link org.ojbc.util.HashMapStateManager}.
	 */
	@Test
	public void testAllMethods() {
		HashMapStateManager hashMapManager = new HashMapStateManager();
		
		hashMapManager.insert("AAABBBB12312");
		assertTrue(hashMapManager.check("AAABBBB12312"));
		
		hashMapManager.evict();
		
		assertTrue(hashMapManager.check("AAABBBB12312"));
	}

}
