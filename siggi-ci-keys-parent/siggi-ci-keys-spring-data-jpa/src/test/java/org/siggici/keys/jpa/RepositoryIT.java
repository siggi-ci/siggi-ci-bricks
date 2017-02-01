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
package org.siggici.keys.jpa;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siggici.keys.DefaultDeployKeyService;
import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

import com.jcraft.jsch.KeyPair;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryIT {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5434).build();

    @Autowired
    private DeployKeyStore deployKeyStore;

    @Test
    public void createStore() {
        String dkId = this.deployKeyStore.create();
        Optional<DeployKey> dk = this.deployKeyStore.byId(dkId, false);
        Assertions.assertThat(dk.get().getId()).isEqualTo(dkId);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public DeployKeyStore deployKeyStore(DeployKeyRepository repository) {
            return new JpaDeployKeyStore(new DefaultDeployKeyService(KeyPair.RSA, 2048, "test"), repository);
        }
    }

}
