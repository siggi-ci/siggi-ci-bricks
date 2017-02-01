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
package org.siggici.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;


public class SimpleSocialUserDetailsService implements SocialUserDetailsService {


    private final Logger log = LoggerFactory.getLogger(SimpleSocialUserDetailsService.class);

    private UserDetailsService userDetailsService;

    public SimpleSocialUserDetailsService(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(final String username) throws UsernameNotFoundException,
            DataAccessException {

        try {
            log.debug("LOAD USER : {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new SocialUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
