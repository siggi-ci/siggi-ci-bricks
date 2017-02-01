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
package org.siggici.controller.common;

import org.siggici.controller.common.connect.ConnectControllerProperties;
import org.siggici.controller.common.connect.SiggiConnectController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnClass({SiggiConnectController.class, ConnectController.class})
@ConditionalOnWebApplication
@AutoConfigureBefore({ SocialWebAutoConfiguration.class })
@EnableConfigurationProperties({ConnectControllerProperties.class})
public class SiggiConnectControllerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({ConnectController.class})
    public ConnectController siggiConnectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository, ConnectControllerProperties connectControllerProperties) {
        log.info("Create Siggi-Connect-Controller ...");
        return new SiggiConnectController(connectionFactoryLocator, connectionRepository, connectControllerProperties);
    }

}
