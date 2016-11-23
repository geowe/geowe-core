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
package org.geowe.client.shared.rest.github.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.geowe.client.shared.rest.github.response.GitHubFileListAttributeBean;
/**
 * 
 * @author jose@geowe.org
 *
 */
@Path("/{user}/{repo}/contents/{path}")
public interface GitHubFileListService {
	
	@GET	
	@Consumes("application/json")
	public List<GitHubFileListAttributeBean> getFileList(@PathParam("user") String user, @PathParam("repo") String repository, @PathParam("path") String path);
		
}