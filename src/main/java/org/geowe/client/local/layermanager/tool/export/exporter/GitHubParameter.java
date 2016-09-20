package org.geowe.client.local.layermanager.tool.export.exporter;

public class GitHubParameter extends FileParameter {
	private String password;
	private String repository;
	private String path;
	private String messageCommit;
	private String sha;
	
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	
	public GitHubParameter() {
		
	}
	
	public GitHubParameter(FileParameter fileParameter) {
		setContent(fileParameter.getContent());
		setExtension(fileParameter.getExtension());
		setFileName(fileParameter.getFileName());
	}
	
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMessageCommit() {
		return messageCommit;
	}
	public void setMessageCommit(String messageCommit) {
		this.messageCommit = messageCommit;
	}	
}
