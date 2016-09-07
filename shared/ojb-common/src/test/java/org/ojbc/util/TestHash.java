/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.ojbc.util.helper.Hash;

public class TestHash {

	@Test
	public void testGetHash() throws Exception
	{
		String txt = "Hello World";
		String salt = "salty";
		
        assertEquals("b10a8db164e0754105b7a99be72e3fe5", Hash.md5(txt));
        assertEquals("0a4d55a8d778e5022fab701977c5d840bbc486d0", Hash.sha1(txt));
        assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", Hash.sha256(txt));

        assertEquals("8ba456f07e5c6892a27d81cf54342077", Hash.md5WithSalt(txt, salt));
        assertEquals("5f4286de3d18a25c9c96201f1d25c9d964d7a185", Hash.sha1WithSalt(txt, salt));
        assertEquals("6961c7405f293d09b73013862d5be76609006c3c2aa49e799c68cbd1feb46a70", Hash.sha256WithSalt(txt, salt));

	}
	
}
