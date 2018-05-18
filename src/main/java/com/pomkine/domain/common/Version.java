package com.pomkine.domain.common;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Version {

    public static final Version NONE = new Version(-1);

    private final long value;

    private Version(long value) {
        this.value = value;
    }

    public static Version of(long value) {
        assert value >= 0;
        return new Version(value);
    }

    public Version increased() {
        if (this == NONE) {
            return Version.of(1);
        }
        return Version.of(value + 1);
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}
