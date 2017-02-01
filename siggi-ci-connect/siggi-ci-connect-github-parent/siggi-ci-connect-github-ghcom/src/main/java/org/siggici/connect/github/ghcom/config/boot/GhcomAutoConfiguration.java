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
package org.siggici.connect.github.ghcom.config.boot;

import javax.sql.DataSource;

import org.siggici.connect.github.ghcom.api.Ghcom;
import org.siggici.connect.github.ghcom.config.AbstractGhcomSocialConfigurer;
import org.siggici.connect.github.ghcom.config.GhcomUsersConnectionRepositoryConfigurer;
import org.siggici.connect.github.ghcom.connect.GhcomConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, GhcomConnectionFactory.class })
@ConditionalOnProperty(prefix = "siggi.connect.ghcom.oauth2", name = "clientId")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
public class GhcomAutoConfiguration {

    @Configuration
    @EnableSocial
    @EnableConfigurationProperties(GhcomProperties.class)
    @ConditionalOnWebApplication
    protected static class GhcomConfigurerAdapter extends AbstractGhcomSocialConfigurer {

        @Autowired
        private GhcomProperties ghcomProperties;

        @Autowired
        private GhcomUsersConnectionRepositoryConfigurer ghcomUsersConnectionRepositoryConfigurer;

        @Bean
        @ConditionalOnMissingBean
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        public Ghcom ghcom(ConnectionRepository repository) {
            Connection<Ghcom> connection = repository.findPrimaryConnection(Ghcom.class);
            return connection != null ? connection.getApi() : null;
        }

        @Bean(name = { "connect/ghcomConnect", "connect/ghcomConnected" })
        @ConditionalOnProperty(prefix = "spring.social", name = "auto-connection-views")
        public GenericConnectionStatusView ghcomConnectView() {
            return new GenericConnectionStatusView("ghcom", "Ghcom");
        }

        @Override
        protected UsersConnectionRepository doGetUsersConnectionRepository(
                ConnectionFactoryLocator connectionFactoryLocator) {
            return ghcomUsersConnectionRepositoryConfigurer
                    .configureUsersConnectionRepository(connectionFactoryLocator);
        }

        @Override
        protected String getClientId() {
            return ghcomProperties.getClientId();
        }

        @Override
        protected String getClientSecret() {
            return ghcomProperties.getClientSecret();
        }

    }

    @Slf4j
    @Configuration
    @ConditionalOnMissingBean({GhcomUsersConnectionRepositoryConfigurer.class})
    protected static class GhcomUsersConnectionRepositoryConfigurerAutoConfiguration {

        @Autowired
        private DataSource dataSource;

        @Bean
        public GhcomUsersConnectionRepositoryConfigurer ghcomUsersConnectionRepositoryConfigurer() {
            log.info("GHCOM_CONFIGURER WILL BE CREATED :::");
            return new GhcomJdbcUsersConnectionRepositoryConfigurer(dataSource);
        }
    }

}
