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
package org.siggici.connect.github.organization;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.siggici.connect.github.AbstractGitHubOperations;
import org.siggici.connect.github.pagination.PagingIterator;
import org.siggici.connect.github.user.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author jbellmann
 */
public class OrganizationTemplate extends AbstractGitHubOperations implements OrganizationOperations {

    private final ParameterizedTypeReference<List<User>> userListTypeRef = new ParameterizedTypeReference<List<User>>() {
    };

    private final ParameterizedTypeReference<List<GitHubOrganization>> orgaListTypeRef = new ParameterizedTypeReference<List<GitHubOrganization>>() {
    };

    private final ParameterizedTypeReference<List<Team>> teamListTypeRef = new ParameterizedTypeReference<List<Team>>() {
    };
    
    public OrganizationTemplate(RestTemplate restTemplate, boolean authorized) {
        this(restTemplate, authorized, API_URL_BASE);
    }

    public OrganizationTemplate(RestTemplate restTemplate, boolean authorized, String apiBaseUrl) {
        super(restTemplate, authorized, apiBaseUrl);
    }

    @Override
    public List<GitHubRepo> listOrganizationRepositories(final String organization, final String type) {
        Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("organization", organization);

        RequestEntity<?> requestEntity = new RequestEntity<>(getDefaultHeaders(), HttpMethod.GET, buildUri("orgs/{organization}/repos?per_page=100", uriVariables));
        ResponseEntity<GitHubRepo[]> organizationsEntity = getRestTemplate().exchange(requestEntity, GitHubRepo[].class);

//                buildUri("orgs/{organization}/repos?per_page=100"), HttpMethod.GET, entity, GitHubRepo[].class,
//                uriVariables);

        return asList(organizationsEntity.getBody());
    }

    @Override
    public boolean isTeamMember(String teamId, String username) {
        return isTeamMember(teamId, username, false);
    }

    @Override
    public boolean isTeamMember(String teamId, String username, boolean includePending) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", teamId);
        uriVariables.put("username", username);

        RequestEntity<?> requestEntity = new RequestEntity<>(getDefaultHeaders(), HttpMethod.GET, buildUri("teams/{id}/members/{username}", uriVariables));
        ResponseEntity<?> responseEntity = getRestTemplate().exchange(requestEntity, Object.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            return true;
        }
        return false;
        // } else if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // Membership membership = responseEntity.getBody();
        // if ("active".equals(membership.getState())) {
        // return true;
        // }
        // if ("pending".equals(membership.getState()) && includePending) {
        // return true;
        // } else {
        // return false;
        // }
        // }
    }

//    @Override
//    public List<Team> listTeams(String organization) {
//        HttpEntity<?> entity = new HttpEntity<>(getDefaultHeaders());
//
//        Map<String, String> uriVariables = new HashMap<String, String>();
//        uriVariables.put("organization", organization);
//
//        ResponseEntity<Team[]> organizationsEntity = getRestTemplate().exchange(
//                buildUri("orgs/{organization}/teams?per_page=100"), HttpMethod.GET, entity, Team[].class, uriVariables);
//
//        return asList(organizationsEntity.getBody());
//    }
    
    @Override
    public List<Team> listTeams(String organization) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);

        List<Team> teamList = new ArrayList<Team>();
        Iterator<List<Team>> iter = new PagingIterator<>(getRestTemplate(),
                buildUri("/orgs/{organization}/teams?per_page=100", uriVariables), teamListTypeRef);
        while (iter.hasNext()) {
            teamList.addAll(iter.next());
        }
        return teamList;
    }

    @Override
    public Team getTeam(String teamId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("teamId", teamId);

        RequestEntity<?> requestEntity = new RequestEntity<>(getDefaultHeaders(), HttpMethod.GET, buildUri("teams/{teamId}", uriVariables));
        ResponseEntity<Team> organizationsEntity = getRestTemplate().exchange(requestEntity, Team.class);

        return organizationsEntity.getBody();
    }

    @Override
    public List<User> listMembers(String organization) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);

        return fetchUsers("/orgs/{organization}/members?per_page=100", uriVariables);
    }

    @Override
    public List<User> listPublicMembers(String organization) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);

        return fetchUsers("/orgs/{organization}/public_members?per_page=100", uriVariables);
    }

    protected List<User> fetchUsers(String path, Map<String, Object> uriVariables) {
        List<User> usersList = new ArrayList<User>();
        Iterator<List<User>> iter = new PagingIterator<>(getRestTemplate(), buildUri(path, uriVariables),
                userListTypeRef);
        while (iter.hasNext()) {
            usersList.addAll(iter.next());
        }
        return usersList;
    }

    @Override
    public boolean isMemberOfOrganization(String organization, String username) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);
        uriVariables.put("username", username);

        HttpStatus status = HttpStatus.NOT_FOUND;
        try {

            ResponseEntity<Void> responseEntity = getRestTemplate()
                    .getForEntity(buildUri("/orgs/{organization}/members/{username}", uriVariables), Void.class);
            status = responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) {
            // skip
        }
        return HttpStatus.NO_CONTENT.equals(status) ? true : false;
    }

    @Override
    public boolean isPublicMemberOfOrganization(String organization, String username) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);
        uriVariables.put("username", username);
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {

            ResponseEntity<Void> responseEntity = getRestTemplate()
                    .getForEntity(buildUri("/orgs/{organization}/public_members/{username}", uriVariables), Void.class);
            status = responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) {
            // skip
        }
        return HttpStatus.NO_CONTENT.equals(status) ? true : false;
    }

    @Override
    public void removeFromOrganization(String organization, String username) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);
        uriVariables.put("username", username);
        getRestTemplate().delete(buildUri("/orgs/{organization}/members/{username}", uriVariables));
    }

