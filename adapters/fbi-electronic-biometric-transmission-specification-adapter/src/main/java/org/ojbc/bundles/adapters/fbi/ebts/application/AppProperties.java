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

package org.ojbc.bundles.adapters.fbi.ebts.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fbi.ebts.adapter")
public class AppProperties {
	private Integer httpMaxTotalConnections = 50;
	private Integer httpConnectionsPerRoute = 15;
	/*	Time a connection can live in the pool before it is closed in ms*/
	private Integer httpConnectionTimeToLive = 15_000; 
	// Timeout to get a connection from the pool (in ms)
	private Integer httpConnectionRequestTimeout = 5000; 
	// Timeout for establishing a TCP connection (in ms)
	private Integer httpConnectionTimeout = 5000; 
	// Timeout for waiting for data after the connection is established (in ms)
	private Integer httpSocketTimeout = 30000; 
	// Evict idle connections from the pool after this time in seconds
	private Integer httpIdleConnectionTimeout = 15; 
	// Whether to validate a connection before leasing it from the pool
	private Integer httpValidateAfterInactivity = 2000; 

    public Integer getHttpMaxTotalConnections() {
        return httpMaxTotalConnections;
    }

    public void setHttpMaxTotalConnections(Integer httpMaxTotalConnections) {
        this.httpMaxTotalConnections = httpMaxTotalConnections;
    }

    public Integer getHttpConnectionsPerRoute() {
        return httpConnectionsPerRoute;
    }

    public void setHttpConnectionsPerRoute(Integer httpConnectionsPerRoute) {
        this.httpConnectionsPerRoute = httpConnectionsPerRoute;
    }

    public Integer getHttpConnectionTimeToLive() {
        return httpConnectionTimeToLive;
    }

    public void setHttpConnectionTimeToLive(Integer httpConnectionTimeToLive) {
        this.httpConnectionTimeToLive = httpConnectionTimeToLive;
    }

    public Integer getHttpConnectionRequestTimeout() {
        return httpConnectionRequestTimeout;
    }

    public void setHttpConnectionRequestTimeout(Integer httpConnectionRequestTimeout) {
        this.httpConnectionRequestTimeout = httpConnectionRequestTimeout;
    }

    public Integer getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }

    public void setHttpConnectionTimeout(Integer httpConnectionTimeout) {
        this.httpConnectionTimeout = httpConnectionTimeout;
    }

    public Integer getHttpSocketTimeout() {
        return httpSocketTimeout;
    }

    public void setHttpSocketTimeout(Integer httpSocketTimeout) {
        this.httpSocketTimeout = httpSocketTimeout;
    }

    public Integer getHttpIdleConnectionTimeout() {
        return httpIdleConnectionTimeout;
    }

    public void setHttpIdleConnectionTimeout(Integer httpIdleConnectionTimeout) {
        this.httpIdleConnectionTimeout = httpIdleConnectionTimeout;
    }

    public Integer getHttpValidateAfterInactivity() {
        return httpValidateAfterInactivity;
    }

    public void setHttpValidateAfterInactivity(Integer httpValidateAfterInactivity) {
        this.httpValidateAfterInactivity = httpValidateAfterInactivity;
    }
}
