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
package org.geowe.client.local.layermanager.tool.export.exporter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.local.util.Base64;
import org.geowe.client.local.util.BasicAuthenticationProvider;
import org.geowe.client.shared.rest.github.GitHubContentResponse;
import org.geowe.client.shared.rest.github.GitHubResponse;
import org.geowe.client.shared.rest.github.GitHubService;
import org.geowe.client.shared.rest.github.GitHubUpdateFileRequest;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.slf4j.Logger;

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
public class GitHubGetFileExporter implements Exporter {
	private final static String URL_BASE = "https://api.github.com/repos/";
	@Inject
	private Logger log;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private ProgressBarDialog autoMessageBox;
	private String sha;

	@Override
	public void export(final FileParameter fileParameter) {
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();
		
		if(fileParameter == null) {
			log.error("Los parametros son nulos");
			autoMessageBox.hide();
		}
		
		
		final String fileName = fileParameter.getFileName() + "."
				+ fileParameter.getExtension();
		log.info("fileName: " + fileName);
//		final String fileName = "Andalusian provinces.geojson";
		final GitHubParameter gitHubParameter = (GitHubParameter) fileParameter;
		final String userName = gitHubParameter.getUserName();
		log.info("UserName: " + userName);
		final String password = gitHubParameter.getPassword();
		log.info("Password: " + password);
		final String repository = gitHubParameter.getRepository();
		log.info("repository: " + repository);
		final String path = gitHubParameter.getPath();
		log.info("path: " + path);
		final String authorizationHeaderValue = BasicAuthenticationProvider
				.getAuthorizationHeaderValue(userName, password);
		log.info("authorizationHeaderValue: " + authorizationHeaderValue);
		
		RestClient.setJacksonMarshallingActive(true);
		RestClient.create(GitHubService.class, URL_BASE, getRemoteCallback(),
				getErrorCallback(), Response.SC_OK).getFile(userName,
				repository, path, fileName, authorizationHeaderValue);
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
				sha = response.getSha();
								
				messageDialogBuilder.createInfo(UIMessages.INSTANCE.gitHubResponseTitle(),sha).show();
			}
		};
	}
}
