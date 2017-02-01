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
package org.siggici.services.project;

import java.util.Optional;

import org.siggici.data.ids.DeployKeyRef;
import org.siggici.data.ids.EncryptionKey;
import org.siggici.data.ids.EncryptionKeyService;
import org.siggici.data.jpa.RepoId;
import org.siggici.data.projects.Project;
import org.siggici.data.projects.ProjectRepository;
import org.siggici.keys.DeployKeyStore;
import org.siggici.services.common.RepositoryInfo;

public class ProjectService {

    private final EncryptionKeyService encryptionKeyService;
    private final DeployKeyStore deployKeyStore;
    private final ProjectRepository projectRepository;

    public ProjectService(EncryptionKeyService encryptionKeyService, DeployKeyStore deployKeyStore,
            ProjectRepository projectRepository) {
        this.encryptionKeyService = encryptionKeyService;
        this.deployKeyStore = deployKeyStore;
        this.projectRepository = projectRepository;
    }

    public Project createProjectIfNotExists(RepositoryInfo repositoryInfo, String accessToken) {
        final RepoId repoId = buildRepoId(repositoryInfo);
        Optional<Project> alreadyExists = projectRepository.findByRepoId(repoId);
        return alreadyExists.orElseGet( () -> {
            final String deploykeyId = deployKeyStore.create();
            final DeployKeyRef deployKeyRef = DeployKeyRef.builder().deployKeyReference(deploykeyId).build();
            final EncryptionKey encryptionKey = encryptionKeyService.create();
            Project p = Project.builder().deployKeyRef(deployKeyRef).encryptionKey(encryptionKey).repoId(repoId).accessToken(accessToken).build();
            p.getRepositoryDetails().setPrivateRepo(repositoryInfo.isPrivateRepo());
            p.getRepositoryDetails().setWebUrl(repositoryInfo.getHtmlUrl());
            return projectRepository.save(p);
        });
    }

    public void disableProjectIfExists(RepositoryInfo repositoryInfo) {
        final RepoId repoId = buildRepoId(repositoryInfo);
        Optional<Project> projectOptional = this.projectRepository.findByRepoId(repoId);
        projectOptional.ifPresent(p -> {
            p.deActivate();
            this.projectRepository.save(p);
        });
    }

    protected RepoId buildRepoId(RepositoryInfo repositoryInfo) {
        return RepoId.builder().provider(repositoryInfo.getProvider()).orga(repositoryInfo.getOrga())
                .repo(repositoryInfo.getName()).build();
    }

}
