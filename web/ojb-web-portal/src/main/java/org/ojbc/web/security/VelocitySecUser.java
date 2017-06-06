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
package org.ojbc.web.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
/**
 * This class is needed only because we are using velocity and are not able to use 
 * Spring security tags for JSP pages. 
 */
@Component
public class VelocitySecUser {

    /**
     * Gets the user name of the user from the Authentication object
     *
     * @return the user name as string
     */
    public static String getPrincipal() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (obj instanceof UserDetails) {
            return ((UserDetails) obj).getUsername();
        } else {
            return "Guest";
        }
    }

    /**
     * Is the user granted all of the grantedAuthorities passed in
     *
     * @param roles
     *            a string array of grantedAuth
     * @return true if user has all of the listed authorities/roles, otherwise
     *         false
     */
    public static boolean allGranted(String... checkForAuths) {
        Set<String> userAuths = getUserAuthorities();
        for (String auth : checkForAuths) {
            if (userAuths.contains(auth))
                continue;
            return false;
        }
        return true;
    }

    /**
     * Is the user granted any of the grantedAuthorities passed into
     *
     * @param roles
     *            a string array of grantedAuth
     * @return true if user has any of the listed authorities/roles, otherwise
     *         false
     */
    public static boolean anyGranted(String... checkForAuths) {
        Set<String> userAuths = getUserAuthorities();
        for (String auth : checkForAuths) {
            if (userAuths.contains(auth))
                return true;
        }
        return false;
    }

    /**
     * is the user granted none of the supplied roles
     *
     * @param roles
     *            a string array of roles
     * @return true only if none of listed roles are granted
     */
    public static boolean noneGranted(String... checkForAuths) {
        Set<String> userAuths = getUserAuthorities();
        for (String auth : checkForAuths) {
            if (userAuths.contains(auth))
                return false;
        }
        return true;
    }

    private static Set<String> getUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = new HashSet<String>();
        @SuppressWarnings("unchecked")
        Collection<SimpleGrantedAuthority> gas = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities(); 
        for (GrantedAuthority ga : gas) {
            roles.add(ga.getAuthority());
        }
        return roles;
    }
}