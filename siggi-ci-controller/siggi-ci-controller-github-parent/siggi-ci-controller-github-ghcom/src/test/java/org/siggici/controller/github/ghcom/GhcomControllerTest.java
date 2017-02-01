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
package org.siggici.controller.github.ghcom;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;
import static org.siggici.controller.github.ghcom.GhcomMappings.ORGANIZATIONS;
import static org.siggici.controller.github.ghcom.GhcomMappings.USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.siggici.connect.github.Github;
import org.siggici.connect.github.ghcom.api.Ghcom;
import org.siggici.services.common.Organization;
import org.siggici.services.common.User;
import org.siggici.services.github.GithubExecutor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
public class GhcomControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private Ghcom ghcom;

	@MockBean
	private GithubExecutor githubExecutor;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new GhcomController(ghcom, githubExecutor))
				.alwaysDo(MockMvcResultHandlers.print()).build();
	}

	@Test
	public void testGetUser() throws Exception {
		when(githubExecutor.getUser(Mockito.any(Github.class))).thenReturn(new User("klaus"));

		mockMvc.perform(get(USER)).andExpect(status().isOk());

		verify(githubExecutor, atLeastOnce()).getUser(Mockito.any(Github.class));
	}

	@Test
	public void testGetOrgas() throws Exception {
		when(githubExecutor.getOrganizations(Mockito.any(Github.class))).thenReturn(new ArrayList<Organization>());

		mockMvc.perform(get(ORGANIZATIONS)).andExpect(status().isOk());

		verify(githubExecutor, atLeastOnce()).getOrganizations(Mockito.any(Github.class));
	}

}
