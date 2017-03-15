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
package org.siggici.webhooks.services;

import org.siggici.data.builds.Build;
import org.siggici.webhooks.services.build.CreateBuildRequest;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BuildCreatedEvent {

    private final Build createdBuild;
    private final CreateBuildRequest req;

    @Builder
    private BuildCreatedEvent(Build createdBuild, CreateBuildRequest req) {
        this.createdBuild = createdBuild;
        this.req = req;
    }

}
