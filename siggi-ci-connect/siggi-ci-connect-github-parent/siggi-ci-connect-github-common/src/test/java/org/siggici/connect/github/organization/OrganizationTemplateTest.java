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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.siggici.connect.github.impl.BaseGitHubApiTest;
import org.siggici.connect.github.user.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;

public class OrganizationTemplateTest extends BaseGitHubApiTest {

    protected OrganizationOperations organizationOperations;

    @Before
    public void buildOrganizationTemplate() {
        organizationOperations = new OrganizationTemplate(restTemplate, true);
    }

    @Test
    public void getMembership() {
        mockServer.expect(requestTo("https://api.github.com/teams/1/members/klaus")).andExpect(method(GET))
                .andRespond(MockRestResponseCreators.withNoContent());

        boolean result = organizationOperations.isTeamMember("1", "klaus");

        assertThat(result).isTrue();

    }

    @Test
    public void getPendingMembership() {
        mockServer.expect(requestTo("https://api.github.com/teams/1/members/klaus")).andExpect(method(GET))
                .andRespond(withSuccess(jsonResource("membershippending"), MediaType.APPLICATION_JSON));

        boolean result = organizationOperations.isTeamMember("1", "klaus");

        assertThat(result).isFalse();

    }

    // @Test
    // public void getPendingMembershipWhenTrue() {
    // mockServer.expect(requestTo("https://api.github.com/teams/1/members/klaus")).andExpect(method(GET)).andRespond(
    // withSuccess(jsonResource("membershippending"),
    // MediaType.APPLICATION_JSON));
    //
    // boolean result = organizationOperations.isTeamMember("1", "klaus", true);
    //
    // assertThat(result).isTrue();
    //
    // }

    @Test
    public void listTeams() {
        mockServer.expect(requestTo("https://api.github.com/orgs/sigmalab-projects/teams?per_page=100"))
                .andExpect(method(GET)).andRespond(withSuccess(jsonResource("teams"), MediaType.APPLICATION_JSON));

        List<Team> teams = organizationOperations.listTeams("sigmalab-projects");

        assertThat(teams).isNotEmpty();
        assertThat(teams).hasSize(1);

    }

    @Test
    public void getTeam() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

        Team team = organizationOperations.getTeam(1);

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isEqualTo(1);
        Assertions.assertThat(team.getName()).isEqualTo("Justice League");
    }

    @Test
    public void updateTeam() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.PATCH))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

        Team team = organizationOperations.updateTeam(1, new TeamRequest("Justice League"));

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isEqualTo(1);
        Assertions.assertThat(team.getName()).isEqualTo("Justice League");
    }

    @Test
    public void createTeam() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/teams"))
                .andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

        Team team = organizationOperations.createTeam("siggi-ci", new TeamRequest("Justice League"));

        Assertions.assertThat(team).isNotNull();
        Assertions.assertThat(team.getId()).isEqualTo(1);
        Assertions.assertThat(team.getName()).isEqualTo("Justice League");
    }

    @Test
    public void deleteTeam() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.DELETE))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withNoContent());

        organizationOperations.deleteTeam(1);

    }

    @Test
    public void listAllOrganizations() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/organizations?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

        List<GitHubOrganization> emailList = organizationOperations.listAllGithubOrganizations();

        Assertions.assertThat(emailList).isNotNull();
        Assertions.assertThat(emailList.size()).isEqualTo(1);
    }

    @Test
    public void listOrganizationsForCurrentUser() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/orgs?per_page=100")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

        List<GitHubOrganization> emailList = organizationOperations.listGithubOrganizations();

        Assertions.assertThat(emailList).isNotNull();
        Assertions.assertThat(emailList.size()).isEqualTo(1);
    }

    @Test
    public void listUserOrganizations() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/klaus/orgs?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

        List<GitHubOrganization> orgaList = organizationOperations.listUserOrganizations("klaus");

        Assertions.assertThat(orgaList).isNotNull();
        Assertions.assertThat(orgaList.size()).isEqualTo(1);
    }

    @Test
    public void getOrganization() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("getOrga.json", getClass()), MediaType.APPLICATION_JSON));

        ExtOrganization orga = organizationOperations.getOrganization("siggi-ci");

        Assertions.assertThat(orga).isNotNull();
        Assertions.assertThat(orga.getName()).isEqualTo("siggi-ci");
        Assertions.assertThat(orga.getLogin()).isEqualTo("siggi-ci");
    }

    @Test
    public void patchOrganization() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci")).andExpect(method(HttpMethod.PATCH))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("getOrga.json", getClass()), MediaType.APPLICATION_JSON));

        OrganizationUpdate update = new OrganizationUpdate();
        update.setBillingEmail("no-billing-for-open-source@gmail.com");
        ExtOrganization orga = organizationOperations.updateOrganization(update, "siggi-ci");

        Assertions.assertThat(orga).isNotNull();
        Assertions.assertThat(orga.getName()).isEqualTo("siggi-ci");
        Assertions.assertThat(orga.getLogin()).isEqualTo("siggi-ci");
    }
    
    
    @Test
    public void listMembers() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/members?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listMembers.json", getClass()), MediaType.APPLICATION_JSON));

        List<User> issueList = organizationOperations.listMembers("siggi-ci");

        Assertions.assertThat(issueList).isNotNull();
        Assertions.assertThat(issueList.size()).isEqualTo(1);
    }

    @Test
    public void listPublicMembers() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/public_members?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listMembers.json", getClass()), MediaType.APPLICATION_JSON));

        List<User> issueList = organizationOperations.listPublicMembers("siggi-ci");

        Assertions.assertThat(issueList).isNotNull();
        Assertions.assertThat(issueList.size()).isEqualTo(1);
    }

    @Test
    public void isMember() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/members/klaus"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withNoContent());

        boolean answer = organizationOperations.isMemberOfOrganization("siggi-ci", "klaus");

        Assertions.assertThat(answer).isTrue();
    }

    @Test
    public void isNotMember() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/members/klaus"))
                .andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withStatus(NOT_FOUND));

        boolean answer = organizationOperations.isMemberOfOrganization("siggi-ci", "klaus");

        Assertions.assertThat(answer).isFalse();
    }

    @Test
    public void removeMember() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/orgs/siggi-ci/members/klaus"))
                .andExpect(method(HttpMethod.DELETE))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withNoContent());

        organizationOperations.removeFromOrganization("siggi-ci", "klaus");
    }
}
