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
import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

public class KeyTest {

    @Test
    public void createKey() throws Exception {
        ByteArrayOutputStream privBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream pubBaos = new ByteArrayOutputStream();
        KeyPair keypair = KeyPair.genKeyPair(new JSch(), KeyPair.RSA , 4096);
        keypair.writePrivateKey(privBaos, null);
        keypair.writePublicKey(pubBaos, "deploykey@siggi-ci");
        System.out.println("public-key: \n" + new String(pubBaos.toByteArray()) );
        System.out.println("private-key: \n" + new String(privBaos.toByteArray()) );
        System.out.println("fingerprint: \n" + keypair.getFingerPrint() );
    }

    @Test
    public void serializeDeployKey() throws JsonGenerationException, JsonMappingException, IOException {
        DeployKey key = DeployKey.builder().id(UUID.randomUUID().toString()).privateKey("priv_key").publicKey("pub_key")
                .fingerprint("fingerPrint").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, key);
    }
}
