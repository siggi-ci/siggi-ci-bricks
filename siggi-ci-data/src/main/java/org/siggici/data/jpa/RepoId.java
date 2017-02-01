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
package org.siggici.data.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This {@link Embeddable} reflects coordinates needed to identify a repository
 * in the system.
 * 
 * @author jbellmann
 *
 */
@Embeddable
@Getter
@ToString
@EqualsAndHashCode
public class RepoId {

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String orga;

    @Column(nullable = false)
    private String repo;

    private RepoId() {
    }

    @Builder
    private RepoId(String provider, String orga, String repo) {
        Assert.hasText(provider, "'provider' should never be null or empty");
        Assert.hasText(orga, "'orga' should never be null or empty");
        Assert.hasText(repo, "'repo' should never be null or empty");
        this.provider = provider;
        this.orga = orga;
        this.repo = repo;
    }
}
