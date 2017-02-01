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

public interface FakeMappings {

	static String UI_PROVIDER = "/ui/providers/fake";
	static String ORGANIZATIONS = UI_PROVIDER + "/orgas";
	static String USER = UI_PROVIDER + "/user";
	static String USER_REPOSITORIES = UI_PROVIDER + "/userrepos";
	static String ORGA_REPOSITORIES = UI_PROVIDER + "/repos/{orga}";

}
