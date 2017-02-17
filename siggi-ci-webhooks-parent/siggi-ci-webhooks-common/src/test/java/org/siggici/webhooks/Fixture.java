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

import com.google.common.collect.Maps;

public class Fixture {

    public static final String PULL_REQUEST = "pull_request";
    public static final String GITHUB = "github";
    public static final String SOME_PAYLOAD = "SomePayload";

    public static HookPayloadEvent create(Map<String, String> rawData) {
        return HookPayloadEvent.build(rawData);
    }

    public static HookPayloadEvent create() {
        Map<String, String> rawData = Maps.newConcurrentMap();
        rawData.put("created", "1");
        rawData.put("eventType", PULL_REQUEST);
        rawData.put("providerType", GITHUB);
        rawData.put("payload", SOME_PAYLOAD);
        return create(rawData);
    }
}
