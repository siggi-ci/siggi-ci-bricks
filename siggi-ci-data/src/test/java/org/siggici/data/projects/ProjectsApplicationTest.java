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
package org.siggici.data.projects;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.siggici.data.ids.DeployKeyRef;
import org.siggici.data.ids.EncryptionKeyService;
import org.siggici.data.jpa.RepoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.stups.junit.postgres.PostgreSqlRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("projects")
public class ProjectsApplicationTest {

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5732).build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EncryptionKeyService encryptionKeyService;

    @Test
    public void contextLoads() throws KeyAlreadyExistsException {
        RepoId repoId = RepoId.builder().provider("ghcom").orga("orga1").repo("repo3").build();
        DeployKeyRef dkRef = DeployKeyRef.builder().deployKeyReference(UUID.randomUUID().toString()).build();
        Project p = Project.builder().repoId(repoId).deployKeyRef(dkRef).encryptionKey(encryptionKeyService.create())
                .build();
        p = projectRepository.save(p);
        Assertions.assertThat(p).isNotNull();
        Assertions.assertThat(p.getId()).isNotNull();
        p.addEnvironmentSetting(ProjectEnvironmentSetting.builder().key("USER").value("KARL").hidden(false).build());
        p.addEnvironmentSetting(ProjectEnvironmentSetting.builder().key("USER-2").value("KARL").hidden(true).build());
        p = projectRepository.save(p);

        Optional<Project> p2Optional = projectRepository.findByRepoId(repoId);
        Assertions.assertThat(p2Optional).isNotEmpty();
        Assertions.assertThat(p2Optional.get()).isEqualTo(p);

        // should throw an Exception
        thrown.expect(Exception.class);
        Project pex = Project.builder().repoId(repoId).deployKeyRef(dkRef).encryptionKey(encryptionKeyService.create())
                .build();
        projectRepository.save(pex);
    }
}
