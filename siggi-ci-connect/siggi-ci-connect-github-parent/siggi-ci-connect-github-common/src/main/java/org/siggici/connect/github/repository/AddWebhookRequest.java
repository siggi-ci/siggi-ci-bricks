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

/**
 * @author  jbellmann
 */
public class AddWebhookRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name = "web";

    private boolean active = true;

    private String[] events = new String[] {"push"};

    private AddWebhookConfig config = new AddWebhookConfig();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String[] getEvents() {
        return events;
    }

    public void setEvents(final String[] events) {
        this.events = events;
    }

    public AddWebhookConfig getConfig() {
        return config;
    }

    public void setConfig(final AddWebhookConfig config) {
        this.config = config;
    }

}
