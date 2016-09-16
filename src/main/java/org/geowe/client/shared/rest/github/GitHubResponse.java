/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.shared.rest.github;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GitHubResponse {
	
	public GitHubResponse() {
		
	}
	
	public GitHubResponse(@MapsTo("content") GitHubContentResponse content, @MapsTo("commit") GitHubCommitResponse commit) {
		this.content = content;
		this.commit = commit;
	}
	
	private GitHubContentResponse content;
	private GitHubCommitResponse commit;
	
	
	public GitHubContentResponse getContent() {
		return content;
	}
	public void setContent(GitHubContentResponse content) {
		this.content = content;
	}
	public GitHubCommitResponse getCommit() {
		return commit;
	}
	public void setCommit(GitHubCommitResponse commit) {
		this.commit = commit;
	}
}
