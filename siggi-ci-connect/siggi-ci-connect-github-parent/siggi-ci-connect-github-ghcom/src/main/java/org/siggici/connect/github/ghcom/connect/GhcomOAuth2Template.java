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
package org.siggici.connect.github.ghcom.connect;

import org.springframework.social.oauth2.OAuth2Template;

public class GhcomOAuth2Template extends OAuth2Template {

	private static final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";
	private static final String TOKENINFO_URL = "https://github.com/login/oauth/access_token";

	public GhcomOAuth2Template(final String clientId, final String clientSecret) {
		this(clientId, clientSecret, AUTHORIZE_URL, TOKENINFO_URL);
	}

	public GhcomOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String tokenInfoUrl) {
		super(clientId, clientSecret, authorizeUrl, tokenInfoUrl);
	}
}