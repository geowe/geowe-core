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
 * Utility Class to get base64 hash
 * 
 * @author jose@geowe.org, rafa@geowe.org
 * @since 08-02-2017
 * @author rafa@geowe.org implementation changed to avoid UTF-8 encoding errors.
 * 
 */
public final class Base64 {

	/**
	 * To prevent instantiation
	 */
	private Base64() {
	}

	public static native String decode(String a) /*-{
		return decodeURIComponent(Array.prototype.map.call(
				atob(str),
				function(c) {
					return '%'
							+ ('00' + c.charCodeAt(0).toString(16)).slice(-2);
				}).join(''));
	}-*/;

	public static native String encode(String a) /*-{
		return btoa(encodeURIComponent(a).replace(/%([0-9A-F]{2})/g,
				function(match, p1) {
					return String.fromCharCode('0x' + p1);
				}));
	}-*/;

}
