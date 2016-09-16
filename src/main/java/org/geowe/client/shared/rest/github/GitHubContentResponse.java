package org.geowe.client.shared.rest.github;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GitHubContentResponse {
	private String downloadUrl;
	
	public GitHubContentResponse() {
		
	}
	
	public GitHubContentResponse(@MapsTo("download_url") String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
