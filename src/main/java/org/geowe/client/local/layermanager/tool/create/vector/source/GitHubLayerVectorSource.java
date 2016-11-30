package org.geowe.client.local.layermanager.tool.create.vector.source;

public class GitHubLayerVectorSource extends URLLayerVectorSource {
	private String sha;
	
	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}
	
}
