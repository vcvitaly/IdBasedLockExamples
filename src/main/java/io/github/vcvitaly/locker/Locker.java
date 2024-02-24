package io.github.vcvitaly.locker;

public interface Locker<T> {

    void lock(T t);

    void unlock(T t);
}
