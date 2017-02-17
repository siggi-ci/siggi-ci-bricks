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

import java.util.Optional;

import org.siggici.data.jpa.RepoId;
import org.siggici.webhooks.HookPayloadEvent;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateBuildRequest {

    private final RepoId repoId;
    private final HookPayloadEvent hookPayloadEvent;
    private final Object pullRequest;
    private final Optional<String> rawBuildDefinition;
    private final Optional<?> parsedBuildDefinition;

    @Builder
    private CreateBuildRequest(RepoId repoId, HookPayloadEvent hookPayloadEvent, Object pullRequest,
            Optional<String> rawBuildDefinition, Optional<?> parsedBuildDefinition) {
        this.repoId = repoId;
        this.hookPayloadEvent = hookPayloadEvent;
        this.pullRequest = pullRequest;
        this.rawBuildDefinition = rawBuildDefinition;
        this.parsedBuildDefinition = parsedBuildDefinition;
    }
}
