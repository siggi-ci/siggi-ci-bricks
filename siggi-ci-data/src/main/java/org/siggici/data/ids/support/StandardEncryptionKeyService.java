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
package org.siggici.data.ids.support;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.siggici.data.ids.EncryptionKey;
import org.siggici.data.ids.EncryptionKeyService;
import org.springframework.util.Assert;

/**
 * Using a {@link SecureRandom} to create some random {@link EncryptionKey}s.
 * 
 * @author jbellmann
 *
 */
public final class StandardEncryptionKeyService implements EncryptionKeyService {
    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    private final List<Integer> VALID_PWD_CHARS = new ArrayList<>();
    private final int passwordLength;
    private final SecureRandom secureRandom;

    public StandardEncryptionKeyService() {
        this(DEFAULT_PASSWORD_LENGTH);
    }

    public StandardEncryptionKeyService(int passwordLength) {
        Assert.isTrue(4 < passwordLength, "'passwordlength' should never be less than 5");
        this.passwordLength = passwordLength;
        this.secureRandom = new SecureRandom();
        IntStream.rangeClosed('0', '9').forEach(VALID_PWD_CHARS::add); // 0-9
        IntStream.rangeClosed('A', 'Z').forEach(VALID_PWD_CHARS::add); // A-Z
        IntStream.rangeClosed('a', 'z').forEach(VALID_PWD_CHARS::add); // a-z
    }

    @Override
    public EncryptionKey create() {
        return EncryptionKey.builder().encryptionKey(getRandomValue()).build();
    }

    protected String getRandomValue() {
        List<String> parts = new ArrayList<>();
        secureRandom.ints(passwordLength, 0, VALID_PWD_CHARS.size()).map(VALID_PWD_CHARS::get)
                .forEach(s -> parts.add(Character.valueOf((char) s).toString()));
        return String.join("", parts);
    }

}
