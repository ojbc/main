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
package org.ojbc.bundles.adapters.personsearch.ndex;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

public class IgnoreIDsDifferenceListener implements DifferenceListener {
    private static final int[] IGNORE_VALUES = new int[] {
            DifferenceConstants.ATTR_VALUE.getId(),
    };

    private boolean isIgnoredDifference(Difference difference) {
        int differenceId = difference.getId();
        for (int i=0; i < IGNORE_VALUES.length; ++i) {
            if (differenceId == IGNORE_VALUES[i]) {
                return true;
            }
        }
        return false;
    }

    public int differenceFound(Difference difference) {
        if (isIgnoredDifference(difference)) {
            return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
        } else {
            return RETURN_ACCEPT_DIFFERENCE;
        }
    }

    public void skippedComparison(Node control, Node test) {
    }
}