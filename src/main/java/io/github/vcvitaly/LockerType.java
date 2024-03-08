package io.github.vcvitaly;

public enum LockerType {
    SIMPLE_HASH,
    SYNCHRONIZED,
    CONCURRENT,
    GUAVA_CACHE,
    STRIPED;

    public static LockerType of(String s) {
        return valueOf(s.toUpperCase());
    }
}
