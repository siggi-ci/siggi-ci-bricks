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

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  jbellmann
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubWebhook implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String url;

    @JsonProperty("test_url")
    private String testUrl;

    @JsonProperty("ping_url")
    private String pingUrl;

    private String name;

    private String[] events = new String[0];

    private boolean active;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("created_at")
    private Date createdAt;
    
    private GitHubWebhookConfig config = new GitHubWebhookConfig();

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(final String testUrl) {
        this.testUrl = testUrl;
    }

    public String getPingUrl() {
        return pingUrl;
    }

    public void setPingUrl(final String pingUrl) {
        this.pingUrl = pingUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String[] getEvents() {
        return events;
    }

    public void setEvents(final String[] events) {
        this.events = events;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public GitHubWebhookConfig getConfig() {
        return config;
    }

    public void setConfig(GitHubWebhookConfig config) {
        this.config = config;
    }

}
