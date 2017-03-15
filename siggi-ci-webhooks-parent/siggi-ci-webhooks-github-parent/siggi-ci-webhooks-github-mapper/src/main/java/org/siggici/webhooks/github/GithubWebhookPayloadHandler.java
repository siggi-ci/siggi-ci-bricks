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
package org.siggici.webhooks.github;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.siggici.data.builds.Build;
import org.siggici.data.jpa.RepoId;
import org.siggici.data.projects.Project;
import org.siggici.data.projects.ProjectRepository;
import org.siggici.webhooks.HookPayloadEvent;
import org.siggici.webhooks.HookPayloadEventHandler;
import org.siggici.webhooks.services.BuildCreatedEvent;
import org.siggici.webhooks.services.build.BuildDefinitionFetcher;
import org.siggici.webhooks.services.build.BuildService;
import org.siggici.webhooks.services.build.CreateBuildRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javaslang.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author jbellmann
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class GithubWebhookPayloadHandler implements HookPayloadEventHandler, ApplicationEventPublisherAware {

    private static final String GITHUB = "github";
    private static final Splitter SPLITTER = Splitter.on("/").omitEmptyStrings().trimResults();
    private static final String PULL_REQUEST = "pull_request";
    private static final List<String> ACTIONS_TO_PROCESS = Lists.newArrayList("opened", "synchronize");
    private final ObjectMapper om = new ObjectMapper();

    private ApplicationEventPublisher applicationEventPublisher;

    private final ProjectRepository projectRepository;
    private final BuildService buildService;
    private final BuildDefinitionFetcher buildDefinitionFetcher;

    @Override
    public boolean supports(HookPayloadEvent hookPayloadEvent) {
        return (GITHUB.equalsIgnoreCase(hookPayloadEvent.getProviderType())
                && PULL_REQUEST.equalsIgnoreCase(hookPayloadEvent.getEventType()));
    }

    public void handle(HookPayloadEvent hookPayloadEvent) {
        log.debug("entering payload-Handler ...");
        PullRequestPayload pullRequestPayload = map(hookPayloadEvent.getRawPayload());
        if (!ACTIONS_TO_PROCESS.contains(pullRequestPayload.getAction())) {
            log.info("skip processing, PR-ACTION is : {}", pullRequestPayload.getAction());
            return;
        }
        RepoId repoId = extractRepoId(pullRequestPayload);

        Optional<Project> optionalProject = projectRepository.findByRepoId(repoId);
        if (!optionalProject.isPresent()) {
            log.info("skip processing, no project found with id : {}", repoId.toString());
            return;
        }

        Tuple2<Optional<String>, Optional<?>> buildDefinition = buildDefinitionFetcher.fetch(
                pullRequestPayload.getRepository().getUrl(), optionalProject.get().getAccessToken(),
                pullRequestPayload.getPullRequest().getHead().getSha());

        CreateBuildRequest req = CreateBuildRequest.builder().repoId(repoId).hookPayloadEvent(hookPayloadEvent)
                .pullRequest(pullRequestPayload).rawBuildDefinition(buildDefinition._1)
                .parsedBuildDefinition(buildDefinition._2).build();

        Build createdBuild = buildService.createBuild(req);
        log.debug("payload-handler done");
        applicationEventPublisher.publishEvent(BuildCreatedEvent.builder().createdBuild(createdBuild).req(req).build());
    }

    private RepoId extractRepoId(PullRequestPayload payload) {
        Iterable<String> splittedFullname = SPLITTER.split(payload.getRepository().getFullName());
        try {
            String host = new URL(payload.getRepository().getHtmlUrl()).getHost();
            RepoId.RepoIdBuilder builder = RepoId.builder();
            if ("github.com".equals(host)) {
                builder.provider("ghcom");
            } else {
                builder.provider("ghe");
            }
            return builder.orga(Iterables.getFirst(splittedFullname, null))
                    .repo(Iterables.getLast(splittedFullname, null)).build();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    protected PullRequestPayload map(String data) {
        try {
            return om.readValue(data, PullRequestPayload.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @PostConstruct
    public void init() {
        log.info("GITHUB_HANDLER WAS INITIALIZED ...");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
