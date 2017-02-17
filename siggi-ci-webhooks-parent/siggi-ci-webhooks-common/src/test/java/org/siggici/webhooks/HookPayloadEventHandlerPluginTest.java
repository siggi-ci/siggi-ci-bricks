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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.core.PluginRegistry;

public class HookPayloadEventHandlerPluginTest {

    private PluginRegistry<HookPayloadEventHandler, HookPayloadEvent> registry;
    private CountDownLatch cl;

    @Before
    public void setUp() {
        cl = new CountDownLatch(2);
        registry = OrderAwarePluginRegistry
                .create(newArrayList(new MyHookPayloadEventHandler(cl), new SecondHookPayloadEventHandler(cl)));
    }

    @Test
    public void test() throws InterruptedException {
        List<HookPayloadEventHandler> payloadHandler = registry.getPluginsFor(Fixture.create());
        payloadHandler.stream().forEach(h -> {
            h.handle(Fixture.create());
        });
        cl.await(3, TimeUnit.SECONDS);
        payloadHandler.stream().findFirst().ifPresent(h -> h.handle(null));;
    }

    static class MyHookPayloadEventHandler implements HookPayloadEventHandler {

        private final CountDownLatch cl;

        public MyHookPayloadEventHandler(CountDownLatch cl) {
            this.cl = cl;
        }

        @Override
        public int getOrder() {
            return 12;
        }

        @Override
        public boolean supports(HookPayloadEvent delimiter) {
            return true;
        }

        @Override
        public void handle(HookPayloadEvent event) {
            System.out.println("Handle " + getClass().getSimpleName());
            cl.countDown();
        }

    }

    static class SecondHookPayloadEventHandler implements HookPayloadEventHandler {

        private final CountDownLatch cl;

        public SecondHookPayloadEventHandler(CountDownLatch cl) {
            this.cl = cl;
        }

        @Override
        public boolean supports(HookPayloadEvent delimiter) {
            return true;
        }

        @Override
        public void handle(HookPayloadEvent event) {
            System.out.println("Handle " + getClass().getSimpleName());
            cl.countDown();
        }

    }
}
