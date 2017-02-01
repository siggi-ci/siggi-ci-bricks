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
package org.siggici.data.projects;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.siggici.data.ids.DeployKeyRef;

public class DeployKeyRefTest {

    @Test
    public void createWithBuilder() {
        String refString = UUID.randomUUID().toString();
        DeployKeyRef ref = DeployKeyRef.builder().deployKeyReference(refString).build();
        Assertions.assertThat(ref).isNotNull();
        Assertions.assertThat(ref.getAsString()).isEqualTo(refString);
    }
}
