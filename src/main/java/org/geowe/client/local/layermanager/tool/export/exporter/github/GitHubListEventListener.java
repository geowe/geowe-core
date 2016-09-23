package org.geowe.client.local.layermanager.tool.export.exporter.github;

import java.util.List;




public interface GitHubListEventListener<T> {
	public void onFinish(List<T> response);	
}
