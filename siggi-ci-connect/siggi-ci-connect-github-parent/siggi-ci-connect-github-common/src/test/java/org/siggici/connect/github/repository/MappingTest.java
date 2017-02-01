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
package org.siggici.connect.github.repository;

import java.io.IOException;

import org.junit.Test;
import org.siggici.connect.github.organization.GitHubRepo;
import org.siggici.connect.github.repository.GitHubRepoOwner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MappingTest {

    @Test
    public void readWriteGitHubRepo() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        GitHubRepo repo = new GitHubRepo();
        repo.setOwner(new GitHubRepoOwner());
        objectMapper.writeValue(System.out, repo);
    }

    @Test
    public void writeAddWebhookRequest() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        AddWebhookRequest request = new AddWebhookRequest();
        request.setActive(true);
        request.setName("sigmalab");
        request.getConfig().setContentType("json");
        request.getConfig().setUrl("https://url.to.siggi/path/to/hook");
        objectMapper.writeValue(System.out, request);
    }

}
