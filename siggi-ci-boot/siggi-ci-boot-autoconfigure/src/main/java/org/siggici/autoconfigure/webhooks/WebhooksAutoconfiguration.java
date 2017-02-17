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
package org.siggici.autoconfigure.webhooks;

import java.util.Set;

import org.siggici.webhooks.HookPayloadEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.plugin.core.Plugin;
import org.springframework.plugin.core.config.EnablePluginRegistries;

/**
 * Autoconfiguration to enable {@link HookPayloadEventHandler}s implemented as
 * {@link Plugin}s.
 * 
 * @author jbellmann
 *
 */
@Configuration
@EnablePluginRegistries({ HookPayloadEventHandler.class })
@ComponentScan({"org.siggici.webhooks"})
public class WebhooksAutoconfiguration {

    @Bean
    public ConversionService customConversionService(Set<Converter<?, ?>> converters) {
        ConversionServiceFactoryBean fb = new ConversionServiceFactoryBean();
        fb.setConverters(converters);
        fb.afterPropertiesSet();
        return fb.getObject();
    }

}
