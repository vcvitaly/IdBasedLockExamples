package io.github.vcvitaly.locker;

import com.google.common.util.concurrent.Striped;

import java.util.concurrent.locks.Lock;

public class StripedLocker<T> implements Locker<T> {

    private final Striped<Lock> striped = Striped.lock(Runtime.getRuntime().availableProcessors() * 4);

    @Override
    public void lock(T t) {
        Lock lock = striped.get(t);
        lock.lock();
    }

    @Override
    public void unlock(T t) {
        Lock lock = striped.get(t);
        lock.unlock();
    }
}
