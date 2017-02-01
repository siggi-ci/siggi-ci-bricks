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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
public class KeysRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private DeployKeyStore deployKeyStore;

    private DeployKey deployKey;

    private DeployKey fullDeployKey;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new KeysRestController(deployKeyStore))
                .alwaysDo(MockMvcResultHandlers.print()).build();

        deployKey = DeployKey.builder().id(UUID.randomUUID().toString()).publicKey("public_key")
                .fingerprint("fingerprint").build();

        fullDeployKey = DeployKey.builder().id(UUID.randomUUID().toString()).publicKey("public_key")
                .privateKey("private_key").fingerprint("fingerprint").build();
    }

    @Test
    public void getDefaultDeployKey() throws Exception {
        when(deployKeyStore.byId(Mockito.anyString(), Mockito.eq(Boolean.FALSE)))
                .thenReturn(Optional.of(deployKey));

        mockMvc.perform(get("/api/keys/1")).andExpect(status().isOk());

        verify(deployKeyStore, atLeastOnce()).byId(Mockito.eq("1"), Mockito.eq(Boolean.FALSE));
    }

    @Test
    public void getFullDeployKey() throws Exception {
        when(deployKeyStore.byId(Mockito.anyString(), Mockito.eq(Boolean.TRUE)))
                .thenReturn(of(fullDeployKey));

        mockMvc.perform(get("/api/keys/1").param("full", "true"))
                .andExpect(status().isOk());

        verify(deployKeyStore, atLeastOnce()).byId(Mockito.eq("1"), Mockito.eq(Boolean.TRUE));
    }

    @Test
    public void createDeployKey() throws Exception {
        when(deployKeyStore.create()).thenReturn(deployKey.getId());
        when(deployKeyStore.byId(Mockito.anyString(), Mockito.eq(Boolean.FALSE)))
                .thenReturn(of(deployKey));

        mockMvc.perform(post("/api/keys")).andExpect(status().isCreated());

        verify(deployKeyStore, atLeastOnce()).create();
    }

    @Test
    public void deployKeyNotFound() throws Exception {
        when(deployKeyStore.create()).thenReturn(deployKey.getId());
        when(deployKeyStore.byId(Mockito.anyString(), Mockito.eq(Boolean.FALSE))).thenReturn(empty());

        mockMvc.perform(get("/api/keys/13"))
                .andExpect(status().isNotFound());

        verify(deployKeyStore, atLeastOnce()).byId(Mockito.anyString(), Mockito.eq(Boolean.FALSE));
    }

}
