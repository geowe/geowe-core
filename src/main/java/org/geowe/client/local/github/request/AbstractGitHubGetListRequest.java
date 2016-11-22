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

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
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
public abstract class AbstractGitHubGetListRequest <T> {
	protected final static String URL_BASE = "https://api.github.com/";
	@Inject
	protected MessageDialogBuilder messageDialogBuilder;
	protected ProgressBarDialog autoMessageBox;	
	protected GitHubListEventListener<T> listener;// = new ArrayList<GitHubListEventListener<T>>();
	
		
	public void setListener(GitHubListEventListener<T> event) {
		listener = event;
	}

	protected RestErrorCallback getErrorCallback() {
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

	protected RemoteCallback<List<T>> getRemoteCallback() {
		return new RemoteCallback<List<T>>() {

			@Override
			public void callback(List<T> response) {
				autoMessageBox.hide();
				notifyListener(response);										
			}
		};
	}
	
	private void notifyListener(List<T> response) {
		//for(GitHubListEventListener<T> event: listener) {
			listener.onFinish(response);			
		//}
	}
}
