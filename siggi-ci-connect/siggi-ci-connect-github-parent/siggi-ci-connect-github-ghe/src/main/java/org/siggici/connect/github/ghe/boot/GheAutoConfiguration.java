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
package org.siggici.connect.github.ghe.boot;

import javax.sql.DataSource;

import org.siggici.connect.github.ghe.api.Ghe;
import org.siggici.connect.github.ghe.config.AbstractGheSocialConfigurer;
import org.siggici.connect.github.ghe.config.GheUsersConnectionRepositoryConfigurer;
import org.siggici.connect.github.ghe.connect.GheConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, GheConnectionFactory.class })
@ConditionalOnProperty(prefix = "siggi.connect.ghe.oauth2", name = "clientId")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
// @AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class GheAutoConfiguration {

    @Configuration
    @EnableSocial
    @EnableConfigurationProperties(GheProperties.class)
    @ConditionalOnWebApplication
    protected static class GheConfigurerAdapter extends AbstractGheSocialConfigurer {

        @Autowired
        private GheProperties gheProperties;

        @Autowired
        private GheUsersConnectionRepositoryConfigurer gheUsersConnectionRepositoryConfigurer;

        @Bean
        @ConditionalOnMissingBean
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        public Ghe ghe(ConnectionRepository repository) {
            Connection<Ghe> connection = repository.findPrimaryConnection(Ghe.class);
            return connection != null ? connection.getApi() : null;
        }

        @Bean(name = { "connect/gheConnect", "connect/gheConnected" })
        @ConditionalOnProperty(prefix = "spring.social", name = "auto-connection-views")
        public GenericConnectionStatusView gheConnectView() {
            return new GenericConnectionStatusView("ghe", "Github-Enterprise");
        }

        @Override
        protected UsersConnectionRepository doGetUsersConnectionRepository(
                ConnectionFactoryLocator connectionFactoryLocator) {
            return gheUsersConnectionRepositoryConfigurer.configureUsersConnectionRepository(connectionFactoryLocator);
        }

        @Override
        protected String getClientId() {
            return gheProperties.getClientId();
        }

        @Override
        protected String getClientSecret() {
            return gheProperties.getClientSecret();
        }

        @Override
        protected String getAuthorizeUrl() {
            return gheProperties.getAuthorizeUrl();
        }

        @Override
        protected String getAccessTokenUrl() {
            return gheProperties.getAccessTokenUrl();
        }

        @Override
        protected String getApiBaseUrl() {
            return gheProperties.getApiBaseUrl();
        }
    }

    @Configuration
    @ConditionalOnMissingBean({ GheUsersConnectionRepositoryConfigurer.class })
    protected static class GheUsersConnectionRepositoryConfigurerAutoConfiguration {

        private final Logger logger = LoggerFactory
                .getLogger(GheUsersConnectionRepositoryConfigurerAutoConfiguration.class);

        @Autowired
        private DataSource dataSource;

        @Bean
        public GheUsersConnectionRepositoryConfigurer gheUsersConnectionRepositoryConfigurer() {
            logger.warn("GHE_CONFIGURER WILL BE CREATED :::");
            return new GheJdbcUsersConnectionRepositoryConfigurer(dataSource);
        }
    }

    // TOOD, I only got it working with JSR-310, not with springs validation-mechanism
//    @Bean
//    public static GhePropertiesValidator ghePropertiesValidator() {
//        return new GhePropertiesValidator();
//    }

}
