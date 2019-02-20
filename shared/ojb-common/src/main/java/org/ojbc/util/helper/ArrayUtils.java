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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.helper;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public final class ArrayUtils {
	
	private ArrayUtils() {
	}
	
	public static boolean hasValue( Predicate<Integer> predicate, Integer... values) {
		return Arrays.stream(values).filter(Objects::nonNull)
				.anyMatch(i -> predicate.test(i));
	}
	
	public static boolean hasPositiveValue( Integer... values) {
		return hasValue(i->i>0, values);
	}

}