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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

//@Ignore
public class BuildMatrixBuildTests {

    @Test
    public void createMatrixBuild() throws JsonProcessingException, IOException, URISyntaxException {
        SiggiYaml siggiYaml = getSiggiYaml("/siggi-matrix.yml");

        List<BuildConfig> result = new BuildMatrixBuilder().createBuilds(siggiYaml);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(9);
        System.out.println(result.toString());
    }

    @Test
    public void createMatrixBuildSpringDataJpa() throws JsonProcessingException, IOException, URISyntaxException {
        SiggiYaml siggiYaml = getSiggiYaml("/siggi-matrix-spring-data-jpa.yml");

        List<BuildConfig> result = new BuildMatrixBuilder().createBuilds(siggiYaml);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(9);
        System.out.println(result.toString());
    }

    @Test
    public void createMatrixBuild_3Jdk_2Envs() throws JsonProcessingException, IOException, URISyntaxException {
        SiggiYaml siggiYaml = getSiggiYaml("/siggi-matrix-3jdk_2env.yml");

        List<BuildConfig> result = new BuildMatrixBuilder().createBuilds(siggiYaml);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(6);
        System.out.println(result.toString());
    }

    @Test
    public void createMatrixBuild_3Jdk_0Envs() throws JsonProcessingException, IOException, URISyntaxException {
        SiggiYaml siggiYaml = getSiggiYaml("/siggi-matrix-3jdk_0env.yml");

        List<BuildConfig> result = new BuildMatrixBuilder().createBuilds(siggiYaml);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(3);
        System.out.println(result.toString());
    }

    // not really, we have one default jdk, so we always should have matrix builds
    @Test
    public void createMatrixBuild_0Jdk_3Envs() throws JsonProcessingException, IOException, URISyntaxException {
        SiggiYaml siggiYaml = getSiggiYaml("/siggi-matrix-0jdk_3env.yml");

        List<BuildConfig> result = new BuildMatrixBuilder().createBuilds(siggiYaml);
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(3);
        System.out.println(result.toString());
    }

    protected SiggiYaml getSiggiYaml(final String resource) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(Files.readAllBytes(Paths.get(getClass().getResource(resource).toURI())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        SiggiYaml siggiYamlConfig = mapper.convertValue(rootNode, SiggiYaml.class);
        return siggiYamlConfig;
    }

}
