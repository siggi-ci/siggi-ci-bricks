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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.siggici.connect.github.impl.BaseGitHubApiTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.MockRestResponseCreators;

/**
 * Tests for {@link UserTemplate}.
 * 
 * @author jbellmann
 *
 */
public class UserTemplateTest extends BaseGitHubApiTest {

    protected UserOperations userOperations;

    @Before
    public void buildOrganizationTemplate() {
        userOperations = new UserTemplate(restTemplate, true);
    }

    @Test
    public void getEmails() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("listEmails.json", getClass()), MediaType.APPLICATION_JSON));

        List<Email> emailList = userOperations.listEmails();

        Assertions.assertThat(emailList).isNotNull();
        Assertions.assertThat(emailList.size()).isEqualTo(1);
    }

    @Test
    public void addEmails() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.POST))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(
                        withSuccess(new ClassPathResource("addEmails.json", getClass()), MediaType.APPLICATION_JSON));

        List<Email> emailList = userOperations.addEmails("octocat@github.com", "support@github.com");

        Assertions.assertThat(emailList).isNotNull();
        Assertions.assertThat(emailList.size()).isEqualTo(2);
    }

    @Test
    public void deleteEmails() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.DELETE))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(MockRestResponseCreators.withNoContent());

        userOperations.deleteEmails("octocat@github.com", "support@github.com");

        mockServer.verify();
    }

    // DEPLOY_KEYS

    @Test
    public void getPublicKeys() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/keys")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("listPublicKeys.json", getClass()),
                        MediaType.APPLICATION_JSON));

        List<ExtPubKey> publicKeyList = userOperations.listPublicKeys();

        Assertions.assertThat(publicKeyList).isNotNull();
        Assertions.assertThat(publicKeyList.size()).isEqualTo(1);
        Assertions.assertThat(publicKeyList.get(0).getKey()).isEqualTo("ssh-rsa AAA...");
    }

    @Test
    public void getPublicKeysForUser() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/users/klaus/keys")).andExpect(method(HttpMethod.GET))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(withSuccess(new ClassPathResource("listPublicKeysForUser.json", getClass()),
                        MediaType.APPLICATION_JSON));

        List<PubKey> pubKeyList = userOperations.listPublicKeys("klaus");

        Assertions.assertThat(pubKeyList).isNotNull();
        Assertions.assertThat(pubKeyList.size()).isEqualTo(1);
        Assertions.assertThat(pubKeyList.get(0).getKey()).isEqualTo("ssh-rsa AAA...");
    }

    @Test
    public void deletePublicKey() throws Exception {
        mockServer.expect(requestTo("https://api.github.com/user/keys/1")).andExpect(method(HttpMethod.DELETE))
                // .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
                .andRespond(MockRestResponseCreators.withNoContent());

        userOperations.deletePublicKey(1);
    }

}
