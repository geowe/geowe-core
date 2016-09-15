/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.util;
/**
 *  Proporciona el valor de la autorización para una autenticación básica
 * @author jose@geowe.org
 *
 */
public class BasicAuthenticationProvider {
	private static final String AUTH_VALUE = "Basic";
	
	public static String getAuthorizationHeaderValue(final String userName, final String password) { 
		final String usernameAndPassword = userName + ":" + password;
        final String authorizationHeaderValue  = AUTH_VALUE + " " + Base64.encode(usernameAndPassword);
        return authorizationHeaderValue;
	}	
}
