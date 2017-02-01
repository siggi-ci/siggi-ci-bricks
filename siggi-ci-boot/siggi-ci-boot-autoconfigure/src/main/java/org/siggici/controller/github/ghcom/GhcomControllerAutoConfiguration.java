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
package org.siggici.controller.github.ghcom;

import org.siggici.connect.github.ghcom.api.Ghcom;
import org.siggici.services.common.ScmProvider;
import org.siggici.services.github.GithubExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ GithubExecutor.class, Ghcom.class })
@ConditionalOnBean({ Ghcom.class, GithubExecutor.class })
public class GhcomControllerAutoConfiguration {

    @Bean
    public GhcomController ghcomController(Ghcom ghcom, GithubExecutor githubExecutor) {
        return new GhcomController(ghcom, githubExecutor);
    }

    @Bean
    public ScmProvider ghcomScmProvider() {
        ScmProvider p = new ScmProvider();
        p.setConnectUrl("/connect/ghcom");
        p.setId("ghcom");
        p.setName("Github.com");
        return p;
    }

}
