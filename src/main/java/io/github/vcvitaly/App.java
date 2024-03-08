package io.github.vcvitaly;

import io.github.vcvitaly.locker.ConcurrentHashMapLocker;
import io.github.vcvitaly.locker.GuavaCacheLocker;
import io.github.vcvitaly.locker.Locker;
import io.github.vcvitaly.locker.SimpleHashMapLocker;
import io.github.vcvitaly.locker.StripedLocker;
import io.github.vcvitaly.locker.SynchronizedHashMapLocker;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class App {

    private final Random rand = new Random();

    private final Map<Integer, LongAdder> db = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        LockerType lockerType = LockerType.of(args[0]);
        App app = new App();
        long before = System.currentTimeMillis();
        switch (lockerType) {
            case SIMPLE_HASH -> app.run(new SimpleHashMapLocker<Integer>());
            case SYNCHRONIZED -> app.run(new SynchronizedHashMapLocker<Integer>());
            case CONCURRENT -> app.run(new ConcurrentHashMapLocker<Integer>());
            case GUAVA_CACHE -> app.run(new GuavaCacheLocker<Integer>());
            case STRIPED -> app.run(new StripedLocker<Integer>());
        }
        long after = System.currentTimeMillis();
        System.out.printf("Spent %d ms%n", after - before);
    }

    public void run(Locker locker) {
        int limit = 1_000;
        for (int i = 0; i < limit; i++) {
            int id = rand.nextInt(10);
            Thread.ofVirtual().start(() -> {
                try {
                    locker.lock(id);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    db.computeIfAbsent(id, keu -> new LongAdder()).increment();
                } finally {
                    locker.unlock(id);
                }
            });
        }
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("totalCount: " + limit);
        System.out.println(
                "countFromDb: " + db.values().stream()
                        .map(LongAdder::sumThenReset)
                        .mapToLong(Long::longValue)
                        .sum()
                );
    }
}