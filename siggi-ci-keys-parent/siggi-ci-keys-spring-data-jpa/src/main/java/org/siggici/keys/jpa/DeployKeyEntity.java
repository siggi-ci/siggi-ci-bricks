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
package org.siggici.keys.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.siggici.keys.DeployKey;

import lombok.Data;

@Entity
@Data
class DeployKeyEntity {

    @Id
    private String id;
    private String publicKey;
    private String privateKey;
    private String fingerprint;

    protected DeployKeyEntity() {
    }

    DeployKey externalForm() {
        return DeployKey.builder().id(id).publicKey(publicKey).privateKey(privateKey).fingerprint(fingerprint).build();
    }

    static DeployKeyEntity fromDeployKey(DeployKey dk) {
        DeployKeyEntity e = new DeployKeyEntity();
        e.id = dk.getId();
        e.publicKey = dk.getPublicKey();
        e.privateKey = dk.getPrivateKey();
        e.fingerprint = dk.getFingerprint();
        return e;
    }
}
