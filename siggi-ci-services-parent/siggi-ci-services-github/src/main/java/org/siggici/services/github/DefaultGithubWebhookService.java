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

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.siggici.connect.github.Github;
import org.siggici.connect.github.repository.AddWebhookRequest;
import org.siggici.connect.github.repository.GitHubWebhook;
import org.siggici.data.jpa.RepoId;
import org.siggici.services.common.RandomStringService;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DefaultGithubWebhookService implements GithubWebhookService {

    private final GithubWebhookServiceProperties properties;
    private final RandomStringService randomStringService = new RandomStringService();

    DefaultGithubWebhookService(GithubWebhookServiceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Optional<GitHubWebhook> registerWebhook(Github github, RepoId repoId) {
        List<GitHubWebhook> existingHooks = checkWebhookExists(github, repoId);
        if (!existingHooks.isEmpty()) {
            log.debug("{} registered Webhooks found for url : {}", existingHooks.size(), properties.getHookUrl());
            return Optional.empty();
        }

        AddWebhookRequest addWebhookRequest = new AddWebhookRequest();
        addWebhookRequest.setName(properties.getName());
        addWebhookRequest.setEvents(properties.getEvents().toArray(new String[properties.getEvents().size()]));
        addWebhookRequest.getConfig().setUrl(buildUrl());

        GitHubWebhook webhook = github.getRepositoryOperations().addWebhook(repoId.getOrga(), repoId.getRepo(),
                addWebhookRequest);
        return Optional.ofNullable(webhook);
    }

    protected List<GitHubWebhook> checkWebhookExists(Github github, RepoId repoId) {
        List<GitHubWebhook> allHooks = github.getRepositoryOperations().getWebhooks(repoId.getOrga(), repoId.getRepo());
        URI targetUri = URI.create(properties.getHookUrl());
        return allHooks.stream().filter(new SameWebhookPredicate(targetUri)).collect(Collectors.toList());
    }

    protected String buildUrl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getHookUrl());
        String userinfo = "";
        if (properties.isRandomUserinfoEnabled()) {
            userinfo = buildRandomUserinfo();
            return builder.userInfo(userinfo).build().toUriString();
        } else if (StringUtils.hasText(properties.getUsername())) {
            if (StringUtils.hasText(properties.getPassword())) {
                userinfo = properties.getUsername() + ":" + properties.getPassword();
            } else {
                userinfo = properties.getUsername();
            }
            return builder.userInfo(userinfo).build().toUriString();
        } else {
            return builder.build().toUriString();
        }
    }

    protected String buildRandomUserinfo() {
        return String.join(":", this.randomStringService.getRandomUsername(),
                this.randomStringService.getRandomPassword());
    }

    @Slf4j
    static class SameWebhookPredicate implements Predicate<GitHubWebhook> {

        private final URI targetUri;

        public SameWebhookPredicate(URI targetUri) {
            this.targetUri = targetUri;
        }

        @Override
        public boolean test(GitHubWebhook t) {
            try {
                URI hookUri = URI.create(t.getConfig().getUrl());
                if (targetUri.getHost().equals(hookUri.getHost()) && targetUri.getPath().equals(hookUri.getPath())
                        && targetUri.getScheme().equals(hookUri.getScheme())) {
                    return true;
                }
            } catch (Exception e) {
                log.error("Failed to test for existing webhooks, {}", t, e);
            }
            return false;
        }

    }

}
