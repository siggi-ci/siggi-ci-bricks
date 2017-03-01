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

import java.io.IOException;

import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.siggici.builddefinition.standard.SiggiYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class SiggiLifecycleTest {

    private final String[] phases = new String[] {
        "before_install", "install", "before_script", "script", "after_failure", "after_success", "after_script"
    };

    @Test
    public void readYaml() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-lifecycle.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getLanguage()).isEqualTo("java");

        for (String lifecyclePhase : phases) {

            checkLifecyclePhase(lifecyclePhase, siggiYamlConfig);
        }
    }

    private void checkLifecyclePhase(final String lifecyclePhase, final SiggiYaml siggiYamlConfig) {
        if ("before_install".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getBeforeInstall();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("install".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getInstall();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("before_script".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getBeforeScript();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("script".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getScript();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("after_success".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getAfterSuccess();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("after_failure".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getAfterFailure();
            checkPhaseCommands(lifecyclePhase, commands);
        }

        if ("after_script".equals(lifecyclePhase)) {
            List<String> commands = siggiYamlConfig.getAfterScript();
            checkPhaseCommands(lifecyclePhase, commands);
        }
    }

    private void checkPhaseCommands(final String lifecyclePhase, final List<String> commands) {
        Assertions.assertThat(commands).isNotEmpty();
        Assertions.assertThat(commands.size()).isEqualTo(2);
        Assertions.assertThat(commands).contains(lifecyclePhase + "_one");
        Assertions.assertThat(commands).contains(lifecyclePhase + "_two");
    }
}
