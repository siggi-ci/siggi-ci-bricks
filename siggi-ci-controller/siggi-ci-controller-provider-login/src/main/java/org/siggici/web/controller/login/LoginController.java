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
package org.siggici.web.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple Login-Controller exposing 'provider' and 'scope' as model-attributes.
 * 
 * @author jbellmann
 *
 */
@Controller
public class LoginController {

    public static final String LOGINSCOPE = "loginscope";
    public static final String LOGINPROVIDER = "loginprovider";

    private final LoginControllerProperties properties;

    public LoginController(LoginControllerProperties loginControllerProperties) {
        this.properties = loginControllerProperties;
    }

    @GetMapping("/login")
    public String loginView(Model model) {
        model.addAttribute(LOGINPROVIDER, properties.getProvider());
        model.addAttribute(LOGINSCOPE, properties.getScope());
        return properties.getLoginViewName();
    }

}
