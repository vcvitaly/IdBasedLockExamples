package io.github.vcvitaly;

import io.github.vcvitaly.locker.ConcurrentHashMapLocker;
import io.github.vcvitaly.locker.GuavaCacheLocker;
import io.github.vcvitaly.locker.SimpleHashMapLocker;
import io.github.vcvitaly.locker.StripedLocker;
import io.github.vcvitaly.locker.SynchronizedHashMapLocker;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void simpleHashMapLockerTest() {
        new App().run(new SimpleHashMapLocker<Integer>());
    }

    @Test
    void synchronizedHashMapLockerTest() {
        new App().run(new SynchronizedHashMapLocker<Integer>());
        /*for (int i = 0; i < 25; i++) {
            long before = System.currentTimeMillis();
            new App().run(new SynchronizedHashMapLocker<Integer>());
            long after = System.currentTimeMillis();
            System.out.printf("Spent %d ms%n", after - before);
        }*/
    }

    @Test
    void concurrentHashMapLockerTest() {
        new App().run(new ConcurrentHashMapLocker<Integer>());
    }

    @Test
    void guavaCacheLockerTest() {
        new App().run(new GuavaCacheLocker<Integer>());
    }

    @Test
    void stripedLockerTest() {
        new App().run(new StripedLocker<Integer>());
    }
}