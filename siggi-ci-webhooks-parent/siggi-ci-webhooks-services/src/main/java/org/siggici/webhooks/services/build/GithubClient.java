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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GithubClient {

    private final ObjectMapper mapper;
    private final RestTemplate client;

    public GithubClient(ObjectMapper mapper, String token) {
        this.mapper = mapper;
        this.client = new RestTemplate();
        this.client.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                request.getHeaders().add("Authorization", "Bearer " + token);
                return execution.execute(request, body);
            }
        });
    }

    public void sendComment(String body, String issueUrl) {
        try {
            Map<String,String> map = new HashMap<>();
            map.put("body", body);
            RequestEntity<?> re = RequestEntity.post(new URI(issueUrl + "/comments")).body(map);
            client.exchange(re, String.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    public JsonNode setCommitStatus(String repoUrl, String commitSha, String status, String detailsUrl, String desc) {
        String statusUrl = String.format("%s/statuses/%s", repoUrl, commitSha);

        ObjectNode statusPayload = mapper.createObjectNode()
                .put("state", status)
                .put("target_url", detailsUrl)
                .put("description", desc)
                .put("context", "Swagger Linter");

        HttpEntity<JsonNode> entity = new HttpEntity<>(statusPayload);
        ResponseEntity<JsonNode> response = client.postForEntity(statusUrl, entity, JsonNode.class);
        return response.getBody();
    }

    public String getFileContent(String repoUrl, String commitSha, String pathToFile) {
        String url = String.format("%s/contents/%s?ref=%s", repoUrl, pathToFile, commitSha);
        JsonNode body = client.getForEntity(url, JsonNode.class).getBody();
        return Arrays.stream(body.get("content").asText().split("\\n"))
                .map(GithubClient::decode)
                .collect(Collectors.joining());
    }

    public static String encode(String data) {
        try {
            return Base64.getEncoder().encodeToString((data).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
