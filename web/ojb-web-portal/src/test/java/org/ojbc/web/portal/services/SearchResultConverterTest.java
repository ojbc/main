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
package org.ojbc.web.portal.services;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.ojbc.web.portal.controllers.dto.PersonFilterCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class SearchResultConverterTest {

	private SearchResultConverter unit;
	private XsltTransformerService xsltTransformerService;
	private ArgumentCaptor<SAXSource> sourceXmlCaptor;
	private ArgumentCaptor<SAXSource> sourceXslCaptor;
	@SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> paramsCaptor;
	private ApplicationContext applicationContext;

	@Before
	public void setup() {
		xsltTransformerService = mock(XsltTransformerService.class);
		applicationContext = mock(ApplicationContext.class);

		unit = new SearchResultConverter();

		unit.searchResultXsl = new ByteArrayResource("search xsl".getBytes()){
			public java.net.URL getURL() throws IOException {
				return new URL("http://something/dontCare");
			};
		};
		unit.incidentSearchResultXsl = unit.searchResultXsl;
		unit.vehicleSearchResultXsl = unit.searchResultXsl;
		
		unit.xsltTransformerService = xsltTransformerService;
		unit.setApplicationContext(applicationContext);
		
		sourceXmlCaptor = ArgumentCaptor.forClass(SAXSource.class);
		sourceXslCaptor = ArgumentCaptor.forClass(SAXSource.class);
		paramsCaptor = ArgumentCaptor.forClass(Map.class);
		
		HashMap<String, String> searchDetailToXsl = new HashMap<String, String>();
		searchDetailToXsl.put("System", "searchDetail-system.xsl");
		searchDetailToXsl.put("System 2", "searchDetail-system2.xsl");
		unit.searchDetailToXsl = searchDetailToXsl;
	}

	@Test
	public void convertPersonSearchResultPassXmlAndXslInCorrectFormat() throws Exception {

		String searchContent = "some search content";

		when(xsltTransformerService.transform(sourceXmlCaptor.capture(), sourceXslCaptor.capture(), paramsCaptor.capture())).thenReturn(
		        "some transformed content");
		String expectedResult = unit.convertPersonSearchResult(searchContent,null);

		assertThat(expectedResult, is("some transformed content"));

		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("some search content"));
		assertThat(getContentFromSAXSource(sourceXslCaptor.getValue()), is("search xsl"));
	}
	
	@Test
	public void convertVehicleSearchResultPassXmlAndXslInCorrectFormat() throws Exception {

		String searchContent = "some search content";

		when(xsltTransformerService.transform(sourceXmlCaptor.capture(), sourceXslCaptor.capture(),paramsCaptor.capture())).thenReturn(
		        "some transformed content");
		String expectedResult = unit.convertVehicleSearchResult(searchContent,null);

		assertThat(expectedResult, is("some transformed content"));

		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("some search content"));
		assertThat(getContentFromSAXSource(sourceXslCaptor.getValue()), is("search xsl"));
	}
	
	@Test
	public void convertIncidentSearchResultPassXmlAndXslInCorrectFormat() throws Exception {

		String searchContent = "some search content";

		when(xsltTransformerService.transform(sourceXmlCaptor.capture(), sourceXslCaptor.capture(),paramsCaptor.capture())).thenReturn(
		        "some transformed content");
		String expectedResult = unit.convertIncidentSearchResult(searchContent,null);

		assertThat(expectedResult, is("some transformed content"));

		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("some search content"));
		assertThat(getContentFromSAXSource(sourceXslCaptor.getValue()), is("search xsl"));
	}

	@Test
	public void convertDetailSearchResultLoadsXslPassedInAndDoesNotPassAnyParams() throws Exception {
		Resource resource = new ByteArrayResource("someSystem xsl".getBytes()){
			public java.net.URL getURL() throws IOException {
				return new URL("http://something/dontCare");
			};
		};

		unit.searchDetailToXsl.put("someSystemName", "someSystemName.xsl");

		when(applicationContext.getResource("classpath:xsl/someSystemName.xsl")).thenReturn(resource );
		when(xsltTransformerService.transform(sourceXmlCaptor.capture(), sourceXslCaptor.capture(),paramsCaptor.capture())).thenReturn(
				"some transformed content");
		
		unit.convertDetailSearchResult("some search content", "someSystemName", null);
		
		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("some search content"));
		assertThat(getContentFromSAXSource(sourceXslCaptor.getValue()), is("someSystem xsl"));
		assertThat(paramsCaptor.getValue(),nullValue());
	}


	@Test
	public void loadResourceChompSpaces() throws UnsupportedEncodingException{
		unit.getResource("   System   ");
		unit.getResource("System 2");
		
		verify(applicationContext).getResource("classpath:xsl/searchDetail-system.xsl");
		verify(applicationContext).getResource("classpath:xsl/searchDetail-system2.xsl");
	}

	@Test
	public void resourcesLoadOnceThenCached() throws UnsupportedEncodingException{
		when(applicationContext.getResource("classpath:xsl/searchDetail-system.xsl")).thenReturn(new ByteArrayResource("".getBytes()));
		
		Resource firstResource = unit.getResource("System");
		Resource secondResource = unit.getResource("System");
		
		verify(applicationContext,Mockito.times(1)).getResource("classpath:xsl/searchDetail-system.xsl");
		
		assertSame(firstResource, secondResource);
	}
	
	
	@Test
	public void convertPersonSearchResultRethrowsException() throws Exception {

		Resource searchResultXsl = Mockito.mock(Resource.class);

		when(searchResultXsl.getInputStream()).thenThrow(new IOException("Unable to read file"));
		unit.searchResultXsl = searchResultXsl;

		try {
			unit.convertPersonSearchResult("dontCare",null);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), containsString("Unable to read XML/XSL"));
		}

	}

	
	@Test
	public void filterXml() throws Exception{
		unit.personFilterXsl = new ByteArrayResource("some xsl".getBytes()){
			public java.net.URL getURL() throws IOException {
				return new URL("http://something/dontCare");
			};
		};
		
		unit.personFilterCleanupMergedXsl = new ByteArrayResource("some xsl".getBytes()){
			public java.net.URL getURL() throws IOException {
				return new URL("http://something/dontCare");
			};
		};
		
		String xmlContent = "xml content";
		PersonFilterCommand personFilterCommand = new PersonFilterCommand();

		when(xsltTransformerService.transform(sourceXmlCaptor.capture(), sourceXslCaptor.capture(),paramsCaptor.capture())).thenReturn(
				"some transformed content");

		String expectedResult = unit.filterXml(xmlContent , personFilterCommand );

		assertThat(expectedResult, is("some transformed content"));
//		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("xml content"));
		// we have a 2-stage filter.  The input to the second stage is the output from the first stage,
		// therefore, we have sourceXmlCaptor value of some transformed content going into the second stage:
		assertThat(getContentFromSAXSource(sourceXmlCaptor.getValue()), is("some transformed content"));
		assertThat(getContentFromSAXSource(sourceXslCaptor.getValue()), is("some xsl"));
		
	}
	private String getContentFromSAXSource(SAXSource source) throws Exception {
		return IOUtils.toString(source.getInputSource().getByteStream());
	}

}
