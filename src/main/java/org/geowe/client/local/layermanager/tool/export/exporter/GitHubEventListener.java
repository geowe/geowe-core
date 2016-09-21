package org.geowe.client.local.layermanager.tool.export.exporter;

import org.geowe.client.shared.rest.github.GitHubContentResponse;


public interface GitHubEventListener {
	public void onFinish(GitHubContentResponse response);
}
