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
package org.siggici.data.builds;

import static com.google.common.collect.ImmutableSortedSet.copyOf;
import static com.google.common.collect.Iterables.transform;

import java.util.List;

import org.springframework.util.Assert;

import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * {@link BuildSlot} states functions.
 *
 * @author jbellmann
 */
public class BuildStates {

    public static final String FINNISHED = "FINNISHED";

    public static final String RUNNING = "RUNNING";

    public static final String CREATED = "CREATED";

    public static final String ENQUEUED = "ENQUEUED";

    public static final String FAILED = "FAILED";

    public static final String SUCCESS = "SUCCESS";

    private static Integer ZERO = new Integer(0);

    private static Long MINUS_ONE = new Long(-1);

    private static BiMap<Integer, String> statesBiMap = new ImmutableBiMap.Builder<Integer, String>().put(0, CREATED)
            .put(1, ENQUEUED).put(2, RUNNING).put(4, FINNISHED).put(6, FAILED).put(8, SUCCESS).build();

    private static final Function<BuildSlot, Integer> extractOrder = new BuildSlotStateToOrderFunction();

    private static final Function<BuildSlot, Long> startTimeExtractor = new StartTimeExtractor();

    private static final Function<BuildSlot, Long> endTimeExctractor = new EndTimeExtractor();

    public static String getLowestState(final List<BuildSlot> buildSlots) {
        return statesBiMap.get(getLowestStateAsInteger(buildSlots));
    }

    public static Integer getLowestStateAsInteger(final List<BuildSlot> buildSlots) {
        Assert.notNull(buildSlots, "List of 'BuildSlots'should never be null");
        if (buildSlots.isEmpty()) {
            return ZERO;
        }

        Iterable<Integer> extracted = transform(buildSlots, extractOrder);
        ImmutableSortedSet<Integer> asSet = copyOf(extracted);
        return asSet.first();
    }

    public static String getHighestState(final List<BuildSlot> buildSlots) {
        return statesBiMap.get(getHighestStateAsInteger(buildSlots));
    }

    public static Integer getHighestStateAsInteger(final List<BuildSlot> buildSlots) {
        Assert.notNull(buildSlots, "List of 'BuildSlots'should never be null");
        if (buildSlots.isEmpty()) {
            return ZERO;
        }

        Iterable<Integer> extracted = transform(buildSlots, extractOrder);
        ImmutableSortedSet<Integer> asSet = copyOf(extracted);
        return asSet.last();
    }

    public static Long getLowestStartTime(final List<BuildSlot> buildSlots) {
        Assert.notNull(buildSlots, "List of 'BuildSlots'should never be null");
        if (buildSlots.isEmpty()) {
            return MINUS_ONE;
        }

        Iterable<Long> extracted = transform(buildSlots, startTimeExtractor);
        ImmutableSortedSet<Long> asSet = copyOf(extracted);
        return asSet.first();
    }

    public static Long getHighestEndTime(final List<BuildSlot> buildSlots) {
        Assert.notNull(buildSlots, "List of 'BuildSlots'should never be null");
        if (buildSlots.isEmpty()) {
            return MINUS_ONE;
        }

        Iterable<Long> extracted = transform(buildSlots, endTimeExctractor);
        ImmutableSortedSet<Long> asSet = copyOf(extracted);
        return asSet.last();
    }

    protected static class BuildSlotStateToOrderFunction implements Function<BuildSlot, Integer> {
        @Override
        public Integer apply(final BuildSlot input) {
            return statesBiMap.inverse().get(input.getState());
        }
    }

    protected static class StartTimeExtractor implements Function<BuildSlot, Long> {
        @Override
        public Long apply(final BuildSlot input) {
            return input.getClock().getStartTime();
        }
    }

    protected static class EndTimeExtractor implements Function<BuildSlot, Long> {
        @Override
        public Long apply(final BuildSlot input) {
            return input.getClock().getEndTime();
        }
    }
}