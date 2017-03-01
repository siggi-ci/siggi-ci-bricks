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

public class YamlWithHipchatNotificationsTests {

    @Test
    public void readYamlMultiJdkWithMultiBeforeMultiScriptBoxEnvAndNotificationsViaWebhook()
        throws JsonProcessingException, IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(
                        getClass().getResource(
                            "/siggi-multi-jdk-multi-before-multi-script-box-env-notifications-hipchat.yml").toURI())));

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

        Assertions.assertThat(siggiYamlConfig.getNotifications()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getNotifications().getHipchat()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getNotifications().getHipchat().isNotify()).isFalse();

        Assertions.assertThat(siggiYamlConfig.getNotifications().getHipchat().getOnFailure()).isEqualTo("always");
    }

    @Test
    public void readYamlMultiJdkWithMultiBeforeMultiScriptBoxEnvAndNotificationViaMail() throws JsonProcessingException,
        IOException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        rootNode = mapper.readTree(Files.readAllBytes(
                    Paths.get(
                        getClass().getResource(
                            "/siggi-multi-jdk-multi-before-multi-script-box-env-notifications-email.yml").toURI())));

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

        Assertions.assertThat(siggiYamlConfig.getNotifications()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getNotifications().getEmail()).isNotNull();
        Assertions.assertThat(siggiYamlConfig.getNotifications().getEmail().getRecipients()).contains("me@test.de",
            "you@yourcompany.com");

        Assertions.assertThat(siggiYamlConfig.getNotifications().getEmail().getOnFailure()).isEqualTo("always");
    }

}
