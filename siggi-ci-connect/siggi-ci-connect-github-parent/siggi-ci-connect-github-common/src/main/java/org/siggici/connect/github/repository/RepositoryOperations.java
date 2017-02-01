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
package org.siggici.connect.github.repository;

import java.util.List;

import org.siggici.connect.github.organization.GitHubRepo;

/**
 * Only operations we really need.
 *
 * @author  jbellmann
 */
public interface RepositoryOperations {

    /**
     * Get the repository.
     *
     * @param   user
     * @param   repo
     *
     * @return
     */
    GitHubRepo getRepository(String user, String repo);

    /**
     * List all repositories for specified user.
     *
     * @param   user
     *
     * @return  list of repositories
     */
    List<GitHubRepo> getRepositories(String user);

    /**
     * @param   user
     * @param   repo
     *
     * @return  list of deploy-keys
     */
    List<GitHubDeployKey> getDeployKeys(String user, String repo);

    /**
     * @param   user
     * @param   repo
     * @param   id
     *
     * @return  deploy-key for id
     */
    GitHubDeployKey getDeployKey(String user, String repo, int id);

    /**
     * Adds an deploy-key to the repository.
     *
     * @param   owner
     * @param   repo
     * @param   title
     * @param   key
     *
     * @return  deploy-key with id
     */
    GitHubDeployKey addDeployKey(String owner, String repo, String title, String key);

    /**
     * Deletes the deploy-key with id.
     *
     * @param  owner
     * @param  repo
     * @param  id
     */
    void deleteDeployKey(String owner, String repo, int id);

    /**
     * @param   user
     * @param   repo
     *
     * @return
     */
    List<GitHubWebhook> getWebhooks(String user, String repo);

    /**
     * @param   user
     * @param   repo
     * @param   id
     *
     * @return
     */
    GitHubWebhook getWebhook(String user, String repo, String id);

    /**
     * @param   user
     * @param   repo
     * @param   request
     *
     * @return
     */
    GitHubWebhook addWebhook(String user, String repo, Object request);

    /**
     * @param  user
     * @param  repo
     * @param  id
     */
    void deleteWebhook(String user, String repo, String id);

    Status createStatus(String owner, String repository, String sha, StatusRequest body);

    List<Status> listStatuses(String owner, String repository, String ref);

    CombinedStatus getCombinedStatus(String owner, String repository, String ref);

}
