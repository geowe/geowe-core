package org.geowe.client.local.layermanager.tool.export.exporter.github;




public interface GitHubEventListener<T> {
	public void onFinish(T response);	
}
