package org.geowe.client.shared.rest.github;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GitHubCommitResponse {
	private String message;
	
	public GitHubCommitResponse() {
		
	}
	
	public GitHubCommitResponse(@MapsTo("message") String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
