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
package org.siggici.webhooks.github;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

public class PullRequestPayloadTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String OPENED_SHA = "d25b30123240c5fef7a76f713fb1b66028d778d5";
    private static final String SYNCHRONIZE_SHA = "86028c84959bbc671a7a2b35eadeb881f1efbdc2";

    @Test
    public void readPullRequestOpened() throws JsonParseException, JsonMappingException, IOException {
        PullRequestPayload payload = mapper.readValue(readClasspathResource("/pr_opened.json"),
                PullRequestPayload.class);
        assertCommonPullRequest(payload, OPENED_SHA);
    }

    @Test
    public void readPullRequestClosed() throws JsonParseException, JsonMappingException, IOException {
        PullRequestPayload payload = mapper.readValue(readClasspathResource("/pr_closed.json"),
                PullRequestPayload.class);
        assertCommonPullRequest(payload, SYNCHRONIZE_SHA);
    }

    @Test
    public void readPullRequestSyncronize() throws JsonParseException, JsonMappingException, IOException {
        PullRequestPayload payload = mapper.readValue(readClasspathResource("/pr_synchronize.json"),
                PullRequestPayload.class);
        assertCommonPullRequest(payload, SYNCHRONIZE_SHA);
    }

    protected void assertCommonPullRequest(PullRequestPayload payload, String shaToMatch) {
        Assertions.assertThat(payload).isNotNull();
        Assertions.assertThat(payload.getPullRequest()).isNotNull();
        Assertions.assertThat(payload.getPullRequest().getHead()).isNotNull();
        Assertions.assertThat(payload.getPullRequest().getHead().getSha()).isEqualTo(shaToMatch);
    }

    String readClasspathResource(String path) {
        try {
            return new String(Resources.asByteSource(new ClassPathResource(path, getClass()).getURL()).read());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
