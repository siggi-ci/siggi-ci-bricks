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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.security.SpringSocialConfigurer;

public class DefaultStandaloneSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Configuration
    @Order(1)
    public static class HooksSecurityConfiguration extends WebSecurityConfigurerAdapter {

        //@formatter:off
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .requestMatcher(new AntPathRequestMatcher("/hooks", "POST"))
                    .authorizeRequests()
                        .anyRequest().hasRole("HOOK")
            .and()
                .httpBasic()
                .and()
            .csrf()
                .disable();
        }
        // @formatter:on

    }

    @Configuration
    @Order(5)
    public static class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

        //@formatter:off
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?param.error=bad_credentials").permitAll()
                .and()
                    .logout().logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                        .permitAll()
                .and()
                    .authorizeRequests()
                        .antMatchers("/favicon.ico", "/static-resources/**", "/css/**", "/js/**").permitAll()
                        .antMatchers("/**").authenticated()
                .and()
                    .rememberMe()
                .and()
                    .apply(new SpringSocialConfigurer())
                .and()
                    .csrf().disable();
        }
        //@formatter:on
    }
}
