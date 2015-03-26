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
package org.ojbc.util.xml;

import java.util.ListIterator;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;

public class NamespaceChangingVisitor extends VisitorSupport {

	private Namespace from;
	private Namespace to;

	public NamespaceChangingVisitor(Namespace from, Namespace to) {
		this.from = from;
		this.to = to;
	}

	@SuppressWarnings("unchecked")
	public void visit(Element node) {
		Namespace ns = node.getNamespace();

		if (ns.getURI().equals(from.getURI())) {
			QName newQName = new QName(node.getName(), to);
			node.setQName(newQName);
		}

		ListIterator namespaces = node.additionalNamespaces().listIterator();
		while (namespaces.hasNext()) {
			Namespace additionalNamespace = (Namespace) namespaces.next();
			if (additionalNamespace.getURI().equals(from.getURI())) {
				namespaces.remove();
			}
		}
	}

}
