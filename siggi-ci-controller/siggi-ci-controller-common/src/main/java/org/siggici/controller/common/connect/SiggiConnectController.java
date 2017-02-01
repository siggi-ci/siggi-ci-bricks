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

import static org.springframework.util.StringUtils.hasText;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

public class SiggiConnectController extends ConnectController {

    protected ConnectControllerProperties connectControllerProperties;

    public SiggiConnectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository, ConnectControllerProperties connectControllerProperties) {

        super(connectionFactoryLocator, connectionRepository);
        this.connectControllerProperties = connectControllerProperties;
    }

    @Override
    protected String connectedView(String providerId) {
        StringBuilder sb = new StringBuilder();
        sb.append(connectControllerProperties.getConnectedViewPrefix());
        sb.append(providerId);
        sb.append(connectControllerProperties.getConnectedViewSuffix());
        return sb.toString();
    }

    @Override
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public String connectionStatus(@PathVariable String providerId, NativeWebRequest request, Model model) {
        model.addAttribute("providerScope", connectControllerProperties.getScopes().get(providerId));
        String providerDescription = connectControllerProperties.getProviderDescriptions().get(providerId);
        if (!hasText(providerDescription)) {
            providerDescription = ConnectControllerProperties.DEFAULT_PROVIDER_DESCRIPTION;
        }
        model.addAttribute("providerDescription", providerDescription);
        model.addAttribute("providerId", providerId);
        return super.connectionStatus(providerId, request, model);
    }

    @Override
    protected String connectView(String providerId) {
        return connectControllerProperties.getConnectViewName();
    }

}
