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
package org.siggici.data.builds;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.siggici.data.jpa.RepoId;

public class RepoIdTest {
    
    @Test
    public void testRepoIdCreation(){
        RepoId repoId = RepoId.builder().provider("ghcom").orga("orga1").repo("repo1").build();
        Assertions.assertThat(repoId).isNotNull();
        Assertions.assertThat(repoId.getProvider()).isEqualTo("ghcom");
        Assertions.assertThat(repoId.getOrga()).isEqualTo("orga1");
        Assertions.assertThat(repoId.getRepo()).isEqualTo("repo1");
    }

}
