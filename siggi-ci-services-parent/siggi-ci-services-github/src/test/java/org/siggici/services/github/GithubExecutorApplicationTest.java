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

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siggici.connect.github.Github;
import org.siggici.connect.github.repository.GitHubDeployKey;
import org.siggici.connect.github.repository.GitHubWebhook;
import org.siggici.connect.github.repository.GitHubWebhookConfig;
import org.siggici.connect.github.repository.RepositoryOperations;
import org.siggici.data.jpa.RepoId;
import org.siggici.data.projects.Project;
import org.siggici.data.projects.ProjectRepository;
import org.siggici.keys.DeployKeyStore;
import org.siggici.services.common.RepositoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("enable")
public class GithubExecutorApplicationTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5932).build();

    @Autowired
    private GithubRepositoryEnabler repositoryEnabler;

    @Autowired
    private DeployKeyStore deployKeyStore;

    @Autowired
    private ProjectRepository projectRepository;

    private Github github;
    private RepositoryOperations repositoryOperations;
    private RepositoryInfo repositoryInfo;

    @Before
    public void setUp() {
        github = Mockito.mock(Github.class);
        repositoryOperations = Mockito.mock(RepositoryOperations.class);
        when(github.getRepositoryOperations()).thenReturn(repositoryOperations);
        repositoryInfo = new RepositoryInfo();
        repositoryInfo.setProvider("ghcom");
        repositoryInfo.setOrga("orga1");
        repositoryInfo.setName("repo1");
    }

    @Test
    public void contextLoads() {

        final String id = deployKeyStore.create();
        Assertions.assertThat(id).isNotNull();

        EnableRequest request = EnableRequest.builder().github(github).repositoryInfo(repositoryInfo)
                .userInfo(new Object()).build();

        when(repositoryOperations.addWebhook(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(new GitHubWebhook());
        when(repositoryOperations.addDeployKey(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(new GitHubDeployKey());
        when(repositoryOperations.getWebhooks(Mockito.anyString(), Mockito.anyString())).thenReturn(listOfWebhooks());

        this.repositoryEnabler.enableGithubRepository(request);
        final RepoId repoId = buildRepoId(repositoryInfo);
        Optional<Project> projectOptional = this.projectRepository.findByRepoId(repoId);
        Assertions.assertThat(projectOptional).isNotEmpty();
    }

    protected RepoId buildRepoId(RepositoryInfo repositoryInfo) {
        return RepoId.builder().provider(repositoryInfo.getProvider()).orga(repositoryInfo.getOrga())
                .repo(repositoryInfo.getName()).build();
    }

    protected List<GitHubWebhook> listOfWebhooks() {
        List<GitHubWebhook> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            GitHubWebhook h = new GitHubWebhook();
            h.setActive(true);
            h.setId(i + 1);
            h.setCreatedAt(new Date());
            h.setEvents(new String[] { "*" });
            h.setName("hook-" + i);
            GitHubWebhookConfig config = new GitHubWebhookConfig();
            config.setContentType("json");
            config.setUrl("https://user:password@host.test/hooks");
            h.setConfig(config);
            result.add(h);
        }
        return result;
    }
}
