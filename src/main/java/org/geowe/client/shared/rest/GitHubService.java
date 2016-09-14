package org.geowe.client.shared.rest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;



	

@Path("/{user}/{repository}")
public interface GitHubService {

	@PUT
	@Path("/contents/{path}/{fileName}")	
	// @Produces("application/json")
	// @InterceptedCall(MyCacheInterceptor.class)
	public void createFile(@PathParam("user") String user, @PathParam("repository") String repository,
			@PathParam("path") String path, @PathParam("fileName") String fileName, 
			@HeaderParam("Authorization") String authorization, InfoCommitGitHub info);
}
