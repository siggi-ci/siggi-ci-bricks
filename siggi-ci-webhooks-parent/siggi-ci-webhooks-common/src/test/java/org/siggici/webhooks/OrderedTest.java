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
package org.siggici.webhooks;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import com.google.common.collect.Lists;

public class OrderedTest {

    @Test
    public void testPluginOrder() {
        List<Object> plugins = Lists.newArrayList(new FirstPlugin(), new SecondPlugin());
        plugins.sort(new AnnotationAwareOrderComparator());
        assertThat(plugins.get(0)).isInstanceOf(SecondPlugin.class);
    }

    @Order(12)
    static class FirstPlugin {
    }

    static class SecondPlugin implements Ordered {

        @Override
        public int getOrder() {
            return 8;
        }

    }

}
