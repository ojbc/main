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
package org.ojbc.web.portal.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("propertySplitter")
public class PropertySplitter {

    /**
     * Example: one.example.property = KEY1:VALUE1,KEY2:VALUE2
     */
    public Map<String, String> map(String property, String secondSplitter) {
        return this.map(property, ",", secondSplitter);
    }

    /**
     * Example: one.example.property = KEY1:VALUE1.1,VALUE1.2;KEY2:VALUE2.1,VALUE2.2
     */
    public Map<String, List<String>> mapOfList(String property) {
        Map<String, String> map = this.map(property, ";");

        Map<String, List<String>> mapOfList = new HashMap<>();
        for (Entry<String, String> entry : map.entrySet()) {
            mapOfList.put(entry.getKey(), this.list(entry.getValue()));
        }

        return mapOfList;
    }

    /**
     * Example: one.example.property = VALUE1,VALUE2,VALUE3,VALUE4
     */
    public List<String> list(String property) {
        return this.list(property, ",");
    }

    /**
     * Example: one.example.property = VALUE1.1,VALUE1.2;VALUE2.1,VALUE2.2
     */
    public List<List<String>> groupedList(String property) {
        List<String> unGroupedList = this.list(property, ";");

        List<List<String>> groupedList = new ArrayList<>();
        for (String group : unGroupedList) {
            groupedList.add(this.list(group));
        }

        return groupedList;

    }

    private List<String> list(String property, String splitter) {
        return Arrays.asList(StringUtils.split(property, splitter));
    }

    private Map<String, String> map(String property, String splitter, String secondSplitter) {
    	Map<String, String> map = new HashMap<>();
        Arrays.stream(StringUtils.split(property, splitter))
        		.filter(StringUtils::isNotBlank)
        		.map(i->StringUtils.split(i, secondSplitter))
        		.forEach(i->map.put(i[0], i[1]));
        
        return map;
        		
    }

}