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

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author  jbellmann
 */
public class SingleValueAsListDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {

    private static final long serialVersionUID = 1L;

    private final BeanProperty property;

    /**
     * Default constructor needed by Jackson to be able to call 'createContextual'. Beware, that the object created here
     * will cause a NPE when used for deserializing!
     */
    public SingleValueAsListDeserializer() {
        super(Collection.class);
        this.property = null;
    }

    /**
     * Constructor for the actual object to be used for deserializing.
     *
     * @param  property  this is the property/field which is to be serialized
     */
    private SingleValueAsListDeserializer(final BeanProperty property) {
        super(property.getType());
        this.property = property;
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext ctxt, final BeanProperty property)
        throws JsonMappingException {
        return new SingleValueAsListDeserializer(property);
    }

    @Override
    public Object deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
        switch (jp.getCurrentToken()) {

            case VALUE_STRING :

                // value is a string but we want it to be something else: unescape the string and convert it
// return new ObjectMapper().readValue(jp.getText(), property.getType());
// String value = jp.getText();
                return asList(new String[]{jp.getText()});

            default :

                // continue as normal: find the correct deserializer for the type and call it
                return ctxt.findContextualValueDeserializer(property.getType(), property).deserialize(jp, ctxt);
        }
    }
}
