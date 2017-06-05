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
package org.ojbc.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ojbc.util.xml.IEPDResourceResolver.LSInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A DOM Resource Resolver implementation that helps with reading schemas out of IEPDs.
 * 
 */
public class IEPDFullPathResourceResolver implements LSResourceResolver{

	private static final List<String> PATHS_FOUND_LIST = new ArrayList<String>();
	
	private Logger logger;
	
	private List<String> parentClassPathDirsToSearch;
	
	/**
	 * @param parentClasspathDirsToSearch
	 * 		a list of parent paths that will be used when searching for 
	 * 		child xsd files as subpaths
	 */
	public IEPDFullPathResourceResolver(List<String> pParentClasspathDirsToSearch) {	
		
		parentClassPathDirsToSearch = pParentClasspathDirsToSearch;
		logger = Logger.getLogger(IEPDFullPathResourceResolver.class.getName());
	}
	
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {

		List<String> possibleSysIdPathList = new ArrayList<String>();			
		possibleSysIdPathList.add(systemId);		
		String sysIdWithoutRelativeSlashes = systemId.replaceAll("\\.\\./", "");
		possibleSysIdPathList.add(sysIdWithoutRelativeSlashes);
																			
		List<String> pathsFailedList = new ArrayList<String>();
				
		
		for(String iBeginningOfPath : parentClassPathDirsToSearch){		
			
			for(String iEndOfPath : possibleSysIdPathList){		
							    
			    String urlString = iBeginningOfPath + "/" + iEndOfPath;
			    urlString = urlString.replace("//", "/");
				URL testUrl = getClass().getClassLoader().getResource(urlString);
			    			    								
				InputStream resourceInStream = null;
				
				try {													
					
					if(testUrl != null){
						
						resourceInStream = testUrl.openStream();
						
						logger.info("Found Resource: " + testUrl.toString());
					}										
				} catch (IOException e) {
					//ignore
				}
												
				if(resourceInStream != null){						
					PATHS_FOUND_LIST.add(testUrl.toString());					
					pathsFailedList.clear();								
					return new LSInputImpl(publicId, systemId, resourceInStream);					
				}else{					
					pathsFailedList.add(systemId);									
				}				
			}				
		}			
		
		String failPathsReadable = "";
		for(String iFail : pathsFailedList){
			failPathsReadable += "\n" + iFail;
		}
		
		String foundPathsReadable = "";
		for(String iPass : PATHS_FOUND_LIST){
			foundPathsReadable += "\n" + iPass;
		}		
		
		// would not arrive here if a path was found
		throw new RuntimeException("Did not resolve resource: \n "
				+ "type=" + type + ", namespaceURI=" + namespaceURI + ",publicId=" + publicId + ",systemId=" + systemId + ",baseURI=" + baseURI + "\n"
				+ "PATHS FAILED: \n" 
				+ failPathsReadable + "\n"
				+ "PATHS FOUND: \n"
				+ foundPathsReadable);	
	}
	
}


