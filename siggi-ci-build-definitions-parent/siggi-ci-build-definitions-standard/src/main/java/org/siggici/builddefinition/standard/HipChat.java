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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

/**
 * HipChat-Notifications.
 *
 * @author  jbellmann
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HipChat {

    @JsonDeserialize(using = SingleValueAsListDeserializer.class)
    private List<String> rooms = new ArrayList<>();

    private String template = "%{repository}#%{build_number} (%{branch} - %{commit} : %{author}): %{message}";

    private boolean notify = true;

    @JsonProperty("on_success")
    private String onSuccess = "always";

    @JsonProperty("on_failure")
    private String onFailure = "always";

    @JsonProperty("on_start")
    private String onStart = "always";

}
