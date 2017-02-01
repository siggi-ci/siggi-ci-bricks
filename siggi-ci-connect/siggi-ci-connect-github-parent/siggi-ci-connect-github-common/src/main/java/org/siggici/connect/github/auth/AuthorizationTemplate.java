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
package org.siggici.connect.github.auth;

import org.siggici.connect.github.AbstractGitHubOperations;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jbellmann on 26.02.16.
 */
public class AuthorizationTemplate extends AbstractGitHubOperations implements AuthorizationOperations {

	private final String apiBaseUrl;
	
	public AuthorizationTemplate(RestTemplate restTemplate, boolean authorized) {
		this(restTemplate, authorized, API_URL_BASE);
	}

	public AuthorizationTemplate(RestTemplate restTemplate, boolean authorized, String apiBaseUrl) {
		super(restTemplate, authorized, apiBaseUrl);
		this.apiBaseUrl = apiBaseUrl;
	}

	@Override
	public TokenResponse createToken(String clientId, TokenRequest tokenRequest) {
		RequestEntity<TokenRequest> requestEntity = RequestEntity
				.put(buildUri("authorizations/clients/" + clientId)).contentType(MediaType.APPLICATION_JSON)
				.body(tokenRequest);
		return getRestTemplate().exchange(requestEntity, TokenResponse.class).getBody();
	}

	@Override
	public TokenResponse createNewToken(CreateNewTokenRequest newTokenRequest) {
		RequestEntity<CreateNewTokenRequest> requestEntity = RequestEntity.post(buildUri("authorizations"))
				.contentType(MediaType.APPLICATION_JSON).body(newTokenRequest);
		return getRestTemplate().exchange(requestEntity, TokenResponse.class).getBody();
	}

	protected void requireAuthorization() {
		if (!isAuthorized()) {
			throw new MissingAuthorizationException(apiBaseUrl);
		}
	}

}
