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
package org.siggici.services.github;

import org.siggici.keys.DeployKeyStore;
import org.siggici.services.project.ProjectService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ GithubWebhookServiceProperties.class, GithubDeploykeyServiceProperties.class })
public class GithubServicesAutoconfiguration {

    @Configuration
    protected static class GithubDeployKeyServiceConfiguration {

        @Bean
        @ConditionalOnMissingBean({ GithubDeploykeyService.class })
        public GithubDeploykeyService defaultGithubDeployKeyService(GithubDeploykeyServiceProperties properties,
                DeployKeyStore deployKeyStore) {
            return new DefaultGithubDeploykeyService(properties, deployKeyStore);
        }
    }

    @Configuration
    protected static class GithubWebhookServiceConfiguration {

        @Bean
        @ConditionalOnMissingBean({ GithubWebhookService.class })
        public GithubWebhookService defaultGithubWebhookService(GithubWebhookServiceProperties properties) {
            return new DefaultGithubWebhookService(properties);
        }
    }

    @Configuration
    protected static class GithubRepositoryEnablerConfiguration {

        @Bean
        @ConditionalOnMissingBean({ GithubRepositoryEnabler.class })
        public GithubRepositoryEnabler defaultGithubRepositoryEnabler(DeployKeyStore deployKeyStore,
                ProjectService projectService, GithubWebhookService githubWebhookService,
                GithubDeploykeyService githubDeploykeyService) {

            return new DefaultGithubRepositoryEnabler(deployKeyStore, projectService, githubWebhookService,
                    githubDeploykeyService);
        }
    }
}
