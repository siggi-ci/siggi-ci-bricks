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
package org.siggici.connect.github.ghe.connect;

import org.siggici.connect.github.auth.AuthorizationOperations;
import org.siggici.connect.github.auth.AuthorizationTemplate;
import org.siggici.connect.github.ghe.api.Ghe;
import org.siggici.connect.github.organization.OrganizationOperations;
import org.siggici.connect.github.organization.OrganizationTemplate;
import org.siggici.connect.github.repository.RepositoryOperations;
import org.siggici.connect.github.repository.RepositoryTemplate;
import org.siggici.connect.github.user.UserOperations;
import org.siggici.connect.github.user.UserTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;

class GheTemplate extends AbstractOAuth2ApiBinding implements Ghe {

	private String accessToken;
	
	private String apiBaseUrl;

	private UserOperations userOperations;

	private RepositoryOperations repositoryOperations;

	private AuthorizationOperations authorizationOperations;

	private OrganizationOperations organizationOperations;

	public GheTemplate(String accessToken, String apiBaseUrl) {
		super(accessToken);
		this.accessToken = accessToken;
		this.apiBaseUrl = apiBaseUrl;
		initialize();
	}

	@Override
	protected OAuth2Version getOAuth2Version() {
		return OAuth2Version.BEARER;
	}

	private void initialize() {
		userOperations = new UserTemplate(getRestTemplate(), isAuthorized(), this.apiBaseUrl);
		repositoryOperations = new RepositoryTemplate(getRestTemplate(), isAuthorized(), this.apiBaseUrl);
		authorizationOperations = new AuthorizationTemplate(getRestTemplate(), isAuthorized(), this.apiBaseUrl);
		organizationOperations = new OrganizationTemplate(getRestTemplate(), isAuthorized(), this.apiBaseUrl);
	}

	@Override
	public UserOperations getUserOperations() {
		return userOperations;
	}

	@Override
	public RepositoryOperations getRepositoryOperations() {
		return repositoryOperations;
	}

	@Override
	public AuthorizationOperations getAuthorizationOperations() {
		return authorizationOperations;
	}

	@Override
	public OrganizationOperations getOrganizationOperations() {
		return organizationOperations;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getApiBaseUrl(){
		return apiBaseUrl;
	}

}
