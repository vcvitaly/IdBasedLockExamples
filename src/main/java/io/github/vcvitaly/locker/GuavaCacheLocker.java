package io.github.vcvitaly.locker;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.jdi.Value;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GuavaCacheLocker<T> implements Locker<T> {

    private final LoadingCache<T, Lock> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(new CacheLoader<T, Lock>() {
                @Override
                public Lock load(T key) throws Exception {
                    return new ReentrantLock();
                }
            });

    @Override
    public void lock(T t) {
        try {
            Lock lock = cache.get(t);
            lock.lock();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock(T t) {
        try {
            Lock lock = cache.get(t);
            lock.unlock();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
