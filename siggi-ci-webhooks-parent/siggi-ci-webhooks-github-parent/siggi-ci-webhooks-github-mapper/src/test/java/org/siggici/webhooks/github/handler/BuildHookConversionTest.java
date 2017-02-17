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
package org.siggici.webhooks.github.handler;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.siggici.data.builds.BuildHook;
import org.siggici.webhooks.github.PullRequestPayload;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.google.common.collect.Sets;

public class BuildHookConversionTest {

    private Set<Converter<?, ?>> converters;
    private ConversionService conversionService;

    @Before
    public void setUp() {
        converters = Sets.newHashSet(new GithubPayloadToBuildHookConverter());
        conversionService = conversionService();
    }

    @Test
    public void convertToBuildHook() {
        BuildHook bh = conversionService.convert(new PullRequestPayload(), BuildHook.class);
        Assertions.assertThat(bh).isNotNull();
    }

    protected ConversionService conversionService() {
        ConversionServiceFactoryBean fb = new ConversionServiceFactoryBean();
        fb.setConverters(converters);
        fb.afterPropertiesSet();
        return fb.getObject();
    }

    static class GithubPayloadToBuildHookConverter implements Converter<PullRequestPayload, BuildHook> {

        @Override
        public BuildHook convert(PullRequestPayload source) {
            return new BuildHook();
        }
    }
}
