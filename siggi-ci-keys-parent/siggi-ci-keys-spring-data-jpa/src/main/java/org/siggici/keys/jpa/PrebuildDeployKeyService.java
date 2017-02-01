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
package org.siggici.keys.jpa;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.siggici.keys.DeployKey;
import org.siggici.keys.DeployKeyService;
import org.siggici.keys.jpa.autoconfigure.DeployKeyServiceProperties;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrebuildDeployKeyService implements DeployKeyService {

    private final DeployKeyService delegate;
    private final BlockingQueue<DeployKey> deployKeyQueue;
    private final DeployKeyServiceProperties props;

    public PrebuildDeployKeyService(DeployKeyService delegate, DeployKeyServiceProperties props) {
        this.delegate = delegate;
        this.deployKeyQueue = new ArrayBlockingQueue<>(props.getPreprocessingCount());
        this.props = props;
    }

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 3 * 1000)
    public void buildKey() {
        if (deployKeyQueue.size() < props.getPreprocessingCount()) {
            log.debug("create an deploy-key ...");
            DeployKey k = create();
            deployKeyQueue.add(k);
            log.debug("... deploy-key created");
        } else {
            log.debug("skip deploy-key creation");
        }
    }

    @Override
    public DeployKey create() {
        DeployKey dk = deployKeyQueue.poll();
        if (dk != null) {
            return dk;
        } else {
            return delegate.create();
        }
    }
}
