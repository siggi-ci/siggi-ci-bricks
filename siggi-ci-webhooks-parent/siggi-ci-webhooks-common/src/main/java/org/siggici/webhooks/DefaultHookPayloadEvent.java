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
package org.siggici.webhooks;

import java.util.Map;

import org.springframework.util.Assert;

class DefaultHookPayloadEvent implements HookPayloadEvent {

    private static final String PAYLOAD = "payload";
    private static final String CREATED = "created";
    private static final String EVENT_TYPE = "eventType";
    private static final String PROVIDER_TYPE = "providerType";
    private final Map<String, String> rawData;

    DefaultHookPayloadEvent(Map<String, String> rawData) {
        Assert.notNull(rawData, "'rawData' should never be null");
        Assert.hasText(rawData.get(EVENT_TYPE), "'eventType' should never be null or empty");
        Assert.hasText(rawData.get(PROVIDER_TYPE), "'providerType' should never be null or empty");
        Assert.hasText(rawData.get(PAYLOAD), "'payload' should never be null or empty");
        Assert.hasText(rawData.get(CREATED));
        this.rawData = rawData;
    }

    @Override
    public String getEventType() {
        return rawData.get(EVENT_TYPE);
    }

    @Override
    public String getProviderType() {
        return rawData.get(PROVIDER_TYPE);
    }

    @Override
    public long getCreated() {
        return rawData.get(CREATED) != null ? Long.valueOf(rawData.get(CREATED)) : 0L;
    }

    @Override
    public String getRawPayload() {
        return rawData.get(PAYLOAD);
    }
}
