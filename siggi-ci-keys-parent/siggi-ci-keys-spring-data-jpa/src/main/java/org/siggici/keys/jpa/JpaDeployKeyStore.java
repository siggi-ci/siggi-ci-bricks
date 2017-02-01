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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.siggici.keys.jpa.DeployKeyEntity.fromDeployKey;

import java.util.Optional;
import java.util.function.BiFunction;

import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyService;
import org.siggici.keys.DeployKeyStore;
import org.siggici.keys.FingerprintCheck;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 
 * @author jbellmann
 *
 */
public class JpaDeployKeyStore implements DeployKeyStore {

    private final DeployKeyService delegate;
    private final PagingAndSortingRepository<DeployKeyEntity, String> repository;
    private final FingerprintCheck fingerprintCheck = new FingerprintCheck();

    public JpaDeployKeyStore(DeployKeyService deployKeyService,
            PagingAndSortingRepository<DeployKeyEntity, String> repository) {
        this.delegate = requireNonNull(deployKeyService, "'deployKeyService' should never be null");
        this.repository = requireNonNull(repository, "'repository' should never be null");
    }

    @Override
    public String create() {
        final DeployKey dk = this.delegate.create();
        return of(this.repository.save(fromDeployKey(dk))).flatMap(dke -> Optional.of(dke.getId()))
                .get();
    }

    @Override
    public Optional<DeployKey> byId(String id, boolean full) {
        return ofNullable(this.repository.findOne(id)).map(it -> of(it.externalForm())).orElse(empty())
                .flatMap(dk -> fun.apply(dk, full));
    }

    private static BiFunction<DeployKey, Boolean, Optional<DeployKey>> fun = (dk, fully) -> {
        return Optional.of(dk.fully(fully));
    };

    @Override
    public boolean hasSameFingerprint(String pubkey, String fingerprint) {
        return fingerprintCheck.hasSameFingerprint(pubkey, fingerprint);
    }

}
