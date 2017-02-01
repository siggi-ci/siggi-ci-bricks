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
package org.siggici.keys.jpa.autoconfigure;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.siggici.keys.DefaultDeployKeyService;
import org.siggici.keys.DeployKeyService;
import org.siggici.keys.DeployKeyStore;
import org.siggici.keys.jpa.DeployKeyRepository;
import org.siggici.keys.jpa.JpaDeployKeyStore;
import org.siggici.keys.jpa.PrebuildDeployKeyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnClass({ DataSource.class, EntityManager.class })
@EnableConfigurationProperties({ DeployKeyServiceProperties.class })
@EnableJpaRepositories(basePackageClasses = { JpaDeployKeyStore.class })
@EntityScan(basePackageClasses = { JpaDeployKeyStore.class })
public class JpaDeployKeyStoreAutoConfiguration {

    @Configuration
    @ConditionalOnMissingBean({ DeployKeyStore.class })
    protected static class DeployKeyStoreConfig {

        @Bean
        public DeployKeyStore deployKeyStore(DeployKeyService deployKeyService, DeployKeyRepository repository) {
            return new JpaDeployKeyStore(deployKeyService, repository);
        }
    }

    @Configuration
    @ConditionalOnMissingBean({ DeployKeyService.class })
    protected static class DeployKeyServiceConfig {

        @Bean
        @ConditionalOnProperty(prefix = "siggi.deploykeys", name = { "enablePreprocessing" }, havingValue = "false", matchIfMissing=true)
        public DeployKeyService defaultDeployKeyService(DeployKeyServiceProperties props) {
            return new DefaultDeployKeyService(props.getType(), props.getSize(), props.getComment());
        }

        @Bean
        @ConditionalOnProperty(prefix = "siggi.deploykeys", name = { "enablePreprocessing" }, havingValue = "true")
        public DeployKeyService prebuildDeployKeyService(DeployKeyServiceProperties props) {
            DeployKeyService delegate = new DefaultDeployKeyService(props.getType(), props.getSize(),
                    props.getComment());
            return new PrebuildDeployKeyService(delegate, props);
        }
    }

}
