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

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Maps the content of an 'siggi.yml' to an {@link SiggiYaml} object.
 *
 * @author  jbellmann
 */
public class SiggiYamlMapper {

    private static ObjectMapper objectMapper;

    protected static ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(new YAMLFactory());
        }

        return objectMapper;
    }

    public static SiggiYaml map(final String string) {
        Assert.hasText(string, "Siggi-Yaml file content should never be null or empty");
        return map(string.getBytes());
    }

    public static SiggiYaml map(final byte[] content) {
        try {
            JsonNode rootNode = getMapper().readTree(content);
            return getMapper().convertValue(rootNode, SiggiYaml.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
