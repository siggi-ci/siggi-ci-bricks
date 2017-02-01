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
package org.siggici.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zaas.accounts.admin")
public class AccountsAdminProperties {


    public static final String DEFAULT_ADMIN_USER_NAME = "klaus";
    public static final String DEFAULT_ADMIN_USER_PASSWORD = "YXudzr892SDuta87";
    public static final String DEFAULT_ADMIN_USER_ROLE = "ROLE_ADMIN";

    private String username = DEFAULT_ADMIN_USER_NAME;

    private String password = DEFAULT_ADMIN_USER_PASSWORD;

    private String role = DEFAULT_ADMIN_USER_ROLE;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
