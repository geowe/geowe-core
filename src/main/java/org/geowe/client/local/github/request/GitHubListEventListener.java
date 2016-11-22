package org.geowe.client.local.github.request;

import java.util.List;




public interface GitHubListEventListener<T> {
	public void onFinish(List<T> response);	
}
