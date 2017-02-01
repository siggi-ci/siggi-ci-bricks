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
package org.siggici.data.counter;

import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.AttributeConverter;

/**
 * To avoid 'BYTEA' data-type in PG-SQL. Easier to fix by admins in case of any
 * error.
 * 
 * @author jbellmann
 *
 */
public class CounterAtomLongConverter implements AttributeConverter<AtomicLong, String> {

    @Override
    public String convertToDatabaseColumn(AtomicLong attribute) {
        return String.valueOf(attribute.get());
    }

    @Override
    public AtomicLong convertToEntityAttribute(String dbData) {
        return new AtomicLong(Long.valueOf(dbData));
    }

}
