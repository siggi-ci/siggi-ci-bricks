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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlTests {

    @Test
    public void readYaml() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(Paths.get(getClass().getResource("/siggi-simple.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getLanguage()).isEqualTo("java");
    }

    @Test
    public void readYamlSinglfffeJdk() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(Paths.get(getClass().getResource("/siggi-jdk.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8");
    }

    @Test
    public void readYamlServices() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-jdk-services.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8");
        Assertions.assertThat(siggiYamlConfig.getServices()).contains("cassandra", "postgres", "redis-server");
    }

    @Test
    public void readYamlAfterSuccess() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-jdk-services-afterSuccess.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8");
        Assertions.assertThat(siggiYamlConfig.getServices()).contains("cassandra", "postgres", "redis-server");

        Assertions.assertThat(siggiYamlConfig.getAfterSuccess()).contains("mvn clean", "mvn test");
    }

    @Test
    public void readYamlAfterFailure() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-jdk-services-afterFailure.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8");
        Assertions.assertThat(siggiYamlConfig.getServices()).contains("cassandra", "postgres", "redis-server");

        Assertions.assertThat(siggiYamlConfig.getAfterFailure()).contains("mvn clean", "mvn test");
    }

    @Test
    public void readYamlGo() throws IOException, URISyntaxException {

        SiggiYaml siggiYamlConfig = SiggiYamlMapper.map(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-go.yml").toURI())));

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getGo()).contains("1.2", "1.3", "1.4");
    }

    @Test
    public void readYamlNodeJs() throws JsonProcessingException, IOException, URISyntaxException {

        SiggiYaml siggiYamlConfig = SiggiYamlMapper.map(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-node_js.yml").toURI())));

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getNodeJs()).contains("0.12", "0.11", "0.10");
    }

    @Test
    public void readYamlMultiJdk() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
    }

    @Test
    public void readYamlMultiJdkWithSingleBefore() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk-before.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install");
    }

    @Test
    public void readYamlMultiJdkWithMultiBefore() throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk-multi-before.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install", "mvn jacoco:report");
    }

    @Test
    public void readYamlMultiJdkWithMultiBeforeSingleScript() throws JsonProcessingException, IOException,
        URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk-multi-before-script.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install", "mvn jacoco:report");
        Assertions.assertThat(siggiYamlConfig.getScript()).contains("mvn test");
        Assertions.assertThat(siggiYamlConfig.getAfterFailure()).isEmpty();
    }

    @Test
    public void readYamlMultiJdkWithMultiBeforeMultiScript() throws JsonProcessingException, IOException,
        URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk-multi-before-multi-script.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install", "mvn jacoco:report");
        Assertions.assertThat(siggiYamlConfig.getScript()).contains("mvn test", "another command");
        Assertions.assertThat(siggiYamlConfig.getAfterFailure()).isEmpty();
    }

    @Test
    public void readYamlMultiJdkWithMultiBeforeMultiScriptBox() throws JsonProcessingException, IOException,
        URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(getClass().getResource("/siggi-multi-jdk-multi-before-multi-script-box.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install", "mvn jacoco:report");
        Assertions.assertThat(siggiYamlConfig.getScript()).contains("mvn test", "another command");
        Assertions.assertThat(siggiYamlConfig.getAfterFailure()).isEmpty();
        Assertions.assertThat(siggiYamlConfig.getBox()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getBox().getType()).isEqualTo("dck");
//        Assertions.assertThat(siggiYamlConfig.getBox().getIdentifier()).isEqualTo("sigmalab/siggi-base:1");
//        Assertions.assertThat(siggiYamlConfig.getBox().getSize()).isEqualTo(4);
        Assertions.assertThat(siggiYamlConfig.getBox().getParams()).isNotEmpty();
        Assertions.assertThat(siggiYamlConfig.getBox().getParams()).containsKeys("dck.image", "dck.mem", "dck.user");
    }

    @Test
    public void readYamlMultiJdkWithMultiBeforeMultiScriptBoxEnv() throws JsonProcessingException, IOException,
        URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(
                        getClass().getResource("/siggi-multi-jdk-multi-before-multi-script-box-env.yml").toURI())));

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);

        Assertions.assertThat(siggiYamlConfig).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getJdk()).contains("oraclejdk8", "oraclejdk7", "openjdk6");
        Assertions.assertThat(siggiYamlConfig.getBefore()).contains("mvn clean install", "mvn jacoco:report");
        Assertions.assertThat(siggiYamlConfig.getScript()).contains("mvn test", "another command");
        Assertions.assertThat(siggiYamlConfig.getAfterFailure()).isEmpty();
        Assertions.assertThat(siggiYamlConfig.getBox()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getBox().getType()).isEqualTo("dck");
//        Assertions.assertThat(siggiYamlConfig.getBox().getIdentifier()).isEqualTo("sigmalab/siggi-base:1");
//        Assertions.assertThat(siggiYamlConfig.getBox().getSize()).isEqualTo(4);
        Assertions.assertThat(siggiYamlConfig.getEnvironment()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getEnvironment().getMatrix()).contains("OPTS=12", "OPTS=14", "OPTS=16");
        Assertions.assertThat(siggiYamlConfig.getEnvironment().getGlobal()).contains("aValue");
    }
}
