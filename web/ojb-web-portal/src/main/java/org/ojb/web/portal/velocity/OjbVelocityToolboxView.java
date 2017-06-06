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
package org.ojb.web.portal.velocity;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.config.ConfigurationUtils;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityView;

/**
 * Used along {@link VelocityViewResolver} to provide access to the Velocity ImportTool 
 * - which is being used in the portal to display external urls.
 */
public class OjbVelocityToolboxView extends VelocityView {
	@Override
	protected Context createVelocityContext(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {

		ViewToolContext context = new ViewToolContext(getVelocityEngine(),
				request, response, getServletContext());

		ToolboxFactory factory = new ToolboxFactory();

		factory.configure(ConfigurationUtils.getVelocityView());

		for (String scope : Scope.values()) {
			context.addToolbox(factory.createToolbox(scope));
		}

		if (model != null) {
			for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) model
					.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}
		return context;
	}
}
