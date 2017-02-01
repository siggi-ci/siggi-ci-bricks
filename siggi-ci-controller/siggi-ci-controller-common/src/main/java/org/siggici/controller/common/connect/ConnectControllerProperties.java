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
package org.siggici.controller.common.connect;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "siggi.connect.controller")
@Data
public class ConnectControllerProperties {

    public static final String DEFAULT_PROVIDER_DESCRIPTION = "No description configured";

    public static final String DEFAULT_CONNECT_VIEW_NAME = "connect/siggi_default_connect";

    public static final String DEFAULT_CONNECTED_VIEW_PREFIX = "redirect:/app/repositories/";

    public static final String DEFAULT_CONNECTED_VIEW_SUFFIX = "/user";

    private String connectViewName = DEFAULT_CONNECT_VIEW_NAME;

    private String connectedViewPrefix = DEFAULT_CONNECTED_VIEW_PREFIX;

    private String connectedViewSuffix = DEFAULT_CONNECTED_VIEW_SUFFIX;

    private Map<String, String> providerDescriptions = new HashMap<String,String>();

    private Map<String, String> scopes = new HashMap<String,String>();

}
