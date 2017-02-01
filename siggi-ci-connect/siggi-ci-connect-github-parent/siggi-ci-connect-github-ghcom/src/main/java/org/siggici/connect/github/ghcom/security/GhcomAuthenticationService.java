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
package org.siggici.connect.github.ghcom.security;

import org.siggici.connect.github.ghcom.api.Ghcom;
import org.siggici.connect.github.ghcom.connect.GhcomConnectionFactory;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

public class GhcomAuthenticationService extends OAuth2AuthenticationService<Ghcom> {

    public GhcomAuthenticationService(String clientId, String clientSecret) {
        super(new GhcomConnectionFactory(clientId, clientSecret));
    }
}
