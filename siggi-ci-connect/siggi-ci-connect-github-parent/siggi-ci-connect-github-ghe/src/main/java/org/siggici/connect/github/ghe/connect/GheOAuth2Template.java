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

import org.springframework.social.oauth2.OAuth2Template;

/**
 * 
 * 'authorizeUrl' = https://your.company.domain/login/oauth/authorize
 * 'tokeninfoUrl' = https://your.company.domain/login/oauth/access_token
 * 'apiBaseUrl' = https://your.company.domain/api/v3/
 * 
 * @author jbellmann
 *
 */
public class GheOAuth2Template extends OAuth2Template {

	public GheOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
	}
}