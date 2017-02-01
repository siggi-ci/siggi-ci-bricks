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
package org.siggici.keys.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Optional;

import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Very simple RestController for creating an fetching {@link DeployKey}s.
 * 
 * @author jbellmann
 *
 */
@RestController
@RequestMapping("/api")
public class KeysRestController {

    private final DeployKeyStore deployKeyStore;

    public KeysRestController(DeployKeyStore deployKeyStore) {
        this.deployKeyStore = deployKeyStore;
    }

    /**
     * Creates {@link DeployKey}s by doing a simple POST-request.
     * 
     * @return
     */
    @PostMapping("/keys")
    public ResponseEntity<DeployKey> createKey() {
        String created = this.deployKeyStore.create();
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(created).toUri();
        return created(location).body(deployKeyStore.byId(created, false).get());
    }

    /**
     * Fetches the {@link DeployKey} with id from DB.
     * 
     * @param id
     * @param full
     * @return
     */
    @GetMapping(value = "/keys/{id}")
    public ResponseEntity<DeployKey> getWithId(@PathVariable String id,
            @RequestParam(defaultValue = "false") String full) {
        Optional<DeployKey> deployKey = deployKeyStore.byId(id, Boolean.valueOf(full));
        return deployKey.map(dpk -> ResponseEntity.ok(dpk))
                        .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }
}
