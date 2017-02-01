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
package org.siggici.connect.github.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.siggici.connect.github.impl.BaseGitHubApiTest;
import org.siggici.connect.github.organization.GitHubRepo;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;

/**
 * @author  jbellmann
 */
public class RepositoryTemplateTest extends BaseGitHubApiTest {

    protected RepositoryOperations repositoryOperations;

    @Before
    public void buildRespositoryOperations(){
        repositoryOperations = new RepositoryTemplate(restTemplate, true);
    }

    @Test
    public void getRepo() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World")).andExpect(method(GET))
                  .andRespond(withSuccess(jsonResource("repo"), MediaType.APPLICATION_JSON));

        GitHubRepo repo = repositoryOperations.getRepository("octocat", "Hello-World");

        // TODO There are other fields that we need to test.
        assertThat(repo.getId()).isEqualTo(1296269L);
        assertThat(repo.getOwner()).isNotNull();

        // owner
        assertThat(repo.getOwner().getLogin()).isEqualTo("octocat");
        assertThat(repo.getOwner().getId()).isEqualTo(1L);
        assertThat(repo.getOwner().getType()).isEqualTo("User");
        assertThat(repo.getOwner().getAvatarUrl()).isEqualTo("https://github.com/images/error/octocat_happy.gif");

        // repo
        assertThat(repo.getName()).isEqualTo("Hello-World");
        assertThat(repo.getFullName()).isEqualTo("octocat/Hello-World");
        assertThat(repo.getUrl()).isEqualTo("https://api.github.com/repos/octocat/Hello-World");
        assertThat(repo.getHtmlUrl()).isEqualTo("https://github.com/octocat/Hello-World");
        assertThat(repo.getCloneUrl()).isEqualTo("https://github.com/octocat/Hello-World.git");
        assertThat(repo.getGitUrl()).isEqualTo("git://github.com/octocat/Hello-World.git");
        assertThat(repo.getSshUrl()).isEqualTo("git@github.com:octocat/Hello-World.git");
        assertThat(repo.getSvnUrl()).isEqualTo("https://svn.github.com/octocat/Hello-World");
    }

    @Test
    public void getRepos() {
        mockServer.expect(requestTo("https://api.github.com/user/repos?per_page=100")).andExpect(method(GET)).andRespond(
            withSuccess(jsonResource("repositories"), MediaType.APPLICATION_JSON));

        List<GitHubRepo> repos = repositoryOperations.getRepositories("octocat");

        assertThat(repos).isNotNull();
        assertThat(repos).isNotEmpty();
        assertThat(repos.size()).isEqualTo(1);

    }

    @Test
    public void getDeployKeys() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/keys")).andExpect(method(GET))
                  .andRespond(withSuccess(jsonResource("repo-deploy-keys"), APPLICATION_JSON));

        List<GitHubDeployKey> keys = repositoryOperations.getDeployKeys("octocat", "Hello-World");
        assertEquals(1, keys.size());

        GitHubDeployKey key = keys.get(0);
        assertEquals(1, key.getId());
        assertEquals("ssh-rsa AAA...", key.getKey());
        assertEquals("https://api.github.com/repos/octocat/Hello-World/keys/1", key.getUrl());
        assertEquals("octocat@octomac", key.getTitle());
        assertEquals(true, key.isVerified());
        assertEquals(new Date(1418226822000L), key.getCreatedAt());
    }

    @Test
    public void addDeployKey() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/keys"))
                  .andExpect(method(HttpMethod.POST)).andRespond(MockRestResponseCreators.withCreatedEntity(
                          URI.create("https://api.github.com/repos/octocat/Hello-World/keys/1")).contentType(
                          MediaType.APPLICATION_JSON).body(jsonResource("repo-deploy-key")));

        GitHubDeployKey key = repositoryOperations.addDeployKey("octocat", "Hello-World", "octocat@octomac",
                "ssh-rsa AAA...");

        assertEquals(1, key.getId());
        assertEquals("ssh-rsa AAA...", key.getKey());
        assertEquals("https://api.github.com/repos/octocat/Hello-World/keys/1", key.getUrl());
        assertEquals("octocat@octomac", key.getTitle());
        assertEquals(true, key.isVerified());
        assertEquals(new Date(1418226822000L), key.getCreatedAt());
    }

    @Test
    public void getWebhooks() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/hooks")).andExpect(method(GET))
                  .andRespond(withSuccess(jsonResource("webhooks"), MediaType.APPLICATION_JSON));

        List<GitHubWebhook> webhooks = repositoryOperations.getWebhooks("octocat", "Hello-World");

        assertThat(webhooks).isNotNull();
        assertThat(webhooks).isNotEmpty();
        assertThat(webhooks.size()).isEqualTo(1);
    }

    @Test
    public void getWebhook() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/hooks/1")).andExpect(method(GET))
                  .andRespond(withSuccess(jsonResource("webhook"), MediaType.APPLICATION_JSON));

        GitHubWebhook webhook = repositoryOperations.getWebhook("octocat", "Hello-World", "1");

        assertThat(webhook).isNotNull();
    }

    @Test
    public void addWebhook() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/hooks")).andExpect(method(POST))
                  .andRespond(withSuccess(jsonResource("webhook"), MediaType.APPLICATION_JSON));

        AddWebhookRequest request = new AddWebhookRequest();
        request.setActive(true);
        request.setEvents(new String[] {"push"});
        request.setName("siggi-integra");
        request.getConfig().setUrl("https://siggi.endpoints/path/toHook");

        GitHubWebhook webhook = repositoryOperations.addWebhook("octocat", "Hello-World", request);

        assertThat(webhook).isNotNull();
    }

    @Test
    public void deleteWebhook() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/Hello-World/hooks/1"))
                  .andExpect(method(HttpMethod.DELETE)).andRespond(MockRestResponseCreators.withNoContent());

        AddWebhookRequest request = new AddWebhookRequest();
        request.setActive(true);
        request.setEvents(new String[] {"push"});
        request.setName("siggi-integra");
        request.getConfig().setUrl("https://siggi.endpoints/path/toHook");

        repositoryOperations.deleteWebhook("octocat", "Hello-World", "1");
    }

    @Test
    public void combinedStatuses() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/repos/siggi-ci/blub/commits/abcdefgh1234567/status"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(jsonResource("combinedStatus"), APPLICATION_JSON));

        CombinedStatus status = repositoryOperations.getCombinedStatus("siggi-ci", "blub", "abcdefgh1234567");

        Assertions.assertThat(status).isNotNull();
        Assertions.assertThat(status.getTotalCount()).isEqualTo(2);
        Assertions.assertThat(status.getRepository().getId()).isEqualTo(1296269);
    }

    @Test
    public void listStatuses() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/repos/siggi-ci/blub/commits/abcdefgh1234567/statuses"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(jsonResource("statusList"), APPLICATION_JSON));

        List<Status> statusList = repositoryOperations.listStatuses("siggi-ci", "blub", "abcdefgh1234567");

        Assertions.assertThat(statusList).isNotNull();
        Assertions.assertThat(statusList.size()).isEqualTo(1);
        Assertions.assertThat(statusList.get(0).getId()).isEqualTo(1);
    }
    

    @Test
    public void createStatuses() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/repos/siggi-ci/blub/statuses/abcdefgh1234567"))
                .andExpect(method(HttpMethod.POST))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(jsonResource("status"), APPLICATION_JSON));

        StatusRequest request = StatusRequest.pendingStatusRequest();
        request.setTargetUrl("https://ci.example.com/1000/output");
        request.setDescription("Build started");
        request.setContext("continuous-integration/whatever");
        Status status = repositoryOperations.createStatus("siggi-ci", "blub", "abcdefgh1234567", request);

        Assertions.assertThat(status).isNotNull();
        Assertions.assertThat(status.getId()).isEqualTo(1);
        Assertions.assertThat(status.getUrl()).isEqualTo("https://api.github.com/repos/octocat/Hello-World/statuses/1");
    }
}
