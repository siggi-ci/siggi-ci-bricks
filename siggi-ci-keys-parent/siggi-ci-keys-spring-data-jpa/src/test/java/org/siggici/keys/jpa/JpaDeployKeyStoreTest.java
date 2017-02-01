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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siggici.keys.jpa.DeployKeyEntity.fromDeployKey;

import java.util.Optional;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyService;
import org.siggici.keys.DeployKeyStore;
import org.springframework.data.repository.PagingAndSortingRepository;

public class JpaDeployKeyStoreTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final DeployKey key1 = DeployKey.builder().id(UUID.randomUUID().toString()).publicKey("publicKey")
            .privateKey("privateKey").fingerprint("finger").build();

    @Test
    public void testCreationSuccessfull() {
        DeployKeyService service = Mockito.mock(DeployKeyService.class);
        PagingAndSortingRepository<DeployKeyEntity, String> repository = Mockito.mock(PagingAndSortingRepository.class);

        JpaDeployKeyStore store = new JpaDeployKeyStore(service, repository);

        when(service.create()).thenReturn(key1);
        when(repository.save(Mockito.any(DeployKeyEntity.class))).thenReturn(fromDeployKey(key1));

        String generatedId = ((DeployKeyStore) store).create();

        assertThat(generatedId).isEqualTo(key1.getId());

        Mockito.verify(service, atLeastOnce()).create();
    }

    @Test
    public void testRetrievedDeployKeyHasNoPrivateKey() {
        DeployKeyService service = Mockito.mock(DeployKeyService.class);
        PagingAndSortingRepository<DeployKeyEntity, String> repository = Mockito.mock(PagingAndSortingRepository.class);

        JpaDeployKeyStore store = new JpaDeployKeyStore(service, repository);

        when(service.create()).thenReturn(key1);
        when(repository.save(Mockito.any(DeployKeyEntity.class))).thenReturn(fromDeployKey(key1));
        when(repository.findOne(Mockito.eq(key1.getId()))).thenReturn(fromDeployKey(key1));

        Optional<DeployKey> dkOptional = ((DeployKeyStore) store).byId(key1.getId(), false);

        assertThat(dkOptional.get().getId()).isEqualTo(key1.getId());
        assertThat(dkOptional.get().getPublicKey()).isEqualTo(key1.getPublicKey());
        assertThat(dkOptional.get().getPrivateKey()).isNull();
        assertThat(dkOptional.get().getFingerprint()).isEqualTo(key1.getFingerprint());

        verify(repository, atLeastOnce()).findOne(Mockito.eq(key1.getId()));
    }
    
    @Test
    public void testRetrievedFullDeployKeyHasPrivateKey() {
        DeployKeyService service = Mockito.mock(DeployKeyService.class);
        PagingAndSortingRepository<DeployKeyEntity, String> repository = Mockito.mock(PagingAndSortingRepository.class);

        JpaDeployKeyStore store = new JpaDeployKeyStore(service, repository);

        when(service.create()).thenReturn(key1);
        when(repository.save(Mockito.any(DeployKeyEntity.class))).thenReturn(fromDeployKey(key1));
        when(repository.findOne(Mockito.eq(key1.getId()))).thenReturn(fromDeployKey(key1));

        Optional<DeployKey> dkOptional = ((DeployKeyStore) store).byId(key1.getId(), true);

        assertThat(dkOptional.get().getId()).isEqualTo(key1.getId());
        assertThat(dkOptional.get().getPublicKey()).isEqualTo(key1.getPublicKey());
        assertThat(dkOptional.get().getPrivateKey()).isEqualTo(key1.getPrivateKey());
        assertThat(dkOptional.get().getFingerprint()).isEqualTo(key1.getFingerprint());

        verify(repository, atLeastOnce()).findOne(Mockito.eq(key1.getId()));
    }

    @Test
    public void testNotFound() {
        DeployKeyService service = Mockito.mock(DeployKeyService.class);
        PagingAndSortingRepository<DeployKeyEntity, String> repository = Mockito.mock(PagingAndSortingRepository.class);

        JpaDeployKeyStore store = new JpaDeployKeyStore(service, repository);

        when(service.create()).thenReturn(key1);
        when(repository.save(Mockito.any(DeployKeyEntity.class))).thenReturn(fromDeployKey(key1));
        when(repository.findOne(Mockito.eq(key1.getId()))).thenReturn(null);

        Optional<DeployKey> dkOptional = ((DeployKeyStore) store).byId(key1.getId(), false);

        assertThat(dkOptional.isPresent()).isFalse();

        verify(repository, atLeastOnce()).findOne(Mockito.eq(key1.getId()));
    }

    
    @Test
    public void testCreationFailsOnSave() {
        thrown.expect(RuntimeException.class);
        DeployKeyService service = Mockito.mock(DeployKeyService.class);
        PagingAndSortingRepository<DeployKeyEntity, String> repository = Mockito.mock(PagingAndSortingRepository.class);

        JpaDeployKeyStore store = new JpaDeployKeyStore(service, repository);

        when(service.create()).thenReturn(key1);
        when(repository.save(Mockito.any(DeployKeyEntity.class))).thenThrow(new RuntimeException("FAILED_SAVE"));

        ((DeployKeyStore) store).create();
    }

}
