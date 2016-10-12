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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.geowe.client.shared.rest.github.GitHubCreateFileRequest;
import org.geowe.client.shared.rest.github.GitHubUpdateFileRequest;
import org.geowe.client.shared.rest.github.response.GitHubContentResponse;
import org.geowe.client.shared.rest.github.response.GitHubResponse;
/**
 * 
 * @author jose@geowe.org
 *
 */
@Path("/{user}/{repository}")
public interface GitHubFileService {

	
	@GET
	@Path("/contents/{path}/{fileName}")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public GitHubContentResponse getFile(@PathParam("user") String user,
			@PathParam("repository") String repository,
			@PathParam("path") String path,
			@PathParam("fileName") String fileName);
	
	@PUT
	@Path("/contents/{path}/{fileName}")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	@GZIP
	public GitHubResponse createFile(@PathParam("user") String user,
			@PathParam("repository") String repository,
			@PathParam("path") String path,
			@PathParam("fileName") String fileName,
			@HeaderParam("Authorization") String authorization,
			GitHubCreateFileRequest content);
	
	@PUT
	@Path("/contents/{path}/{fileName}")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	@GZIP
	public GitHubResponse updateFile(@PathParam("user") String user,
			@PathParam("repository") String repository,
			@PathParam("path") String path,
			@PathParam("fileName") String fileName,
			@HeaderParam("Authorization") String authorization,
			GitHubUpdateFileRequest content);
}
