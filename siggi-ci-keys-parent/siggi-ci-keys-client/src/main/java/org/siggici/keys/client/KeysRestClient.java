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
package org.siggici.keys.client;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

/**
 * Simple {@link KeysRestClient}.
 * 
 * @author jbellmann
 *
 */
public class KeysRestClient implements DeployKeyStore {

    private final RestOperations restOperations;

    private final String postUrl;
    private final String getUrl;

    public KeysRestClient(RestOperations restOperations, String baseUrl) {
        this.restOperations = requireNonNull(restOperations, "'restOperations' should never be null");
        requireNonNull(baseUrl, "'baseUrl' should never be null");
        this.postUrl = baseUrl + "/api/keys";
        this.getUrl = baseUrl + "/api/keys/{id}?full={full}";
    }

    @Override
    public String create() {
        ResponseEntity<DeployKey> response = restOperations.postForEntity(postUrl, emptyMap(), DeployKey.class);
        return response.getBody().getId();
    }

    @Override
    public Optional<DeployKey> byId(String id, boolean full) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);
        variables.put("full", full);
        ResponseEntity<DeployKey> response = restOperations.getForEntity(getUrl, DeployKey.class, variables);
        return Optional.ofNullable(response.getBody());
    }

    @Override
    public boolean hasSameFingerprint(String pubkey, String fingerprint) {
        return false;
    }

}