//    @Override
//    public List<GitHubOrganization> listGithubOrganizations() {
//        RequestEntity<?> requestEntity = new RequestEntity<>(getDefaultHeaders(), HttpMethod.GET, buildUri("user/orgs?per_page=100"));
//        ResponseEntity<GitHubOrganization[]> organizationsEntity = getRestTemplate().exchange(requestEntity, GitHubOrganization[].class);
//        return Arrays.asList(organizationsEntity.getBody());
//    }

    @Override
    public List<GitHubOrganization> listAllGithubOrganizations() {
        return listOrganizations("/organizations?per_page=100");
    }

    @Override
    public List<GitHubOrganization> listGithubOrganizations() {
        return listOrganizations("/user/orgs?per_page=100");
    }

    @Override
    public List<GitHubOrganization> listUserOrganizations(String username) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("username", username);

        return listOrganizations("/user/{username}/orgs?per_page=100", uriVariables);
    }

    protected List<GitHubOrganization> listOrganizations(String path) {
        return listOrganizations(path, Collections.emptyMap());
    }

    protected List<GitHubOrganization> listOrganizations(String path, Map<String, Object> uriVariables) {
        List<GitHubOrganization> result = new ArrayList<GitHubOrganization>();
        Iterator<List<GitHubOrganization>> iter = new PagingIterator<>(getRestTemplate(), buildUri(path, uriVariables),
                orgaListTypeRef);
        while (iter.hasNext()) {
            result.addAll(iter.next());
        }
        return result;
    }

    @Override
    public ExtOrganization getOrganization(String organization) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);
        return getRestTemplate().getForObject(buildUri("/orgs/{organization}", uriVariables), ExtOrganization.class);
    }

    @Override
    public ExtOrganization updateOrganization(OrganizationUpdate update, String organization) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);

        RequestEntity<OrganizationUpdate> entity = RequestEntity.patch(buildUri("/orgs/{organization}", uriVariables))
                .contentType(MediaType.APPLICATION_JSON).body(update);

        return getRestTemplate().exchange(entity, ExtOrganization.class).getBody();
    }

    @Override
    public Team getTeam(long teamId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("teamid", teamId);
        return getRestTemplate().getForObject(buildUri("/teams/{teamid}", uriVariables), Team.class);
    }

    @Override
    public Team createTeam(String organization, TeamRequest teamRequest) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("organization", organization);

        RequestEntity<TeamRequest> entity = RequestEntity.post(buildUri("/orgs/{organization}/teams", uriVariables))
                .contentType(MediaType.APPLICATION_JSON).body(teamRequest);

        return getRestTemplate().exchange(entity, Team.class).getBody();
    }

    @Override
    public Team updateTeam(long teamId, TeamRequest teamRequest) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("teamid", teamId);

        RequestEntity<TeamRequest> entity = RequestEntity.patch(buildUri("/teams/{teamid}", uriVariables))
                .contentType(MediaType.APPLICATION_JSON).body(teamRequest);

        return getRestTemplate().exchange(entity, Team.class).getBody();
    }

    @Override
    public void deleteTeam(long teamId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("teamid", teamId);
        getRestTemplate().delete(buildUri("/teams/{teamid}", uriVariables));
    }

    protected HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");

        return headers;
    }

}
