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

import java.util.Optional;

/**
 * 
 * @author jbellmann
 *
 */
public interface DeployKeyStore {

    /**
     * Creates an {@link DeployKey} and returns the id for the generated
     * {@link DeployKey}.
     * 
     * @return
     */
    String create();

    /**
     * Get the {@link DeployKey} with specified id. If param 'full' is 'true'
     * the returned {@link DeployKey} will contain the 'private-key' too.
     * Otherwise only the 'public-key' should be available in the result.
     * 
     * @param id
     * @param full,
     *            use 'true' to get a {@link DeployKey} with 'private-key'.
     * @return {@link DeployKey}, with 'privateKey'
     */
    Optional<DeployKey> byId(String id, boolean full);

    boolean hasSameFingerprint(String pubkey, String fingerprint);

}
