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
package org.siggici.data.counter;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("counter")
public class CounterApplicationTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5532).build();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private CounterService service;

    @Test
    public void contextLoads() {
        long id1 = service.nextId("build/ghcom/orga1/repo3");
        long id2 = service.nextId("build/ghcom/orga1/repo3");
        long id3 = service.nextId("build/ghcom/orga1/repo3");
        long id4 = service.nextId("build/ghcom/orga1/repo4");

        Assertions.assertThat(id1).isGreaterThan(0).isLessThan(2);
        Assertions.assertThat(id2).isGreaterThan(1).isLessThan(3);
        Assertions.assertThat(id3).isGreaterThan(2).isLessThan(4);
        Assertions.assertThat(id4).isGreaterThan(0).isLessThan(2);
    }
}
