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
package org.siggici.connect.github.ghe.config;

import org.siggici.connect.github.ghe.connect.GheConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;

public abstract class AbstractGheSocialConfigurer implements SocialConfigurer{

    private final Logger log = LoggerFactory.getLogger(AbstractGheSocialConfigurer.class);

    @Override
    public void addConnectionFactories(final ConnectionFactoryConfigurer connectionFactoryConfigurer,
                                       final Environment environment) {
        
        log.info("register GHE_CONNECTION_FACTORY ...");
        connectionFactoryConfigurer.addConnectionFactory(new GheConnectionFactory(getClientId(), getClientSecret(), getAuthorizeUrl(), getAccessTokenUrl(), getApiBaseUrl()));
    }

    @Override
    public UserIdSource getUserIdSource() {

        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(
            final ConnectionFactoryLocator connectionFactoryLocator) {

        // this is hacky, but didn't found out how to do these configuration without it
//        if (connectionFactoryLocator instanceof SocialAuthenticationServiceRegistry) {
//            log.debug("Initialize ConnectionFactory with key {} and secret {}",
//                    getClientId().substring(0, getClientIdSubstringLenght()),
//                    getClientSecret().substring(0, getClientSecretIdSubstringCount()));
//
//            SocialAuthenticationServiceRegistry registry = (SocialAuthenticationServiceRegistry)
//                    connectionFactoryLocator;
//            registry.addAuthenticationService(new GheAuthenticationService(getClientId(), getClientSecret(), getAuthorizeUrl(), getAccessTokenUrl(), getApiBaseUrl()));
//        }

        return doGetUsersConnectionRepository(connectionFactoryLocator);
    }

    protected abstract UsersConnectionRepository doGetUsersConnectionRepository(
            ConnectionFactoryLocator connectionFactoryLocator);

    protected abstract String getClientId();

    protected abstract String getClientSecret();

    protected int getClientIdSubstringLenght() {
        return 8;
    }

    protected int getClientSecretIdSubstringCount() {
        return 4;
    }
    
    protected abstract String getAuthorizeUrl();
    
    protected abstract String getAccessTokenUrl();
    
    protected abstract String getApiBaseUrl();
}
