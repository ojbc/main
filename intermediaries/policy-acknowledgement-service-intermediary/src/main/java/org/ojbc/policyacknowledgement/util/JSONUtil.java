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
package org.ojbc.policyacknowledgement.util;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONUtil {
    private final static Log log = LogFactory.getLog(JSONUtil.class);

    private static ObjectWriter objectWriter = new ObjectMapper().writer()
            .withDefaultPrettyPrinter()
            .with(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));;

    private JSONUtil() {
    }

    public static String toJsonString(Object object) {
        String json = "";
        try {
            json = objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            json = e.getMessage();
        }
        return json;
    }

}
