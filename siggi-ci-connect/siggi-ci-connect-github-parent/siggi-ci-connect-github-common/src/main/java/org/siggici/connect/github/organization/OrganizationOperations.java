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

import java.util.List;

import org.siggici.connect.github.user.User;

/**
 * @author jbellmann
 */
public interface OrganizationOperations {

    ExtOrganization getOrganization(String organization);

    ExtOrganization updateOrganization(OrganizationUpdate update, String organization);

    List<GitHubOrganization> listAllGithubOrganizations();

    List<GitHubOrganization> listGithubOrganizations();

    List<GitHubOrganization> listUserOrganizations(String username);

    List<GitHubRepo> listOrganizationRepositories(String organization, String type);

    // https://developer.github.com/v3/orgs/teams/#get-team-membership

    boolean isTeamMember(String teamId, String username);

    boolean isTeamMember(String teamId, String username, boolean includePending);

    List<Team> listTeams(String organization);

    Team getTeam(String teamId);

    Team getTeam(long teamId);

    Team createTeam(String organization, TeamRequest teamRequest);

    Team updateTeam(long teamId, TeamRequest teamRequest);

    void deleteTeam(long teamId);

    List<User> listMembers(String organization);

    List<User> listPublicMembers(String organization);

    boolean isMemberOfOrganization(String organization, String username);

    boolean isPublicMemberOfOrganization(String organization, String username);

    void removeFromOrganization(String organization, String username);

}
