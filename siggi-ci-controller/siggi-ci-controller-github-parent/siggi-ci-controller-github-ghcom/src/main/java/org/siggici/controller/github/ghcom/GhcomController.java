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
package org.siggici.controller.github.ghcom;

import static org.siggici.controller.github.ghcom.GhcomMappings.ORGANIZATIONS;
import static org.siggici.controller.github.ghcom.GhcomMappings.ORGA_REPOSITORIES;
import static org.siggici.controller.github.ghcom.GhcomMappings.REPOSITORY_DISABLE;
import static org.siggici.controller.github.ghcom.GhcomMappings.REPOSITORY_ENABLE;
import static org.siggici.controller.github.ghcom.GhcomMappings.USER;
import static org.siggici.controller.github.ghcom.GhcomMappings.USER_REPOSITORIES;

import java.util.List;

import org.siggici.connect.github.ghcom.api.Ghcom;
import org.siggici.services.common.Organization;
import org.siggici.services.common.RepositoryInfo;
import org.siggici.services.common.User;
import org.siggici.services.github.GithubExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GhcomController {

    private final Ghcom ghcom;
    private final GithubExecutor executor;

    @Autowired
    public GhcomController(Ghcom ghcom, GithubExecutor githubExecutor) {
        this.ghcom = ghcom;
        this.executor = githubExecutor;
    }

    @GetMapping(USER)
    User getUser() {
        return this.executor.getUser(ghcom);
    }

    @GetMapping(ORGANIZATIONS)
    List<Organization> getOrganizations() {
        return this.executor.getOrganizations(ghcom);
    }

    @GetMapping(USER_REPOSITORIES)
    public List<RepositoryInfo> getUserRepos() {
        String username = getUser().getName();
        return this.executor.getRepositories(ghcom, username);
    }

    @GetMapping(ORGA_REPOSITORIES)
    List<RepositoryInfo> getOrgaRepositories(@PathVariable String orga) {
        return this.executor.getOrganizationRepositories(ghcom, orga);
    }

    @PostMapping(REPOSITORY_ENABLE)
    RepositoryInfo enableRepository(@RequestBody RepositoryInfo repositoryInfo) {
        return this.executor.enableRepository(ghcom, repositoryInfo);
    }

    @PostMapping(REPOSITORY_DISABLE)
    RepositoryInfo disableRepository(@RequestBody RepositoryInfo repositoryInfo) {
        return this.executor.disableRepository(ghcom, repositoryInfo);
    }

}
