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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class CreateKeyTest extends AbstractKeysRestClientTest {

    private KeysRestClient client;

    @Before
    public void buildOrganizationTemplate() {
        client = new KeysRestClient(restTemplate, "http://keys_service");
    }

    @Test
    public void create() {
        mockServer
            .expect(requestTo("http://keys_service/api/keys"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withCreatedEntity(URI.create("http://keys_service/api/keys/08da1346-a8ca-44d5-a078-f4586da2cb5e"))
                            .body(jsonResource("deployKey"))
                            .contentType(MediaType.APPLICATION_JSON));

        String keyId = client.create();

        assertThat(keyId).isEqualTo("08da1346-a8ca-44d5-a078-f4586da2cb5e");
    }
}
