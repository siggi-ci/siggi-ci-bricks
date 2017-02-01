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
package org.siggici.connect.github.ghe.boot;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GhePropertiesValidator implements Validator {

	@Override
	public boolean supports(Class<?> type) {
		return type == GheProperties.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clientId", "clientId.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clientSecret", "clientSecret.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "authorizeUrl", "authorizeUrl.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accessTokenUrl", "accessTokenUrl.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "apiBaseUrl", "apiBaseUrl.empty");
		GheProperties props = (GheProperties) target;
		try {
			new URL(props.getAccessTokenUrl());
		} catch (MalformedURLException e) {
			errors.reject("accessTokenUrl", "accessTokenUrl.notUrl");
		}

		try {
			new URL(props.getApiBaseUrl());
		} catch (MalformedURLException e) {
			errors.reject("apiBaseUrl", "apiBaseUrl.notUrl");
		}

		try {
			new URL(props.getAuthorizeUrl());
		} catch (MalformedURLException e) {
			errors.reject("authorizeUrl", "authorizeUrl.notUrl");
		}
	}

}
