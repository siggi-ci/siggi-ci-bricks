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
package org.siggici.connect.github.pagination;

import org.springframework.util.Assert;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Filter for {@link Iterables} of {@link LinkRelation}
 * 
 * @author jbellmann
 *
 */
class RelationsPredicate implements Predicate<LinkRelation> {

	private final String relation;

	public RelationsPredicate(String relation) {
		Assert.notNull(relation, "'relation' should never be null");
		Assert.hasText(relation, "'relation' should never be empty");
		this.relation = relation;
	}

	@Override
	public boolean apply(LinkRelation input) {
		return relation.equals(input.getRelation());
	}

}