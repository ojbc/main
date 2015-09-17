<?xml version="1.0" encoding="UTF-8"?>
<!--

    Unless explicitly acquired and licensed from Licensor under another license, the contents of
    this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
    versions as allowed by the RPL, and You may not copy or use this file in either source code
    or executable form, except in compliance with the terms and conditions of the RPL

    All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
    WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
    WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
    PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
    governing rights and limitations under the RPL.

    http://opensource.org/licenses/RPL-1.5

    Copyright 2012-2015 Open Justice Broker Consortium

-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
	
	<xsl:param name="sid"/>
	
	<xsl:template match="/">		
		<PersonSearchRequest xmlns="http://ojbc.org/IEPD/Exchange/PersonSearchRequest/1.0">                     	                                           
		   <Person xmlns="http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0">            
		      <PersonAugmentation xmlns="http://niem.gov/niem/domains/jxdm/4.1">
		         <PersonStateFingerprintIdentification>
		            <IdentificationID xmlns="http://niem.gov/niem/niem-core/2.0"><xsl:value-of select="$sid"/></IdentificationID>
		         </PersonStateFingerprintIdentification>
		      </PersonAugmentation>
		   </Person>
		   <SourceSystemNameText xmlns="http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0">{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History</SourceSystemNameText>
	   </PersonSearchRequest>
   </xsl:template>
</xsl:stylesheet>