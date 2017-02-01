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

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.siggici.data.jpa.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BuildSlot extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "build_ref", foreignKey = @ForeignKey(name = "build_ref_fk"))
    private Build build;

    private int slot = -1;

    @Embedded
    private Clock clock = new Clock();

    private String state = BuildStates.CREATED;

    private String matrixDiscriminator;

    private BuildSlot() {
        //
    }

    BuildSlot(Build build, int slot) {
        this.build = build;
        this.slot = slot;
    }

    public void start() {
        this.state = BuildStates.RUNNING;
        this.clock.start();
    }

    public void fail() {
        this.state = BuildStates.FAILED;
        this.clock.stop();
    }
}
