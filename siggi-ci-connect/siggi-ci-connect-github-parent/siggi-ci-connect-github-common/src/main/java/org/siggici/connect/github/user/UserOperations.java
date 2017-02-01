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

import java.util.List;

public interface UserOperations {

    /**
     * Retrieves the user's GitHub profile ID.
     *
     * @return the user's GitHub profile ID.
     */
    String getProfileId();

    /**
     * Retrieves the user's GitHub profile details.
     *
     * @return the user's GitHub profile
     */
    GitHubUserProfile getUserProfile();

    List<Email> listEmails();

    List<Email> addEmails(String... emails);

    List<Email> addEmails(List<String> emails);

    void deleteEmails(String... emails);

    void deleteEmails(List<String> emails);

    List<PubKey> listPublicKeys(String username);

    List<ExtPubKey> listPublicKeys();

    ExtPubKey getPublicKey(long id);

    ExtPubKey createPublicKey(PubKeyInput pubKey);

    void deletePublicKey(long id);

}
