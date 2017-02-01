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
package org.siggici.services.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.util.Assert;

/**
 * Service that genrates some random-strings with {@link SecureRandom}.
 * 
 * @author jbellmann
 *
 */
public class RandomStringService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final List<Integer> VALID_PWD_CHARS = new ArrayList<>();

    public RandomStringService() {
        IntStream.rangeClosed('0', '9').forEach(VALID_PWD_CHARS::add); // 0-9
        IntStream.rangeClosed('A', 'Z').forEach(VALID_PWD_CHARS::add); // A-Z
        IntStream.rangeClosed('a', 'z').forEach(VALID_PWD_CHARS::add); // a-z
    }

    public String getRandomString(int length) {
        Assert.isTrue(4 < length, "'passwordlength' should never be less than 5");
        List<String> parts = new ArrayList<>();
        secureRandom.ints(length, 0, VALID_PWD_CHARS.size()).map(VALID_PWD_CHARS::get)
                .forEach(s -> parts.add(Character.valueOf((char) s).toString()));
        return String.join("", parts);
    }

    public String getRandomUsername() {
        return getRandomString(8);
    }

    public String getRandomPassword() {
        return getRandomString(16);
    }
}
