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
package org.siggici.keys;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultDeployKeyService implements DeployKeyService {

    private final int type;
    private final int size;
    private final String comment;

    public DefaultDeployKeyService(int type, int size, String comment) {
        this.type = type;
        this.size = size;
        this.comment = comment;
    }

    /**
     * This could take a while.
     */
    public DeployKey create() {
        log.debug("creating keypair ...");
        try {
            ByteArrayOutputStream privBaos = new ByteArrayOutputStream();
            ByteArrayOutputStream pubBaos = new ByteArrayOutputStream();
            KeyPair keypair = KeyPair.genKeyPair(new JSch(), type, size);
            keypair.writePrivateKey(privBaos, null);
            keypair.writePublicKey(pubBaos, comment);
            return DeployKey.builder().id(UUID.randomUUID().toString()).publicKey(new String(pubBaos.toByteArray()))
                    .privateKey(new String(privBaos.toByteArray())).fingerprint(keypair.getFingerPrint()).build();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create DeployKey", e);
        } finally {
            log.debug("keypair created");
        }
    }

}
