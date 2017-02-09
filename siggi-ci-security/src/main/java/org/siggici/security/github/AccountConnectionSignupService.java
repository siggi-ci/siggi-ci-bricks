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
package org.siggici.security.github;

import java.util.Arrays;
import java.util.List;

import org.siggici.connect.github.Github;
import org.siggici.security.AccountConnectionSignupProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.util.Assert;

public class AccountConnectionSignupService implements ConnectionSignUp {

    private final Logger logger = LoggerFactory.getLogger(AccountConnectionSignupService.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String ROLE_USER = "ROLE_USER";

    private final Logger LOG = org.slf4j.LoggerFactory.getLogger(AccountConnectionSignupService.class);

    private final UserDetailsManager userDetailsManager;
    private final AccountConnectionSignupProperties configProperties;

    public AccountConnectionSignupService(UserDetailsManager userDetailsManager,
            AccountConnectionSignupProperties accountConnectionSignupProperties) {
        this.userDetailsManager = userDetailsManager;
        this.configProperties = accountConnectionSignupProperties;
    }

    @Override
    public String execute(Connection<?> connection) {
        final Object api = connection.getApi();
        Assert.notNull(api, "'api' should never be null");
        if (api instanceof Github) {
            Github github = (Github) api;
            String login = github.getUserOperations().getUserProfile().getLogin();
            LOG.info("Got from API : {}", login);
            if (configProperties.getAdmins().contains(login)) {
                registerUser(login, Arrays.asList(ROLE_ADMIN));
                return login;
            } else if (configProperties.getUsers().contains(login)) {
                registerUser(login, Arrays.asList(ROLE_USER));
                return login;
            } else if (isTeamMember(login, github)) {
                registerUser(login, Arrays.asList(ROLE_USER));
                return login;
            } else if (configProperties.isAllowAll() ){
                registerUser(login, Arrays.asList(ROLE_USER));
                return login;
            }
        } else {
            logger.warn("Expected connection of type 'github', but got another : {}", connection.getKey().getProviderId());
        }
        return null;
    }

    protected void registerUser(String login, List<String> authorities) {
        User user = new User(login, "NOT_NEEDED",
                AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
        userDetailsManager.createUser(user);
    }

    protected boolean isTeamMember(String login, Github github) {
        final String teamId = configProperties.getTeamId() + "";
        boolean isMember = github.getOrganizationOperations().isTeamMember(teamId, login);
        LOG.info("Is member of " + teamId + " : " + isMember);
        return isMember;
    }
}
