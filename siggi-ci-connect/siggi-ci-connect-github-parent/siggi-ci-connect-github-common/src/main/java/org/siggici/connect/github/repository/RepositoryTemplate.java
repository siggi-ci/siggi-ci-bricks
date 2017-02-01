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

import static java.util.Arrays.asList;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.siggici.connect.github.AbstractGitHubOperations;
import org.siggici.connect.github.organization.GitHubRepo;
import org.siggici.connect.github.pagination.PagingIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 * Implementation for {@link RepositoryOperations}.
 *
 * @author jbellmann
 */
public class RepositoryTemplate extends AbstractGitHubOperations implements RepositoryOperations {

    private final Logger log = LoggerFactory.getLogger(RepositoryTemplate.class);

    private final ParameterizedTypeReference<List<Status>> statusListTypeRef = new ParameterizedTypeReference<List<Status>>() {
    };

    private final ParameterizedTypeReference<List<GitHubRepo>> repoListTypeRef = new ParameterizedTypeReference<List<GitHubRepo>>() {
    };

    public RepositoryTemplate(RestTemplate restTemplate, boolean authorized) {
        this(restTemplate, authorized, API_URL_BASE);
    }

    public RepositoryTemplate(RestTemplate restTemplate, boolean authorized, String apiBaseUrl) {
        super(restTemplate, authorized, apiBaseUrl);
    }

    public GitHubRepo getRepository(final String user, final String repo) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        return getRestTemplate().getForObject(buildRepoUri("", uriVariables), GitHubRepo.class);
    }

    public List<GitHubRepo> getRepositories(final String user) {
        // RequestEntity<?> requestEntity = new
        // RequestEntity<>(getDefaultHeaders(), HttpMethod.GET,
        // buildUri("user/repos?per_page=100"));
        // ResponseEntity<GitHubRepo[]> repositoriesEntity =
        // getRestTemplate().exchange(requestEntity, GitHubRepo[].class);

        List<GitHubRepo> result = new ArrayList<GitHubRepo>();
        Iterator<List<GitHubRepo>> iter = new PagingIterator<>(getRestTemplate(), buildUri("user/repos?per_page=100"),
                repoListTypeRef);
        while (iter.hasNext()) {
            result.addAll(iter.next());
        }
        // buildUri("user/repos?per_page=100"), HttpMethod.GET, entity,
        // GitHubRepo[].class,
        // new HashMap<String, Object>());

        // log.info("LINK_HEADER : {}",
        // repositoriesEntity.getHeaders().get("Link"));

        // return asList(repositoriesEntity.getBody());
        return result;
    }

    public List<GitHubDeployKey> getDeployKeys(final String user, final String repo) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        return asList(getRestTemplate().getForObject(buildRepoUri("/keys", uriVariables), GitHubDeployKey[].class));
    }

    public GitHubDeployKey getDeployKey(final String user, final String repo, final int id) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        uriVariables.put("id", id);
        return getRestTemplate().getForObject(buildRepoUri("/keys/{id}", uriVariables), GitHubDeployKey.class);
    }

    public GitHubDeployKey addDeployKey(final String owner, final String repo, final String title, final String key) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", owner);
        uriVariables.put("repo", repo);
        AddDeployKeyRequest request = new AddDeployKeyRequest(title, key);
        return getRestTemplate().postForObject(buildRepoUri("/keys", uriVariables), request, GitHubDeployKey.class);
    }

    public void deleteDeployKey(final String owner, final String repo, final int id) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", owner);
        uriVariables.put("repo", repo);
        uriVariables.put("id", id);
        getRestTemplate().delete(buildRepoUri("/keys/{id}"));
    }

    @Override
    public List<GitHubWebhook> getWebhooks(final String user, final String repo) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        return asList(getRestTemplate().getForObject(buildRepoUri("/hooks", uriVariables), GitHubWebhook[].class));
    }

    @Override
    public GitHubWebhook getWebhook(final String user, final String repo, final String id) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        uriVariables.put("id", id);
        return getRestTemplate().getForObject(buildRepoUri("/hooks/{id}", uriVariables), GitHubWebhook.class);
    }

    @Override
    public GitHubWebhook addWebhook(final String user, final String repo, final Object request) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        return getRestTemplate().postForObject(buildRepoUri("/hooks", uriVariables), request, GitHubWebhook.class);
    }

    @Override
    public void deleteWebhook(final String user, final String repo, final String id) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("user", user);
        uriVariables.put("repo", repo);
        uriVariables.put("id", id);
        getRestTemplate().delete(buildRepoUri("/hooks/{id}", uriVariables));
    }

    private String buildRepoUri(final String path) {
        return buildUri("repos/{user}/{repo}" + path).toString();
    }

    private String buildRepoUri(final String path, Map<String, Object> uriVariables) {
        return buildUri("repos/{user}/{repo}" + path, uriVariables).toString();
    }

    @Override
    public Status createStatus(String owner, String repository, String sha, StatusRequest body) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("owner", owner);
        uriVariables.put("repository", repository);
        uriVariables.put("sha", sha);

        URI uri = new UriTemplate(buildUriString("/repos/{owner}/{repository}/statuses/{sha}")).expand(uriVariables);
        RequestEntity<StatusRequest> entity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON)
                .body(body);

        ResponseEntity<Status> responseEntity = getRestTemplate().exchange(entity, Status.class);
        return responseEntity.getBody();
    }

    @Override
    public List<Status> listStatuses(String owner, String repository, String ref) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("owner", owner);
        uriVariables.put("repository", repository);
        uriVariables.put("ref", ref);

        return getRestTemplate().exchange(buildUri("/repos/{owner}/{repository}/commits/{ref}/statuses", uriVariables),
                HttpMethod.GET, null, statusListTypeRef).getBody();
    }

    /**
     * 'ref' can be SHA, branch or tag.
     */
    @Override
    public CombinedStatus getCombinedStatus(String owner, String repository, String ref) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("owner", owner);
        uriVariables.put("repository", repository);
        uriVariables.put("ref", ref);

        return getRestTemplate().exchange(buildUri("/repos/{owner}/{repository}/commits/{ref}/status", uriVariables),
                HttpMethod.GET, null, CombinedStatus.class).getBody();
    }

    protected HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");

        return headers;
    }
}
