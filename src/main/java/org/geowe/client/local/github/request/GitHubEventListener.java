package org.geowe.client.local.github.request;




public interface GitHubEventListener<T> {
	public void onFinish(T response);	
}
