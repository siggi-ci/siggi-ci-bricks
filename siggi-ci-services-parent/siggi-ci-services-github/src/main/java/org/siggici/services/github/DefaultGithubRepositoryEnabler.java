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
package org.siggici.services.github;

import java.util.Optional;

import org.siggici.connect.github.repository.GitHubWebhook;
import org.siggici.data.projects.Project;
import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;
import org.siggici.keys.NoDeploykeyFoundException;
import org.siggici.services.project.ProjectService;
import org.springframework.transaction.annotation.Transactional;

class DefaultGithubRepositoryEnabler implements GithubRepositoryEnabler {

    private final DeployKeyStore deployKeyStore;
    private final ProjectService projectService;
    private final GithubWebhookService githubWebhookService;
    private final GithubDeploykeyService githubDeployKeyService;

    DefaultGithubRepositoryEnabler(DeployKeyStore deployKeyStore, ProjectService projectService,
            GithubWebhookService defaultGithubWebhookService, GithubDeploykeyService githubDeployKeyService) {
        this.deployKeyStore = deployKeyStore;
        this.projectService = projectService;
        this.githubWebhookService = defaultGithubWebhookService;
        this.githubDeployKeyService = githubDeployKeyService;
    }

    @Override
    @Transactional
    public void enableGithubRepository(EnableRequest request) {
        final Project p = this.projectService.createProjectIfNotExists(request.getRepositoryInfo(), request.getGithub().getAccessToken());

        Optional<DeployKey> dkOptional = this.deployKeyStore.byId(p.getDeployKeyRef().getAsString(), false);
        DeployKey dk = dkOptional
                .orElseThrow(() -> new NoDeploykeyFoundException(p.getDeployKeyRef().getAsString()));

        Optional<GitHubWebhook> createdWebhook = this.githubWebhookService.registerWebhook(request.getGithub(),
                p.getRepoId());

        this.githubDeployKeyService.registerDeployKey(request.getGithub(), p.getRepoId(), dk);
        // TODO, we should send an EVENT or 'response'?
    }

}
