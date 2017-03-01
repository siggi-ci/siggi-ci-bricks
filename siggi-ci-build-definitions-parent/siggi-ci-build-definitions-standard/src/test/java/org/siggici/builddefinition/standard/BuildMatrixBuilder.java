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
import java.util.concurrent.atomic.AtomicInteger;

import javaslang.API;
import javaslang.Predicates;

public class BuildMatrixBuilder {

    public List<BuildConfig> createBuilds(final SiggiYaml siggiYaml) {
        List<BuildConfig> result = API.Match(siggiYaml.getLanguage()).of(
                API.Case(Predicates.isIn("java", "clojure", "groovy", "scala"), build(siggiYaml.getJdk(), siggiYaml.getEnvironment().getMatrix())),
                API.Case(Predicates.isIn("node_js"), build(siggiYaml.getNodeJs(), siggiYaml.getEnvironment().getMatrix())),
                API.Case(Predicates.isIn("go"), build(siggiYaml.getGo(), siggiYaml.getEnvironment().getMatrix())),
                API.Case(API.$(), new ArrayList<>())
        );

        return result;
    }

    protected List<BuildConfig> build(final List<String> first, final List<String> environmentMatrix) {
        List<BuildConfig> result = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger(0);
        for (String jdk : first) {
            if (environmentMatrix.size() > 0) {
                for (String m : environmentMatrix) {
                    result.add(new BuildConfig(counter.incrementAndGet(), jdk, m));
                }
            } else {
                result.add(new BuildConfig(counter.incrementAndGet(), jdk, ""));
            }
        }

        return result;
    }

}
