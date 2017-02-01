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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.siggici.connect.github.Github;
import org.siggici.connect.github.organization.GitHubOrganization;
import org.siggici.connect.github.organization.GitHubRepo;
import org.siggici.services.common.Organization;
import org.siggici.services.common.RepositoryInfo;
import org.siggici.services.common.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GithubExecutor {

    private final GithubRepositoryEnabler githubRepositoryEnabler;

    public GithubExecutor(GithubRepositoryEnabler githubRepositoryEnabler) {
        this.githubRepositoryEnabler = githubRepositoryEnabler;
    }

    public User getUser(Github github) {
        return new User(github.getUserOperations().getProfileId());
    }

    public List<Organization> getOrganizations(Github github) {
        List<GitHubOrganization> orgas = github.getOrganizationOperations().listGithubOrganizations();
        List<Organization> result = new ArrayList<>();
        orgas.forEach(orga -> {
            result.add(new Organization(orga.getLogin()));
        });
        return result;
    }

    public List<RepositoryInfo> getRepositories(Github github, String username) {
        List<GitHubRepo> repos = github.getRepositoryOperations().getRepositories(username);
        return repos.stream().filter(filterForUser(username)).map(toRepoInfo(username, github.getProviderId()))
                .collect(toList());
    }

    public List<RepositoryInfo> getOrganizationRepositories(Github github, String orga) {
        // 'type' not used here
        List<GitHubRepo> repos = github.getOrganizationOperations().listOrganizationRepositories(orga, "all");

        List<RepositoryInfo> result = new ArrayList<>();
        repos.forEach(repo -> {
            result.add(new RepositoryInfo(false, github.getProviderId(), orga, repo.getName(), repo.isPrivateRepo(),
                    repo.getHtmlUrl()));
        });
        return result;
    }

    public RepositoryInfo enableRepository(Github github, RepositoryInfo repositoryInfo) {
        log.info("enable repository : {}", repositoryInfo);

        EnableRequest request = EnableRequest.builder().github(github).repositoryInfo(repositoryInfo)
                .userInfo(new Object()).build();

        this.githubRepositoryEnabler.enableGithubRepository(request);

        return new RepositoryInfo(true, github.getProviderId(), repositoryInfo.getOrga(), repositoryInfo.getName(),
                repositoryInfo.isPrivateRepo(), repositoryInfo.getHtmlUrl());
    }

    public RepositoryInfo disableRepository(Github github, RepositoryInfo repositoryInfo) {
        log.info("disable repository : {}", repositoryInfo);
        repositoryInfo.setEnabled(false);
        return repositoryInfo;
    }

    public static Predicate<GitHubRepo> filterForUser(String username) {
        return new Predicate<GitHubRepo>() {
            @Override
            public boolean test(GitHubRepo t) {
                return t.getFullName().startsWith(username + "/");
            }
        };
    }

    public static Function<GitHubRepo, RepositoryInfo> toRepoInfo(String orga, String provider) {
        return new Function<GitHubRepo, RepositoryInfo>() {
            @Override
            public RepositoryInfo apply(GitHubRepo repo) {
                return new RepositoryInfo(false, provider, orga, repo.getName(), repo.isPrivateRepo(),
                        repo.getHtmlUrl());
            }
        };
    }
}
