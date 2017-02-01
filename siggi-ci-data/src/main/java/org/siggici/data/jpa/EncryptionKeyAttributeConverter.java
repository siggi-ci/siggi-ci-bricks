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
package org.siggici.data.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.EntityListeners;

import org.siggici.data.ids.EncryptionKey;

/**
 * Converts {@link EncryptionKey} from/to {@link String} during save/load
 * to/from DB.<br/>
 * TODO think about encrypt the 'key' itself during save/load into DB with
 * {@link EntityListeners}.
 * 
 * @author jbellmann
 *
 */
@Converter(autoApply = true)
public class EncryptionKeyAttributeConverter implements AttributeConverter<EncryptionKey, String> {

    @Override
    public String convertToDatabaseColumn(EncryptionKey attribute) {
        return attribute.getAsString();
    }

    @Override
    public EncryptionKey convertToEntityAttribute(String dbData) {
        return EncryptionKey.builder().encryptionKey(dbData).build();
    }

}
