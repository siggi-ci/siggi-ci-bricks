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

import org.siggici.data.ids.EncryptionKeyService;
import org.siggici.data.projects.ProjectRepository;
import org.siggici.keys.DeployKeyStore;
import org.siggici.services.project.ProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubApplicationEnablerConfig {

    @Bean
    public ProjectService projectService(DeployKeyStore deployKeyStore, EncryptionKeyService encryptionKeyService,
            ProjectRepository projectRepository) {

        return new ProjectService(encryptionKeyService, deployKeyStore, projectRepository);
    }

}
