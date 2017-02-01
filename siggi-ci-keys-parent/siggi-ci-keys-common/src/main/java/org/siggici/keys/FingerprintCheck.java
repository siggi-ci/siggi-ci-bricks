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

import java.util.Base64;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FingerprintCheck {

    private static final String HOST = "host";

    public boolean hasSameFingerprint(String pubkey, String fingerprint) {
        try {
            byte[] pubKeyDecoded = Base64.getDecoder().decode(pubkey.getBytes());
            HostKey hk;
            hk = new HostKey(HOST, pubKeyDecoded);
            String pubkeyFingerprint = hk.getFingerPrint(new JSch());
            return fingerprint.equals(pubkeyFingerprint);
        } catch (JSchException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

}
