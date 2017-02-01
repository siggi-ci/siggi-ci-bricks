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
package org.siggici.data.ids.support;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siggici.data.ids.EncryptionKey;
import org.siggici.data.ids.EncryptionKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SupportApplicationTest {

    @Autowired
    private EncryptionKeyService encryptionKeyService;

    @Test
    public void contextLoads() {
        IntStream.range(1, 12).forEach(i -> {
            EncryptionKey k = encryptionKeyService.create();
            log.info("Created : {} : {}", k.getAsString(), k);
        });
    }

    @Configuration
    @ImportAutoConfiguration(EncryptionKeyServiceAutoConfiguration.class)
    static class TestConfig {
    }

}
