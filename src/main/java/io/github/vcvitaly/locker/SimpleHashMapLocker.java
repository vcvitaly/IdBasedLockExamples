package io.github.vcvitaly.locker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleHashMapLocker<T> implements Locker<T> {

    private Map<T, Lock> map = new HashMap<>();

    @Override
    public void lock(T t) {
        Lock lock = map.get(t);
        if (lock == null) {
            lock = map.computeIfAbsent(t, key -> new ReentrantLock());
        }
        lock.lock();
    }

    @Override
    public void unlock(T t) {
        Lock lock = map.get(t);
        if (lock == null) {
            throw new IllegalStateException("Lock for [%s] is null".formatted(t));
        }
        lock.unlock();
    }
}
