package org.geowe.client.shared.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InfoCommitGitHub {

	@XmlElement public String message;
    @XmlElement public String content;
    
    public InfoCommitGitHub() {
    	
    }
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
