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
package org.siggici.webhooks.services.build;

import static java.lang.String.join;

import org.siggici.data.builds.Build;
import org.siggici.data.builds.BuildHook;
import org.siggici.data.builds.BuildRepository;
import org.siggici.data.jpa.RepoId;
import org.siggici.services.common.NumberGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BuildService {

    private final BuildRepository buildRepository;
    private final NumberGenerator counterService;
    private final ConversionService conversionService;

    public BuildService(BuildRepository buildRepository, NumberGenerator numberGenerator,
            @Qualifier("customConversionService") ConversionService customConversionService) {
        this.buildRepository = buildRepository;
        this.counterService = numberGenerator;
        this.conversionService = customConversionService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Build createBuild(CreateBuildRequest createBuildRequest) {
        final RepoId repoId = createBuildRequest.getRepoId();
        log.info("create build for : {}", repoId.toString());

        final BuildHook buildHook = conversionService.convert(createBuildRequest.getPullRequest(), BuildHook.class);
        buildHook.setRawHookPayload(createBuildRequest.getHookPayloadEvent().getRawPayload());

        final long nextId = counterService
                .nextId(join("/", "build", repoId.getProvider(), repoId.getOrga(), repoId.getRepo()));

        return buildRepository.save(new Build(repoId, nextId, buildHook));
    }

}
