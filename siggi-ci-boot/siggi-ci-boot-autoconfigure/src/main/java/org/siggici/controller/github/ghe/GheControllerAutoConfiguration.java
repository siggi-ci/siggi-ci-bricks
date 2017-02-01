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
package org.siggici.controller.github.ghe;

import org.siggici.connect.github.ghe.api.Ghe;
import org.siggici.services.common.ScmProvider;
import org.siggici.services.github.GithubExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({GithubExecutor.class, Ghe.class})
@ConditionalOnBean({ Ghe.class, GithubExecutor.class })
public class GheControllerAutoConfiguration {

    @Bean
    public GheController gheController(Ghe ghe, GithubExecutor githubExecutor) {
        return new GheController(ghe, githubExecutor);
    }

    @Bean
    public ScmProvider gheScmProvider() {
        ScmProvider p = new ScmProvider();
        p.setConnectUrl("/connect/ghe");
        p.setId("ghe");
        p.setName("Github Enterprise");
        return p;
    }

}
