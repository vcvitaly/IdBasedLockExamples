package io.github.vcvitaly;

import io.github.vcvitaly.locker.Locker;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class App {

//    private final LongAdder totalCount = new LongAdder();
    private final Random rand = new Random();

    private final Map<Integer, LongAdder> db = new ConcurrentHashMap<>();

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