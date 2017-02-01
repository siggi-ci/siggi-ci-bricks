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
package org.siggici.security.autoconfiguration;

import javax.sql.DataSource;

import org.siggici.security.AccountConnectionSignupProperties;
import org.siggici.security.AccountsAdminProperties;
import org.siggici.security.AdminCommandLineRunner;
import org.siggici.security.SimpleSocialUserDetailsService;
import org.siggici.security.github.AccountConnectionSignupService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.social.security.SocialUserDetailsService;

@Configuration
@EnableConfigurationProperties({ AccountsAdminProperties.class })
public class SiggiCiSecurityAutoConfiguration {

    @Configuration
    @ConditionalOnMissingBean({ UserDetailsManager.class })
    public static class UserDetailsManagerAutoConfiguration {

        @Bean
        public UserDetailsManager userDetailsManager(DataSource dataSource) {
            JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
            manager.setDataSource(dataSource);
            return manager;
        }

        @Bean
        public SocialUserDetailsService socialUserDetailsService(UserDetailsService userDetailsService) {
            return new SimpleSocialUserDetailsService(userDetailsService);
        }
    }

    @Configuration
    @ConditionalOnBean({ UserDetailsManager.class })
    @AutoConfigureAfter({ UserDetailsManagerAutoConfiguration.class })
    public static class AccountConnectionSignupAutoConfiguration {

        @Bean
        public AccountConnectionSignupService accountConnectionSignupService(UserDetailsManager userDetailsManager,
                AccountConnectionSignupProperties accountConnectionSignupProperties) {

            return new AccountConnectionSignupService(userDetailsManager, accountConnectionSignupProperties);
        }

        @Bean
        public AdminCommandLineRunner adminCommandLineRunner(UserDetailsManager userDetailsManager,
                AccountsAdminProperties properties) {
            return new AdminCommandLineRunner(userDetailsManager, properties);
        }
    }

}
