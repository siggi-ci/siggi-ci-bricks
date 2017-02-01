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

import java.util.Optional;

import org.siggici.data.jpa.RepoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Some finders for {@link Build}s.
 * 
 * @author jbellmann
 *
 */
public interface BuildRepository extends PagingAndSortingRepository<Build, Long> {

    Optional<Build> findByRepoIdAndBuildnumber(RepoId repoId, long buildnumber);

    Page<Build> findByRepoId(RepoId repoId, Pageable pageable);

    Iterable<Build> findByRepoIdProviderAndRepoIdOrga(String provider, String orga);

    Page<Build> findByRepoIdOrderByBuildnumberDesc(RepoId repoId, Pageable pageable);

    Page<Build> findByRepoIdOrderByBuildnumberAsc(RepoId repoId, Pageable pageable);

    Iterable<Build> findByState(String buildState);
}
