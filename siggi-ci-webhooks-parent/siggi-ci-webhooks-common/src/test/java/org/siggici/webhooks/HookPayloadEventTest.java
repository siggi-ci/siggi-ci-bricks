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

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class HookPayloadEventTest {

    @Test
    public void create() {

        HookPayloadEvent event = Fixture.create();
        Assertions.assertThat(event).isNotNull();
        Assertions.assertThat(event.getRawPayload()).isEqualTo(Fixture.SOME_PAYLOAD);
        Assertions.assertThat(event.getEventType()).isEqualTo(Fixture.PULL_REQUEST);
        Assertions.assertThat(event.getProviderType()).isEqualTo(Fixture.GITHUB);
        Assertions.assertThat(event.getCreated()).isEqualTo(1);
    }

}
