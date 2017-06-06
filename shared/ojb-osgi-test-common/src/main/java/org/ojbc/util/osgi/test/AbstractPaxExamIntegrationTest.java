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
/*

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
   # # # #
   
   The code in this file is a derivative work of software posted to GitHub by user ANierbeck, at https://github.com/ANierbeck/Camel-Pax-Exam-Demo.  Because it was licensed there
   under the Apache License, Version 2.0, the OJBC is required to re-license it under this same license.  There was no copyright notice in the original source file.
   
   Modifications made by the OJBC are:  Copyright (c) 2015 Open Justice Broker Consortium
   
 */
package org.ojbc.util.osgi.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.features.FeaturesService;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.util.PathUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Base class of Pax Exam integration tests. This class provides some useful utility functionality, like executing commands in the Karaf shell and
 * obtaining references to OSGi services loaded in Karaf.
 * 
 */
public abstract class AbstractPaxExamIntegrationTest {

	@Inject
	protected FeaturesService featuresService;

	@Inject
	protected BundleContext bundleContext;

	private ExecutorService executor = Executors.newCachedThreadPool();

	@ProbeBuilder
	public TestProbeBuilder configureTestBundle(TestProbeBuilder probe) {
		// this is necessary for the generated test bundle to access OSGi services in the container
		probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "*,org.apache.felix.service.*,org.ojbc.*;status=provisional");
		return probe;
	}
	
	protected File getMavenProjectRootDirectory() {
		String baseDir = PathUtils.getBaseDir();
		return new File(new File(new File(new File(baseDir).getParent()).getParent()).getParent());
	}

	/**
	 * Execute a command in the Karaf shell
	 * 
	 * @param command
	 *            the command
	 * @param timeout
	 *            number of milliseconds to wait for the command to execute
	 * @param echo
	 *            whether to echo the command to the shell
	 * @return the result of executing the command
	 */
	protected String executeCommand(final String command, final Long timeout, final Boolean echo) {

		String response;

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final PrintStream printStream = new PrintStream(byteArrayOutputStream);

		final CommandProcessor commandProcessor = getOsgiService(CommandProcessor.class, null, 30000L);
		final CommandSession commandSession = commandProcessor.createSession(System.in, printStream, System.err);

		FutureTask<String> commandFuture = new FutureTask<String>(new Callable<String>() {
			public String call() {
				try {
					if (!echo) {
						System.out.println(command);
					}
					commandSession.execute(command);
				} catch (Exception e) {
					e.printStackTrace();
				}
				printStream.flush();
				return byteArrayOutputStream.toString();
			}
		});

		try {
			executor.submit(commandFuture);
			response = commandFuture.get(timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			response = "Command " + command + " timed out.";
		}

		return response;

	}

	/**
	 * Get an OSGi reference of the specified type, filtered as specified
	 * 
	 * @param type
	 *            the type of the service object
	 * @param filter
	 *            the filter syntax (must be a valid OSGi service filter)
	 * @param timeout
	 *            number of milliseconds to wait before timing out
	 * @return the service reference
	 */
	protected <T> T getOsgiService(Class<T> type, String filter, long timeout) {

		try {
			String fullyQualifiedFilterString;
			if (filter != null) {
				if (filter.startsWith("(")) {
					fullyQualifiedFilterString = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")" + filter + ")";
				} else {
					fullyQualifiedFilterString = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")(" + filter + "))";
				}
			} else {
				fullyQualifiedFilterString = "(" + Constants.OBJECTCLASS + "=" + type.getName() + ")";
			}
			Filter osgiFilter = FrameworkUtil.createFilter(fullyQualifiedFilterString);
			ServiceTracker tracker = new ServiceTracker(bundleContext, osgiFilter, null);
			tracker.open(true);

			// Note that the tracker is not closed to keep the reference
			// This is buggy, as the service reference may change i think

			Object svc = type.cast(tracker.waitForService(timeout));
			if (svc == null) {

				for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, null))) {
					System.err.println("ServiceReference: " + ref);
				}

				for (ServiceReference ref : asCollection(bundleContext.getAllServiceReferences(null, fullyQualifiedFilterString))) {
					System.err.println("Filtered ServiceReference: " + ref);
				}

				throw new RuntimeException("Service query timed out: " + fullyQualifiedFilterString);
			}

			return type.cast(svc);

		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("Invalid filter", e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	private static Collection<ServiceReference> asCollection(ServiceReference[] references) {
		return references != null ? Arrays.asList(references) : Collections.<ServiceReference> emptyList();
	}

}
