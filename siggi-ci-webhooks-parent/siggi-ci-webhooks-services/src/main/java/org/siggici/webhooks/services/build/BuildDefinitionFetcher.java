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
package org.siggici.webhooks.services.build;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javaslang.Tuple;
import javaslang.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BuildDefinitionFetcher {

    private ObjectMapper om = new ObjectMapper();

    protected Optional<String> fetchBuildDefinition(String repoUrl, String accessToken, String sha) {
        return Optional.ofNullable(new GithubClient(om, accessToken)
                .getFileContent(repoUrl, sha, ".siggi.yml"));
    }

    protected Optional<?> parseBuildDefinition(String raw) {
        log.info("parse raw builddefinition ...");
        try {
            JsonNode json = om.readTree(raw);
            String type = json.path("type").asText("simple");
            String version = json.path("version").asText("1.0");
            return Optional.empty();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Tuple2<Optional<String>, Optional<?>> fetch(String repoUrl, String accessToken, String sha) {
        Optional<String> optionalRawContent = fetchBuildDefinition(repoUrl, accessToken, sha);
        Optional<?> optionalParsed = optionalRawContent.map(raw -> parseBuildDefinition(raw));
        return Tuple.of(optionalRawContent, optionalParsed);
    }

}
