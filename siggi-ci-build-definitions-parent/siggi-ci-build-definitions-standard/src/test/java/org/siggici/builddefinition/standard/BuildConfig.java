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
package org.siggici.builddefinition.standard;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Strings;
import org.springframework.core.style.ToStringCreator;

/**
 * Configures exactly one build.
 *
 * @author  jbellmann
 */
public class BuildConfig {

    private int slot;

    private String jdk;

    private String nodeJs;

    private String go;

    private List<String> env = new ArrayList<>();

    public BuildConfig(final int slot, final String jdk, final String environmentItem) {
        this.slot = slot;
        this.jdk = jdk;
        if (!Strings.isNullOrEmpty(environmentItem)) {
            this.env.add(environmentItem);
        }
    }

    public BuildConfig() { }

    public String getJdk() {
        return jdk;
    }

    public void setJdk(final String jdk) {
        this.jdk = jdk;
    }

    public List<String> getEnv() {
        return env;
    }

    public void setEnv(final List<String> env) {
        this.env = env;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public String getNodeJs() {
        return nodeJs;
    }

    public void setNodeJs(final String nodeJs) {
        this.nodeJs = nodeJs;
    }

    public String getGo() {
        return go;
    }

    public void setGo(final String go) {
        this.go = go;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("slot", slot).append("jdk", jdk).append("env", env.toString()).toString();
//        return MoreObjects.toStringHelper(this).add("slot", slot).add("jdk", jdk).add("env", env.toString()).toString();
    }

}
