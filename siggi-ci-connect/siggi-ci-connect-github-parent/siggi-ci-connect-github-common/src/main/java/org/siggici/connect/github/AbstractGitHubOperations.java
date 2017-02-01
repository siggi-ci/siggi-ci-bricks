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
package org.siggici.connect.github;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

public abstract class AbstractGitHubOperations {

    // GitHub API v3
    public static final String API_URL_BASE = "https://api.github.com/";

    private final Logger log = LoggerFactory.getLogger(AbstractGitHubOperations.class);

    private final RestTemplate restTemplate;
    private final boolean isAuthorized;
    private final String apiUrlBase;

    public AbstractGitHubOperations(RestTemplate restTemplate, boolean isAuthorized) {
        this(restTemplate, isAuthorized, API_URL_BASE);
    }

    public AbstractGitHubOperations(RestTemplate restTemplate, boolean isAuthorized, String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.isAuthorized = isAuthorized;
        this.apiUrlBase = apiBaseUrl;

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (log.isWarnEnabled()) {
                    if (response.getStatusCode() != HttpStatus.NOT_FOUND) {
                        String bodyText = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
                        log.warn("Github API REST response body:" + bodyText);
                    }
                }
            }

        });
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    protected boolean isAuthorized() {
        return isAuthorized;
    }

    protected String getApiBaseUrl() {
        return apiUrlBase;
    }

    public UriTemplate buildUriTemplate(String path) {
        return new UriTemplate(buildUriString(path));
    }

    public URI buildUri(String path, Map<String, Object> uriVariables) {
        return new UriTemplate(buildUriString(path)).expand(uriVariables);
    }

    public URI buildUri(String path) {
        return buildUri(path, new HashMap<String, Object>(0));
    }

    public String buildUriString(String path) {
        return getApiBaseUrl() + path;
    }

}
