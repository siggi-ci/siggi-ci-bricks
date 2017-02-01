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
package org.siggici.controller.github.ghe;

import static org.siggici.controller.github.ghe.GheMappings.ORGANIZATIONS;
import static org.siggici.controller.github.ghe.GheMappings.ORGA_REPOSITORIES;
import static org.siggici.controller.github.ghe.GheMappings.REPOSITORY_DISABLE;
import static org.siggici.controller.github.ghe.GheMappings.REPOSITORY_ENABLE;
import static org.siggici.controller.github.ghe.GheMappings.USER;
import static org.siggici.controller.github.ghe.GheMappings.USER_REPOSITORIES;

import java.util.List;

import org.siggici.connect.github.ghe.api.Ghe;
import org.siggici.services.common.Organization;
import org.siggici.services.common.RepositoryInfo;
import org.siggici.services.common.User;
import org.siggici.services.github.GithubExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GheController {

    private final Ghe ghe;
    private final GithubExecutor executor;

    @Autowired
    GheController(Ghe ghe, GithubExecutor executor) {
        Assert.notNull(ghe, "'ghe' should never be null");
        this.ghe = ghe;
        this.executor = executor;
    }

    @GetMapping(USER)
    User getUser() {
        return this.executor.getUser(ghe);
    }

    @GetMapping(ORGANIZATIONS)
    List<Organization> getOrganizations() {
        return this.executor.getOrganizations(ghe);
    }

    @GetMapping(USER_REPOSITORIES)
    public List<RepositoryInfo> getUserRepos() {
        String username = getUser().getName();
        return this.executor.getRepositories(ghe, username);
    }

    @GetMapping(ORGA_REPOSITORIES)
    List<RepositoryInfo> getOrgaRepositories(@PathVariable String orga) {
        return this.executor.getOrganizationRepositories(ghe, orga);
    }

    @PostMapping(REPOSITORY_ENABLE)
    RepositoryInfo enableRepository(@RequestBody RepositoryInfo repositoryInfo) {
        return this.executor.enableRepository(ghe, repositoryInfo);
    }

    @PostMapping(REPOSITORY_DISABLE)
    RepositoryInfo disableRepository(@RequestBody RepositoryInfo repositoryInfo) {
        return this.executor.disableRepository(ghe, repositoryInfo);
    }
}
