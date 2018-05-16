package com.pomkine.domain.common;

import lombok.Getter;

public class Aggregate<EventType> {

    @Getter
    private AggregateId id;

}
