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
package org.siggici.keys.jpa.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * @author jbellmann
 *
 */
@ConfigurationProperties(prefix = "siggi.deploykeys")
@Data
public class DeployKeyServiceProperties {

    private int type = 2; // RSA as a default
    private int size = 4096; // also possible: 1024, 2048
    private String comment = "siggi-ci-deploykey";
    private String timeStampFormat;
    private boolean enablePreprocessing = false;
    private int preprocessingCount = 10;
}
