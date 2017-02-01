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
package org.siggici.data.builds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.siggici.data.jpa.AbstractEntity;
import org.siggici.data.jpa.RepoId;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
//formatter:off
@Table(uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "provider", "orga", "repo", "buildnumber" }, name="build_id_unique_constraint")
})
//formatter:on
@Getter
@EqualsAndHashCode(callSuper = true)
@EntityListeners({ BuildListener.class })
public class Build extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @NotBlank
    private String state = BuildStates.CREATED;

    @Embedded
    private RepoId repoId;

    @Column(nullable = false)
    @NotNull
    private Long buildnumber;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "build")
    private List<BuildSlot> buildSlots = new ArrayList<BuildSlot>(0);

    @Embedded
    private BuildHook buildHook = new BuildHook();

    @Embedded
    private Clock clock = new Clock();

    public BuildSlot addBuildSlot() {
        BuildSlot slot = new BuildSlot(this, this.buildSlots.size() + 1);
        this.buildSlots.add(slot);
        return slot;
    }
}
