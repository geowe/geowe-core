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

import org.geowe.client.local.github.request.GitHubParameter;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.local.util.Base64;
import org.geowe.client.local.util.BasicAuthenticationProvider;
import org.geowe.client.shared.rest.github.GitHubCreateFileRequest;
import org.geowe.client.shared.rest.github.response.GitHubContentResponse;
import org.geowe.client.shared.rest.github.response.GitHubResponse;
import org.geowe.client.shared.rest.github.service.GitHubFileService;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

/**
 * https://api.github.com/repos/{user}/{repository}/contents/{path}/{filename.
 * extension}
 * 
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GitHubCreateFileExporter implements Exporter {
	private final static String URL_BASE = "https://api.github.com/repos/";

	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private ProgressBarDialog autoMessageBox;
	private GitHubParameter gitHubParameter;

	@Override
	public void export(FileParameter fileParameter) {
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();

		final String fileName = fileParameter.getFileName() + "."
				+ fileParameter.getExtension();
		gitHubParameter = (GitHubParameter) fileParameter;
		final String userName = gitHubParameter.getUserName();
		final String password = gitHubParameter.getPassword();
		final String repository = gitHubParameter.getRepository();
		final String path = gitHubParameter.getPath();
		final String message = gitHubParameter.getMessageCommit();
		final String authorizationHeaderValue = BasicAuthenticationProvider
				.getAuthorizationHeaderValue(userName, password);
		final GitHubCreateFileRequest content = new GitHubCreateFileRequest();
		content.setContent(Base64.encode(fileParameter.getContent()));
		content.setMessage(message);
		RestClient.setJacksonMarshallingActive(true);
		RestClient.create(GitHubFileService.class, URL_BASE,
				getRemoteCallback(), getErrorCallback(), Response.SC_CREATED)
				.createFile(userName, repository, path, fileName,
						authorizationHeaderValue, content);
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

				messageDialogBuilder.createError(
						UIMessages.INSTANCE.warning() + " " + defaultCodeError,
						message).show();

				return false;
			}
		};
	}

	private RemoteCallback<GitHubResponse> getRemoteCallback() {
		return new RemoteCallback<GitHubResponse>() {

			@Override
			public void callback(GitHubResponse response) {
				autoMessageBox.hide();
				GitHubContentResponse content = response.getContent();
				String url = content.getDownloadUrl();
				messageDialogBuilder.createInfo(
						UIMessages.INSTANCE.gitHubResponseTitle(),
						UIMessages.INSTANCE.gitHubSavedSucsessfully(url))
						.show();
			}

		};
	}
}
