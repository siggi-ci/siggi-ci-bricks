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
package org.siggici.connect.github.social;

import org.siggici.connect.github.Github;
import org.siggici.connect.github.user.GitHubUserProfile;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import javaslang.Tuple;
import javaslang.Tuple2;

public class GithubAdapter<T extends Github> implements ApiAdapter<T> {

    @Override
    public boolean test(T api) {
        try {
            api.getUserOperations().getUserProfile().getLogin();
            return true;
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @Override
    public void setConnectionValues(T api, ConnectionValues values) {
        GitHubUserProfile ghUserProfile = api.getUserOperations().getUserProfile();
        Tuple2<String, String> loginNameTuple = extractLoginName(ghUserProfile);

        values.setProviderUserId(loginNameTuple._1);
        values.setDisplayName(loginNameTuple._2);
    }

    @Override
    public UserProfile fetchUserProfile(T api) {
        GitHubUserProfile ghUserProfile = api.getUserOperations().getUserProfile();
        Tuple2<String, String> loginNameTuple = extractLoginName(ghUserProfile);

        return new UserProfileBuilder().setUsername(loginNameTuple._1).setName(loginNameTuple._2)
                .setEmail(ghUserProfile.getEmail()).build();
    }

    @Override
    public void updateStatus(T api, String message) {
        throw new UnsupportedOperationException();
    }

    protected Tuple2<String, String> extractLoginName(GitHubUserProfile userProfile) {
        String login = userProfile.getLogin();
        String name = userProfile.getName();
        if (name.trim().isEmpty()) {
            name = login;
        }
        return Tuple.of(login, name);
    }

}
