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
package org.geowe.client.local.github.request;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.github.service.GitHubFileListService;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;

import com.google.gwt.http.client.Response;

/**
 * https://api.github.com/repos/jmmluna/geodata/contents/
 * https://developer.github.com/v3/repos/contents/#get-contents
 * 
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GitHubGetFileListRequest <T> extends AbstractGitHubGetListRequest {
		
	public void send(String userName, String repository, String path) {
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();		
				
		RestClient.setJacksonMarshallingActive(true);
		RestClient.create(GitHubFileListService.class, URL_BASE + "repos/", getRemoteCallback(),
				getErrorCallback(), Response.SC_OK).getFileList(userName, repository, path);
	}
		
}
