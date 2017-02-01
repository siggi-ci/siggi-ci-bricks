/**
 * Copyright (C) 2016 Joerg Bellmann (joerg.bellmann@googlemail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.siggici.connect.github.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

	private long id;

	private String url;

	private String name;

	private String slug;

	private String description;

	private String privacy;

	private String permission;

	@JsonProperty("members_url")
	private String membersUrl;

	@JsonProperty("repositories_url")
	private String repositoriesUrl;

	@JsonProperty("members_count")
	private long membersCount;

	@JsonProperty("repos_count")
	private long reposCount;

	private GitHubOrganization organization;

}
