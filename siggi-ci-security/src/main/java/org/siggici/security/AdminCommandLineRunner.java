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
package org.siggici.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

/**
 * Verifies the Accounts-Admin-User exists. If not it will be created.
 * 
 * @author jbellmann
 *
 */
@Component
public class AdminCommandLineRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(AdminCommandLineRunner.class);

    private final UserDetailsManager userDetailsManager;
    private final AccountsAdminProperties properties;

    public AdminCommandLineRunner(UserDetailsManager userDetailsManager, AccountsAdminProperties properties) {
        this.userDetailsManager = userDetailsManager;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userDetailsManager.userExists(properties.getUsername())) {
            logger.info("Create user with username : {} and role : {}", properties.getUsername(), properties.getRole());
            userDetailsManager.createUser(new User(properties.getUsername(), properties.getPassword(),
                    createAuthorityList(properties.getRole())));
            logger.info("user created");
        }
    }

}
