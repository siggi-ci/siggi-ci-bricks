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
package org.siggici.connect.github.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siggici.connect.github.AbstractGitHubOperations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author jbellmann
 */
public class UserTemplate extends AbstractGitHubOperations implements UserOperations {

    public static final String USER_EMAILS_PATH = "/user/emails";

    private final ParameterizedTypeReference<List<Email>> emailListTypeRef = new ParameterizedTypeReference<List<Email>>() {
    };

    private final ParameterizedTypeReference<List<ExtPubKey>> extPubKeyListTypeRef = new ParameterizedTypeReference<List<ExtPubKey>>() {
    };

    private final ParameterizedTypeReference<List<PubKey>> pubKeyListTypeRef = new ParameterizedTypeReference<List<PubKey>>() {
    };

    public UserTemplate(RestTemplate restTemplate, boolean authorized) {
        this(restTemplate, authorized, API_URL_BASE);
    }

    public UserTemplate(RestTemplate restTemplate, boolean authorized, String apiBaseUrl) {
        super(restTemplate, authorized, apiBaseUrl);
    }

    @Override
    public String getProfileId() {
        return getUserProfile().getLogin();
    }

    @Override
    public GitHubUserProfile getUserProfile() {
        return getRestTemplate().getForObject(buildUri("user"), GitHubUserProfile.class);
    }

    @Override
    public List<Email> listEmails() {
        return Arrays
                .asList(getRestTemplate().getForEntity(buildUriString(USER_EMAILS_PATH), Email[].class).getBody());
    }

    @Override
    public List<Email> addEmails(String... emails) {
        return addEmails(Arrays.asList(emails));
    }

    @Override
    public List<Email> addEmails(List<String> emails) {
        RequestEntity<List<String>> reqEntity = RequestEntity.post(buildUri(USER_EMAILS_PATH))
                .contentType(MediaType.APPLICATION_JSON).body(emails);
        return getRestTemplate().exchange(reqEntity, emailListTypeRef).getBody();
    }

    @Override
    public void deleteEmails(String... emails) {
        deleteEmails(Arrays.asList(emails));
    }

    @Override
    public void deleteEmails(List<String> emails) {
        RequestEntity<List<String>> reqEntity = RequestEntity.method(HttpMethod.DELETE, buildUri(USER_EMAILS_PATH))
                .contentType(MediaType.APPLICATION_JSON).body(emails);
        getRestTemplate().exchange(reqEntity, Void.class);
    }

    @Override
    public List<PubKey> listPublicKeys(String username) {
        Map<String, Object> uriVariabels = new HashMap<String, Object>(0);
        uriVariabels.put("username", username);

        RequestEntity<Void> reqEntity = RequestEntity.get(buildUri("/users/{username}/keys", uriVariabels)).build();

        return getRestTemplate().exchange(reqEntity, pubKeyListTypeRef).getBody();
    }

    @Override
    public List<ExtPubKey> listPublicKeys() {
        return getRestTemplate().exchange(buildUri("/user/keys"), HttpMethod.GET, null, extPubKeyListTypeRef)
                .getBody();
    }

    @Override
    public ExtPubKey getPublicKey(long id) {
        Map<String, Object> uriVariabels = new HashMap<String, Object>(0);
        uriVariabels.put("id", id);
        return getRestTemplate().getForObject(buildUri("/user/keys/{id}", uriVariabels), ExtPubKey.class);
    }

    @Override
    public ExtPubKey createPublicKey(PubKeyInput pubKey) {
        RequestEntity<PubKeyInput> reqEntity = RequestEntity.post(buildUri(USER_EMAILS_PATH))
                .contentType(MediaType.APPLICATION_JSON).body(pubKey);
        return getRestTemplate().exchange(reqEntity, ExtPubKey.class).getBody();
    }

    @Override
    public void deletePublicKey(long id) {
        Map<String, Object> uriVariabels = new HashMap<String, Object>(0);
        uriVariabels.put("id", id);
        getRestTemplate().delete(buildUri("/user/keys/{id}", uriVariabels));
    }

}
