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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.siggici.data.ids.DeployKeyRef;
import org.siggici.data.ids.EncryptionKey;
import org.siggici.data.jpa.AbstractEntity;
import org.siggici.data.jpa.DeployKeyRefAttributeConverter;
import org.siggici.data.jpa.EncryptionKeyAttributeConverter;
import org.siggici.data.jpa.RepoId;
import org.springframework.util.Assert;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
// formatter:off
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "provider", "orga", "repo" }, name = "repo_id_unique_constraint") })
// formatter:on
@Getter
@EqualsAndHashCode(callSuper = false, of = { "repoId" })
@ToString
public class Project extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Embedded
    private RepoId repoId;

    private boolean active = true;

    @Convert(converter = EncryptionKeyAttributeConverter.class)
    private EncryptionKey encryptionKey;

    @Convert(converter = DeployKeyRefAttributeConverter.class)
    private DeployKeyRef deployKeyRef;

    @Embedded
    private RepositoryDetails repositoryDetails = new RepositoryDetails();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ENVIRONMENT_SETTING", joinColumns = @JoinColumn(name = "project_id"))
    private List<ProjectEnvironmentSetting> environmentSettings = new ArrayList<>();

    private String accessToken;

    private Project() {
    }

    @Builder
    private Project(RepoId repoId, EncryptionKey encryptionKey, DeployKeyRef deployKeyRef, String accessToken) {
        Assert.notNull(repoId, "'repoId' should never be null");
        Assert.notNull(encryptionKey, "'encryptionKey' should never be null");
        Assert.notNull(deployKeyRef, "'deployKeyRef' should never be null");
        this.repoId = repoId;
        this.encryptionKey = encryptionKey;
        this.deployKeyRef = deployKeyRef;
        this.accessToken = accessToken;
    }

    public void addEnvironmentSetting(ProjectEnvironmentSetting environmentSetting) throws KeyAlreadyExistsException {
        Assert.notNull(environmentSetting, "'environmentSetting' should never be null");
        validateForKeyAlreadyExists(environmentSetting);
        this.environmentSettings.add(environmentSetting);
    }

    public void removeEnvironmentSetting(String key) {
        this.environmentSettings.removeIf(new SameKeyPredicate(key));
    }

    protected void validateForKeyAlreadyExists(ProjectEnvironmentSetting environmentSetting)
            throws KeyAlreadyExistsException {
        long withKeyInList = this.environmentSettings.stream().filter(new SameKeyPredicate(environmentSetting.getKey()))
                .count();
        if (withKeyInList > 0) {
            throw new KeyAlreadyExistsException(environmentSetting.getKey());
        }
    }

    public List<ProjectEnvironmentSetting> getEnvironmentSettings() {
        return Collections.unmodifiableList(this.environmentSettings);
    }

    public void activate() {
        this.active = true;
    }

    public void deActivate() {
        this.active = false;
    }

    protected static class SameKeyPredicate implements Predicate<ProjectEnvironmentSetting> {

        private final String key;

        SameKeyPredicate(String key) {
            this.key = key;
        }

        @Override
        public boolean test(ProjectEnvironmentSetting t) {
            return t.getKey().equals(key);
        }

    }
}
