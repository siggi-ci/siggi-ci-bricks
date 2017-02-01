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
package org.siggici.keys;

/**
 * Exception thrown when no {@link DeployKey} could be found.
 * 
 * @author jbellmann
 *
 */
public class NoDeploykeyFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String deployKeyRef;

    public NoDeploykeyFoundException(String deployKeyRef) {
        this.deployKeyRef = deployKeyRef;
    }

    @Override
    public String getMessage() {
        return String.format("No DeployKey found for id : %s", deployKeyRef);
    }

}
