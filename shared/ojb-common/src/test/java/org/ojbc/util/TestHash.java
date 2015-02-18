package org.ojbc.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.ojbc.util.helper.Hash;

public class TestHash {

	@Test
	public void testGetHash() throws Exception
	{
		String txt = "Hello World";
        assertEquals("b10a8db164e0754105b7a99be72e3fe5", Hash.md5(txt));
        assertEquals("0a4d55a8d778e5022fab701977c5d840bbc486d0", Hash.sha1(txt));
        assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", Hash.sha256(txt));
       
	}
	
}
