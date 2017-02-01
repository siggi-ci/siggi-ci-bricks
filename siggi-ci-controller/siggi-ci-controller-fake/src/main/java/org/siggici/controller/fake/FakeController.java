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
package org.siggici.controller.fake;

import static org.siggici.controller.fake.FakeMappings.ORGANIZATIONS;
import static org.siggici.controller.fake.FakeMappings.ORGA_REPOSITORIES;
import static org.siggici.controller.fake.FakeMappings.USER;
import static org.siggici.controller.fake.FakeMappings.USER_REPOSITORIES;

import java.util.Arrays;
import java.util.List;

import org.siggici.services.common.Organization;
import org.siggici.services.common.RepositoryInfo;
import org.siggici.services.common.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FakeController {

	private static final String HOOKS = "https://github.com/siggi-ci/hooks";

    @GetMapping(USER)
	User getUser() {
		return new User("haribo");
	}

	@GetMapping(ORGANIZATIONS)
	List<Organization> getOrganizations() {
		return Arrays.asList(new Organization("Another"), new Organization("PowerLab-51"),
				new Organization("SmallCompany"), new Organization("FrickelBude-2"));
	}

	@GetMapping(USER_REPOSITORIES)
	public List<RepositoryInfo> getUserRepos() {
		String username = getUser().getName();
		return Arrays.asList(new RepositoryInfo(true, "fake", username, "repo1", true, HOOKS),
				new RepositoryInfo(false, "fake", username, "repo2", false, HOOKS),
				new RepositoryInfo(false, "fake", username, "repo3", true, HOOKS),
				new RepositoryInfo(true, "fake", username, "repo4", false, HOOKS));
	}

	@GetMapping(ORGA_REPOSITORIES)
	List<RepositoryInfo> getRepositories(@PathVariable String orga) {
		return Arrays.asList(new RepositoryInfo(true, "fake", orga, "repo1", false, HOOKS),
				new RepositoryInfo(false, "fake", orga, "repo2", false, HOOKS), new RepositoryInfo(false, "fake", orga, "repo3", true, HOOKS),
				new RepositoryInfo(true, "fake", orga, "repo4", true, HOOKS));
	}

	@PostMapping("enabled")
	RepositoryInfo enableRepository(RepositoryInfo repoInfo) {
		log.info("Got repoInfo to disable: {}", repoInfo);
		// TODO, if we have one in DB and it's enabled -> disable it
		repoInfo.setEnabled(false);
		return repoInfo;
	}

	@PostMapping("disabled")
	RepositoryInfo disableRepository(RepositoryInfo repoInfo) {
		log.info("Got repoInfo to enable: {}", repoInfo);
		// TODO, if we have one in DB and it's disabled -> enable it
		// or create keys, webhook and store in DB, enabled
		repoInfo.setEnabled(true);
		return repoInfo;
	}

}
