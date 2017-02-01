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

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode(of = { "key" })
public class ProjectEnvironmentSetting {

    private static final String MASK = "********";

    private String key;

    private boolean hidden = false;

    private String value;

    @SuppressWarnings("unused")
    private ProjectEnvironmentSetting() {
    }

    @Builder
    ProjectEnvironmentSetting(String key, String value, boolean hidden) {
        this.key = key;
        this.value = value;
        this.hidden = hidden;
    }

    public String getMasked() {
        if (hidden) {
            return MASK;
        } else {
            return value;
        }
    }
}
