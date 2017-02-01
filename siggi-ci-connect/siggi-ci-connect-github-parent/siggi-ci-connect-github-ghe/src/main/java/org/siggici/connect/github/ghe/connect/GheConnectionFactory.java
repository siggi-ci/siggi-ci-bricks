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

import org.siggici.connect.github.ghe.api.Ghe;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

public class GheConnectionFactory extends OAuth2ConnectionFactory<Ghe> {

//	public static final String PROVIDER_ID = "ghe";

	public GheConnectionFactory(final String clientId, final String clientSecret, String authorizeUrl,
			String accessTokenUrl, String apiBaseUrl) {
		super(Ghe.PROVIDER_ID, new GheServiceProvider(clientId, clientSecret, authorizeUrl, accessTokenUrl, apiBaseUrl),
				new GheAdapter());
	}

	@Override
	protected String extractProviderUserId(AccessGrant accessGrant) {
		Ghe ghe = ((GheServiceProvider) getServiceProvider()).getApi(accessGrant.getAccessToken());
		UserProfile userProfile = getApiAdapter().fetchUserProfile(ghe);
		return userProfile.getUsername();
	}

}
