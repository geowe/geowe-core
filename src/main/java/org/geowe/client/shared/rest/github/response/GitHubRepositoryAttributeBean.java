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
package org.geowe.client.shared.rest.github.response;

import java.io.Serializable;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * 
 * @author jose@geowe.org
 *
 */
@Portable
public class GitHubRepositoryAttributeBean {
	
	private Integer attributeId;
	private String attributeName;
	private String attributeFullName;

	
	public GitHubRepositoryAttributeBean(@MapsTo("id") Integer id, @MapsTo("name") String name, @MapsTo("full_Name")String fullName) {
		this.attributeId = id;
		this.attributeName = name;
		this.attributeFullName = fullName;
	}

	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeFullName() {
		return attributeFullName;
	}
	public void setAttributeFullName(String attributeFullName) {
		this.attributeFullName = attributeFullName;
	}
	public Integer getAttributeId() {
		return attributeId;
	}
	
	public void setAttributeId(Integer id) {
		this.attributeId = id;
	}
	

	@Override
	public String toString() {
		return "Repository [" + attributeName + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GitHubRepositoryAttributeBean other = (GitHubRepositoryAttributeBean) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		return true;
	}	 
}
