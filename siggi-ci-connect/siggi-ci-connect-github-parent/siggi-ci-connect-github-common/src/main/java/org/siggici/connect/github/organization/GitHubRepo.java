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

import java.io.Serializable;

import org.siggici.connect.github.repository.GitHubRepoOwner;

/**
 * A GitHub repo.
 *
 * @author  Willie Wheeler (willie.wheeler@gmail.com)
 * @author  Andy Wilkinson
 */
@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepo implements Serializable {

    private long id;

    private GitHubRepoOwner owner;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("private")
    private boolean privateRepo;

    private boolean fork;

    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("git_url")
    private String gitUrl;

    @JsonProperty("ssh_url")
    private String sshUrl;

    @JsonProperty("svn_url")
    private String svnUrl;

    @JsonProperty("mirror_url")
    private String mirrorUrl;

    private String homepage;

    private String language;

    @JsonProperty("default_branch")
    private String defaultBranch;

}
