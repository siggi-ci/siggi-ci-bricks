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
package org.siggici.services.github;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.siggici.connect.github.Github;
import org.siggici.connect.github.repository.GitHubDeployKey;
import org.siggici.data.jpa.RepoId;
import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyStore;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation that registers an 'Deploy-Key' for an repository.
 * 
 * @author jbellmann
 *
 */
@Slf4j
class DefaultGithubDeploykeyService implements GithubDeploykeyService {

    private final GithubDeploykeyServiceProperties properties;
    private final DeployKeyStore deployKeyStore;

    DefaultGithubDeploykeyService(GithubDeploykeyServiceProperties properties, DeployKeyStore deployKeyStore) {
        this.properties = properties;
        this.deployKeyStore = deployKeyStore;
    }

    @Override
    public void registerDeployKey(Github github, RepoId repoId, DeployKey dk) {
        List<GitHubDeployKey> deployKeys = github.getRepositoryOperations().getDeployKeys(repoId.getOrga(),
                repoId.getRepo());

        List<GitHubDeployKey> keyWithSameFingerprint = deployKeys.stream()
                .filter(new SameFingerprintPredicate(deployKeyStore, dk.getFingerprint())).collect(Collectors.toList());

        if (keyWithSameFingerprint.isEmpty()) {
            log.info(
                    "no existing deploy-key found in repository for repoid: {}, add new deploy-key with fingerprint: {} ",
                    repoId.toString(), dk.getFingerprint());
            github.getRepositoryOperations().addDeployKey(repoId.getOrga(), repoId.getRepo(), properties.getTitle(),
                    dk.getPublicKey());
        }
    }

    static class SameFingerprintPredicate implements Predicate<GitHubDeployKey> {
        private static final String EMPTY_STRING_REPLACEMENT = "";
        private static final String EQUAL_STR = "=";
        private static final String SSH_RSA_PREFIX = "ssh-rsa ";
        private final DeployKeyStore deployKeyStore;
        private final String fingerprint;

        public SameFingerprintPredicate(DeployKeyStore deployKeyStore, String expectedFingerprint) {
            this.deployKeyStore = deployKeyStore;
            this.fingerprint = expectedFingerprint;
        }

        @Override
        public boolean test(GitHubDeployKey t) {
            String key = t.getKey();
            key = key.replaceAll(SSH_RSA_PREFIX, EMPTY_STRING_REPLACEMENT);
            key = key.substring(0, key.lastIndexOf(EQUAL_STR) + 1);
            return deployKeyStore.hasSameFingerprint(key, fingerprint);
        }
    }
}
