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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.siggici.builddefinition.standard.SiggiYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlBranchesTest {

    @Test
    public void readYamlBranchesOnly()
            throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper
                .readTree(Files.readAllBytes(Paths.get(getClass().getResource("/siggi-branches-only.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getBranches().getOnly()).contains("master", "feature-234");
    }

    @Test
    public void readYamlBranchesExcept()
            throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper
                .readTree(Files.readAllBytes(Paths.get(getClass().getResource("/siggi-branches-except.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getBranches().getExcept()).contains("master", "feature-234");
    }

    @Test
    public void readYamlBranchesBoth()
            throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper
                .readTree(Files.readAllBytes(Paths.get(getClass().getResource("/siggi-branches-both.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getBranches().getOnly()).contains("master", "feature-234");
        Assertions.assertThat(siggiYamlConfig.getBranches().getExcept()).contains("master", "feature-234");
    }

}
