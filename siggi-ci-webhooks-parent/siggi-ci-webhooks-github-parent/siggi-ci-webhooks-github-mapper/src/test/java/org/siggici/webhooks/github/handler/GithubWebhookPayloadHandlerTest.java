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
package org.siggici.webhooks.github.handler;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.siggici.data.jpa.RepoId;
import org.siggici.data.projects.Project;
import org.siggici.data.projects.ProjectRepository;
import org.siggici.data.projects.RepositoryDetails;
import org.siggici.webhooks.HookPayloadEvent;
import org.siggici.webhooks.github.GithubWebhookPayloadHandler;
import org.siggici.webhooks.github.PullRequestPayload;
import org.siggici.webhooks.services.build.BuildDefinitionFetcher;
import org.siggici.webhooks.services.build.BuildService;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import javaslang.Tuple;

public class GithubWebhookPayloadHandlerTest {

    private ObjectMapper mapper = new ObjectMapper();

    private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
    private BuildService buildService = Mockito.mock(BuildService.class);
    private Project project;
    private RepositoryDetails repositoryDetails;
    private BuildDefinitionFetcher buildDefinitionFetcher;

    @Before
    public void setUp() {
        project = Mockito.mock(Project.class);
        repositoryDetails = Mockito.mock(RepositoryDetails.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        buildService = Mockito.mock(BuildService.class);
        buildDefinitionFetcher = Mockito.mock(BuildDefinitionFetcher.class);
    }

    @Test
    public void testSupports() throws JsonParseException, JsonMappingException, IOException {

        GithubWebhookPayloadHandler handler = new GithubWebhookPayloadHandler(projectRepository, buildService,
                buildDefinitionFetcher);

        Assertions.assertThat(handler.supports(buildHookPayloadEvent())).isTrue();
    }

    @Test
    public void testSupportsShouldBeFalseWhenProviderIsBitbucket()
            throws JsonParseException, JsonMappingException, IOException {

        GithubWebhookPayloadHandler handler = new GithubWebhookPayloadHandler(projectRepository, buildService,
                buildDefinitionFetcher);

        HookPayloadEvent event = buildHookPayloadEventNonGithub();

        Assertions.assertThat(handler.supports(event)).isFalse();
    }

    @Test
    public void testGithubWebhookPayloadHandler() throws JsonParseException, JsonMappingException, IOException {

        when(projectRepository.findByRepoId(Mockito.any(RepoId.class))).thenReturn(Optional.of(project));
        when(project.getAccessToken()).thenReturn("1234567890");
        when(project.getRepositoryDetails()).thenReturn(repositoryDetails);
        when(repositoryDetails.getWebUrl()).thenReturn("https://github.com/siggi-ci/hooks");
        when(buildDefinitionFetcher.fetch(Mockito.any(String.class), Mockito.any(String.class), Mockito.anyString())).thenReturn(Tuple.of(Optional.empty(), Optional.empty()));

        GithubWebhookPayloadHandler handler = new GithubWebhookPayloadHandler(projectRepository, buildService,
                buildDefinitionFetcher);
        handler.handle(buildHookPayloadEvent());
    }

    protected HookPayloadEvent buildHookPayloadEvent() throws JsonParseException, JsonMappingException, IOException {
        Map<String, String> hookEventPayload = Maps.newHashMap();
        hookEventPayload.put("eventType", "pull_request");
        hookEventPayload.put("providerType", "github");
        hookEventPayload.put("created", System.currentTimeMillis() + "");
        PullRequestPayload payload = mapper.readValue(readClasspathResource("/pr_opened.json", getClass()),
                PullRequestPayload.class);

        hookEventPayload.put("payload", mapper.writeValueAsString(payload));

        return HookPayloadEvent.build(hookEventPayload);
    }

    protected HookPayloadEvent buildHookPayloadEventNonGithub()
            throws JsonParseException, JsonMappingException, IOException {
        Map<String, String> hookEventPayload = Maps.newHashMap();
        hookEventPayload.put("eventType", "pull_request");
        hookEventPayload.put("providerType", "bitbucket");
        hookEventPayload.put("created", System.currentTimeMillis() + "");
        PullRequestPayload payload = mapper.readValue(readClasspathResource("/pr_opened.json", getClass()),
                PullRequestPayload.class);

        hookEventPayload.put("payload", mapper.writeValueAsString(payload));

        return HookPayloadEvent.build(hookEventPayload);
    }

    static String readClasspathResource(String path, Class<?> clazz) {
        try {
            return new String(Resources.asByteSource(new ClassPathResource(path, clazz).getURL()).read());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    static class TestingGithubWebhookHandler extends GithubWebhookPayloadHandler {

        public TestingGithubWebhookHandler(ProjectRepository projectRepository, BuildService buildService,
                BuildDefinitionFetcher fetcher) {
            super(projectRepository, buildService, fetcher);
        }

    }
}
