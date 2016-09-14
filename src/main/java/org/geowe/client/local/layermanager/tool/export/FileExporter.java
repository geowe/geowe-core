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
package org.geowe.client.local.layermanager.tool.export;

import java.util.List;


/**
 * Utility class responsible to export a file
 * 
 * @author geowe.org
 *
 */
public final class FileExporter implements Exporter {

	/**
	 * Export a text/plain UTF-8 file.
	 * 
	 * @param text
	 * @param fileName
	 */
	public static native void saveAs(String text, String fileName) /*-{
		var blob = new Blob([ text ], {
			type : "text/plain;charset=utf-8;",
		});
		$wnd.saveAs(blob, fileName);

	}-*/;

	@Override
	public void export(List<String> parameters) {
		final String content = parameters.get(0);
		final String extension = parameters.get(1);
		final String fileName = parameters.get(2)+ "." + extension;
		 
		saveAs(content, fileName);		
	}
}