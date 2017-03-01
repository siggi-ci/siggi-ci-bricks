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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SiggiYaml { 

    // TODO, java as default?
    private String language = "java";

    // start lifecycle

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("before_install")
    private List<String> beforeInstall = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> install = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("before_script")
    private List<String> beforeScript = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> script = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("after_success")
    private List<String> afterSuccess = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("after_failure")
    private List<String> afterFailure = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("after_script")
    private List<String> afterScript = new ArrayList<>();

    // end lifecycle

    // general
    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> jdk = asList(new String[]{"openjdk8"});

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    @JsonProperty("node_js")
    private List<String> nodeJs = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> go = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> before = new ArrayList<>();

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> services = new ArrayList<>();

    private Box box = new Box();

    private Notifications notifications;

    @JsonProperty("env")
    private Environment environment = new Environment();

    private Branches branches = new Branches();
}
