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
package org.geowe.server.upload;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1296384085403737411L;

	private static final long MAX_FILE_SIZE = 1024 * 1024 * 2; //2MB 
	private static final Logger LOG = LoggerFactory
			.getLogger(FileUploadServlet.class.getName());

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final ServletFileUpload upload = new ServletFileUpload();
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_FILE_SIZE);

		try {
			final FileItemIterator iter = upload.getItemIterator(request);
			final StringWriter writer = new StringWriter();
			while (iter.hasNext()) {
				final FileItemStream item = iter.next();				
				IOUtils.copy(item.openStream(), writer, "UTF-8");
				final String content = writer.toString();
				response.setStatus(HttpStatus.SC_OK);
				response.getWriter().printf(content);
			}
		} catch (SizeLimitExceededException e) {
			response.setStatus(HttpStatus.SC_REQUEST_TOO_LONG);
			response.getWriter().printf(
					HttpStatus.SC_REQUEST_TOO_LONG + ":" + e.getMessage());
			LOG.error(e.getMessage());
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().printf(
					HttpStatus.SC_INTERNAL_SERVER_ERROR
							+ ": ups! something went wrong.");
			LOG.error(e.getMessage());
		}
	}
}
