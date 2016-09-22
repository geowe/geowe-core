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
package org.geowe.client.local.layermanager.tool.export.exporter.github;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.github.response.GitHubContentResponse;
import org.geowe.client.shared.rest.github.service.GitHubFileService;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

/**
 * https://api.github.com/repos/{user}/{repository}/contents/{path}/{filename.extension}
 * 
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GitHubGetFileRequest  {
	private final static String URL_BASE = "https://api.github.com/repos/";	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private ProgressBarDialog autoMessageBox;	
	private List<GitHubEventListener<GitHubContentResponse>> listener = new ArrayList<GitHubEventListener<GitHubContentResponse>>();
	
	public void send(GitHubParameter gitHubParameter) {
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();		
		final String fileName = gitHubParameter.getFileName() + "."
				+ gitHubParameter.getExtension();		
		final String userName = gitHubParameter.getUserName();		
		final String repository = gitHubParameter.getRepository();
		final String path = gitHubParameter.getPath();
				
		RestClient.setJacksonMarshallingActive(true);
		RestClient.create(GitHubFileService.class, URL_BASE, getRemoteCallback(),
				getErrorCallback(), Response.SC_OK).getFile(userName,
				repository, path, fileName);
	}
	
	public void addListener(GitHubEventListener<GitHubContentResponse> event) {
		listener.add(event);
	}

	private RestErrorCallback getErrorCallback() {
		return new RestErrorCallback() {

			@Override
			public boolean error(Request request, Throwable throwable) {
				autoMessageBox.hide();
				String message = "";
				int defaultCodeError = Response.SC_NOT_MODIFIED;
				try {
					throw throwable;
				} catch (ResponseException e) {
					Response response = e.getResponse();					
					message = response.getStatusText(); 
					defaultCodeError = response.getStatusCode();
					
				} catch (Throwable t) {
					message = t.getMessage();
				}
				
				messageDialogBuilder.createError(UIMessages.INSTANCE.warning() + " " + defaultCodeError, message).show();
				
				return false;
			}
		};
	}

	private RemoteCallback<GitHubContentResponse> getRemoteCallback() {
		return new RemoteCallback<GitHubContentResponse>() {

			@Override
			public void callback(GitHubContentResponse response) {
				autoMessageBox.hide();
				notifyListener(response);										
			}
		};
	}
	
	private void notifyListener(GitHubContentResponse response) {
		for(GitHubEventListener<GitHubContentResponse> event: listener) {
			event.onFinish(response);
		}
	}
}
